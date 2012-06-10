package org.robotrader.strategy.impl;

import java.util.LinkedList;
import java.util.List;

import org.robotrader.order.PutOrCall;
import org.robotrader.order.domain.BuyOrSell;
import org.robotrader.order.domain.Order;
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
		
		if (previousOrder == null) {
			
			double price = quoteToday.getClose();
			int quantity = (int) (amount / price);

			Quote quoteYesterday = quotes.get(quotes.size() - 2);

			if (quoteYesterday.getHigh() > average
					&& quoteToday.getLow() < average) {
				// sell order
				currentOrder = new Order(quantity, price, BuyOrSell.Buy,
						PutOrCall.PUT);
			}

			if (quoteYesterday.getLow() < average
					&& quoteToday.getHigh() > average) {
				// buy order
				currentOrder = new Order(quantity, price, BuyOrSell.Buy,
						PutOrCall.CALL);
			}
		} else {
			if (previousOrder.putOrCall == PutOrCall.CALL) {
				double loss = previousOrder.price - quoteToday.getLow();
				double lossPercentage = loss / previousOrder.price;
				if (lossPercentage > stopLossPercentage) {
					// sell
				}
			} else {
				
			}
		}

		return currentOrder != null;
	}

	@Override
	public Order getNextOrder() {
		return currentOrder;
	}

}
