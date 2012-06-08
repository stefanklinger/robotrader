package org.robotrader.strategy.impl;

import java.util.LinkedList;
import java.util.List;

import org.robotrader.order.domain.Order;
import org.robotrader.quote.domain.Quote;
import org.robotrader.strategy.TradingStrategy;

public class MovingAverageTradingStrategy implements TradingStrategy {

	private final List<Quote> quotes = new LinkedList<Quote>();

	private final int days;

	private double average;

	private Order currentOrder;

	public MovingAverageTradingStrategy(int days) {
		this.days = days;
	}

	@Override
	public void addQuote(Quote quote) {
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
		if (currentOrder == null) {
			if (quotes.get(quotes.size() - 2).getHigh() > average
					& quotes.get(quotes.size() - 1).getLow() < average) {
				// sell order
				Order order = new Order();
			}
		}
		return false;
	}

	@Override
	public Order getNextOrder() {
		// TODO Auto-generated method stub
		return null;
	}

}
