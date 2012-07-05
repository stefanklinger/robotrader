package org.robotrader.strategy.impl;

import java.util.LinkedList;
import java.util.List;

import org.robotrader.order.domain.BuyOrSell;
import org.robotrader.order.domain.Order;
import org.robotrader.order.domain.PutOrCall;
import org.robotrader.quote.domain.Quote;
import org.robotrader.strategy.TradingStrategy;
import org.springframework.util.Assert;

public class MovingAverageTradingStrategy implements TradingStrategy {

	private final List<Quote> quotes = new LinkedList<Quote>();

	private final double amount = 1000.0;

	private final double stopLossPercentage = 0.05;

	private final int days;

	private double average;

	private Order currentOrder;

	private Order previousOrder;

	public MovingAverageTradingStrategy(int days) {
		Assert.isTrue(days > 4, "At least 5 days average required.");
		this.days = days;
	}

	@Override
	public void addQuote(Quote quote) {
		checkAscendingOrderOfQuotes(quote);

		currentOrder = null;
		quotes.add(quote);
		calculateAverage();
		
		if (quotes.size() > days) {
			quotes.remove(0);
		}
	}

	private void checkAscendingOrderOfQuotes(Quote latestQuote) {
		if (quotes.size() > 1) {
			Quote previousQuote = quotes.get(quotes.size() - 1);
			Assert.isTrue(latestQuote.getDate()
					.isAfter(previousQuote.getDate()), String.format(
					"Latest quote [%s] is not after previous quote [%s].",
					latestQuote, previousQuote));
		}
	}

	private void calculateAverage() {
		double sum = 0.0;
		for (Quote quote : quotes) {
			sum += quote.getClose();
		}

		average = sum / quotes.size();
	}

	@Override
	public boolean hasOrder() {
		if (days != quotes.size()) {
			return false;
		}

		Quote quoteToday = quotes.get(quotes.size() - 1);
		Quote quoteYesterday = quotes.get(quotes.size() - 2);

		if (previousOrder == null) {
			checkForBuyOrder(quoteToday, quoteYesterday);
		} else {
			checkForStopLossOrder(quoteToday);
			checkForSellOrder(quoteToday, quoteYesterday);
		}

		return currentOrder != null;
	}

	private void checkForSellOrder(Quote quoteToday, Quote quoteYesterday) {

		if (previousOrder.putOrCall == PutOrCall.CALL
				&& quoteYesterday.getHigh() > average
				&& quoteToday.getLow() < average) {
			// put order
			currentOrder = Order.createSellOrderFromBuyOrder(previousOrder,
					quoteToday.getLow());
		}

		if (previousOrder.putOrCall == PutOrCall.PUT
				&& quoteYesterday.getLow() < average
				&& quoteToday.getHigh() > average) {
			// call order
			currentOrder = Order.createSellOrderFromBuyOrder(previousOrder,
					quoteToday.getHigh());
		}

	}

	private void checkForStopLossOrder(Quote quoteToday) {
		// TODO: use best price instead of previous order price
		if (previousOrder.putOrCall == PutOrCall.CALL) {
			double loss = previousOrder.price - quoteToday.getLow();
			double lossPercentage = loss / previousOrder.price;
			if (lossPercentage > stopLossPercentage) {
				// sell
				currentOrder = Order.createSellOrderFromBuyOrder(previousOrder,
						quoteToday.getLow());
			}
		} else {
			double loss = quoteToday.getHigh() - previousOrder.price;
			double lossPercentage = loss / previousOrder.price;
			if (lossPercentage > stopLossPercentage) {
				// sell
				currentOrder = Order.createSellOrderFromBuyOrder(previousOrder,
						quoteToday.getHigh());
			}
		}
	}

	private void checkForBuyOrder(Quote quoteToday, Quote quoteYesterday) {
		// TODO: Can I really buy at close price?
		double price = quoteToday.getClose();
		int quantity = (int) (amount / price);

		if (quoteYesterday.getHigh() > average && quoteToday.getLow() < average) {
			// put order
			currentOrder = new Order(quantity, price, BuyOrSell.Buy,
					PutOrCall.PUT);
		}

		if (quoteYesterday.getLow() < average && quoteToday.getHigh() > average) {
			// call order
			currentOrder = new Order(quantity, price, BuyOrSell.Buy,
					PutOrCall.CALL);
		}
	}

	@Override
	public Order getNextOrder() {
		return currentOrder;
	}

	public double getAverage() {
		return average;
	}
}
