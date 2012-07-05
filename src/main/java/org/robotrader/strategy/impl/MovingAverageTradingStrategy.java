package org.robotrader.strategy.impl;

import java.util.LinkedList;
import java.util.List;

import org.robotrader.order.domain.BuyOrSell;
import org.robotrader.order.domain.Order;
import org.robotrader.order.domain.OrderType;
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

	private Order buyOrder;

	public MovingAverageTradingStrategy(int days) {
		Assert.isTrue(days > 4, "At least 5 days average required.");
		this.days = days;
	}

	@Override
	public Order addQuote(Quote quote) {
		checkAscendingOrderOfQuotes(quote);

		quotes.add(quote);
		calculateAverage();

		if (quotes.size() > days) {
			quotes.remove(0);
		}
		return hasOrder();
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

	public Order hasOrder() {
		if (days != quotes.size()) {
			return null;
		}

		Quote quoteToday = quotes.get(quotes.size() - 1);
		Quote quoteYesterday = quotes.get(quotes.size() - 2);

		if (buyOrder == null) {
			buyOrder = checkForBuyOrder(quoteToday, quoteYesterday);
			return buyOrder;
		} else {
			Order order = checkForSellOrder(quoteToday, quoteYesterday);
			if (order != null) {
				buyOrder = null;
				return order;
			}
		}
		return null;
	}

	private Order checkForSellOrder(Quote quoteToday, Quote quoteYesterday) {
		Order order = checkForStopLossOrder(quoteToday);
		if (order != null) {
			return order;
		}

		if (buyOrder.putOrCall == PutOrCall.CALL
				&& quoteYesterday.getHigh() > average
				&& quoteToday.getLow() < average) {
			// put order
			return Order.createSellOrderFromBuyOrder(buyOrder, quoteToday, OrderType.Default);
		}

		if (buyOrder.putOrCall == PutOrCall.PUT
				&& quoteYesterday.getLow() < average
				&& quoteToday.getHigh() > average) {
			// call order
			return Order.createSellOrderFromBuyOrder(buyOrder, quoteToday, OrderType.Default);
		}

		return null;
	}

	private Order checkForStopLossOrder(Quote quoteToday) {
		// TODO: use best price instead of previous order price
		if (buyOrder.putOrCall == PutOrCall.CALL) {
			double loss = buyOrder.price - quoteToday.getLow();
			double lossPercentage = loss / buyOrder.price;
			if (lossPercentage > stopLossPercentage) {
				// sell
				return Order.createSellOrderFromBuyOrder(buyOrder, quoteToday, OrderType.Stop_Loss);
			}
		} else {
			double loss = quoteToday.getHigh() - buyOrder.price;
			double lossPercentage = loss / buyOrder.price;
			if (lossPercentage > stopLossPercentage) {
				// sell
				return Order.createSellOrderFromBuyOrder(buyOrder, quoteToday, OrderType.Stop_Loss);
			}
		}

		return null;
	}

	private Order checkForBuyOrder(Quote quoteToday, Quote quoteYesterday) {
		// TODO: Can I really buy at close price?
		double price = quoteToday.getClose();
		int quantity = (int) (amount / price);

		if (quoteYesterday.getHigh() > average && quoteToday.getLow() < average) {
			// put order
			return new Order(quantity, price, BuyOrSell.Buy, PutOrCall.PUT,
					quoteToday.getDate(), OrderType.Default);
		}

		if (quoteYesterday.getLow() < average && quoteToday.getHigh() > average) {
			// call order
			return new Order(quantity, price, BuyOrSell.Buy, PutOrCall.CALL,
					quoteToday.getDate(), OrderType.Default);
		}
		return null;
	}

	public double getAverage() {
		return average;
	}
}
