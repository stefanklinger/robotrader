package org.robotrader.order.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.robotrader.order.domain.Order;
import org.robotrader.order.service.Broker;
import org.robotrader.quote.domain.Quote;
import org.robotrader.strategy.TradingStrategy;

public class DefaultBroker implements Broker {

	@Override
	public List<Order> getOrders(List<Quote> quotes, TradingStrategy strategy) {
		List<Order> orders = new ArrayList<Order>();
		for (Quote quote: quotes) {
			strategy.addQuote(quote);
			if (strategy.hasOrder()) {
				orders.add(strategy.getNextOrder());
			}
		}
		
		return orders;
	}
}
