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
		Assert.isTrue(days > 5);
		this.days = days;
	}

	@Override
	public void addQuote(Quote quote) {
		currentOrder = null;
		calculateAverage();
		quotes.add(quote);
		if (quotes.size() > days) {
			quotes.remove(0);
		}
	}

	private void calculateAverage() {
		if (days != quotes.size()) {
			return;
		}
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
}
