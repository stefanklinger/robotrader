package org.robotrader.strategy.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.robotrader.order.domain.BuyOrSell;
import org.robotrader.order.domain.Order;
import org.robotrader.order.domain.OrderType;
import org.robotrader.order.domain.PutOrCall;
import org.robotrader.quote.domain.Quote;
import org.robotrader.strategy.TradingStrategy;
import org.springframework.util.Assert;

public class MovingAverageTradingStrategy implements TradingStrategy {

	private static final Logger log = Logger
			.getLogger(MovingAverageTradingStrategy.class);

	private final List<Quote> quotes = new LinkedList<Quote>();

	private final double amount = 1000.0;

	private final double stopLossPercentage = 0.10;

	private final int days;

	private double average;

	private Order buyOrder;

	private double bestPrice;

	public MovingAverageTradingStrategy(int days) {
		Assert.isTrue(days > 4, "At least 5 days average required.");
		this.days = days;
	}

	@Override
	public List<Order> addQuote(Quote quote) {
		checkAscendingOrderOfQuotes(quote);

		log.debug(String.format("Adding quote [%s]", quote));
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
		log.debug(String.format("Average [%s] on [%s]", average,
				quotes.get(quotes.size() - 1).getDate()));
	}

	public List<Order> hasOrder() {
		if (days != quotes.size()) {
			return null;
		}

		Quote quoteToday = quotes.get(quotes.size() - 1);
		Quote quoteYesterday = quotes.get(quotes.size() - 2);

		if (buyOrder == null) {
			buyOrder = checkForBuyOrder(quoteToday, quoteYesterday);
			bestPrice = buyOrder != null ? buyOrder.price : 0.0;

			return buyOrder != null ? Arrays.asList(buyOrder) : null;
		} else {
			Order order = checkForSellOrder(quoteToday, quoteYesterday);
			if (order != null) {
				List<Order> orders = new ArrayList<Order>();
				orders.add(order);
				buyOrder = checkForBuyOrder(quoteToday, quoteYesterday);
				bestPrice = buyOrder != null ? buyOrder.price : 0.0;
				if (buyOrder != null) {
					orders.add(buyOrder);
				}
				return orders;
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
				&& quoteYesterday.getClose() > average
				&& quoteToday.getClose() < average) {
			log.debug(String
					.format("Todays close [%s] below average [%s], yesterdays above [%s]. Selling Call Order.",
							quoteToday.getClose(), average,
							quoteYesterday.getClose()));
			// put order
			return Order.createSellOrderFromBuyOrder(buyOrder, quoteToday,
					OrderType.Default);
		}

		if (buyOrder.putOrCall == PutOrCall.PUT
				&& quoteYesterday.getClose() < average
				&& quoteToday.getClose() > average) {
			log.debug(String
					.format("Todays close [%s] above average [%s], yesterdays below [%s]. Selling Put Order.",
							quoteToday.getClose(), average,
							quoteYesterday.getClose()));

			// call order
			return Order.createSellOrderFromBuyOrder(buyOrder, quoteToday,
					OrderType.Default);
		}

		return null;
	}

	private Order checkForStopLossOrder(Quote quoteToday) {
		if (buyOrder.putOrCall == PutOrCall.CALL) {
			if (quoteToday.getClose() > bestPrice) {
				bestPrice = quoteToday.getClose();
			}
			double loss = bestPrice - quoteToday.getClose();
			double lossPercentage = loss / bestPrice;
			if (lossPercentage > stopLossPercentage) {
				// sell
				return Order.createSellOrderFromBuyOrder(buyOrder, quoteToday,
						OrderType.Stop_Loss);
			}
		} else {
			if (quoteToday.getClose() < bestPrice) {
				bestPrice = quoteToday.getClose();
			}
			double loss = quoteToday.getClose() - bestPrice;
			double lossPercentage = loss / bestPrice;
			if (lossPercentage > stopLossPercentage) {
				// sell
				return Order.createSellOrderFromBuyOrder(buyOrder, quoteToday,
						OrderType.Stop_Loss);
			}
		}

		return null;
	}

	private Order checkForBuyOrder(Quote quoteToday, Quote quoteYesterday) {
		// TODO: Can I really buy at close price?
		double price = quoteToday.getClose();
		int quantity = (int) (amount / price);

		if (quoteYesterday.getClose() > average
				&& quoteToday.getClose() < average) {
			log.debug(String
					.format("Todays close [%s] below average [%s], yesterdays above [%s]. Buying Put Order.",
							quoteToday.getClose(), average,
							quoteYesterday.getClose()));
			// put order
			return new Order(quantity, price, BuyOrSell.Buy, PutOrCall.PUT,
					quoteToday.getDate(), OrderType.Default);
		}

		if (quoteYesterday.getClose() < average
				&& quoteToday.getClose() > average) {
			log.debug(String
					.format("Todays close [%s] above average [%s], yesterdays below [%s]. Buying Call Order.",
							quoteToday.getClose(), average,
							quoteYesterday.getClose()));

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
