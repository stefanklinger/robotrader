package org.robotrader.order.service.impl;

import java.util.List;

import org.robotrader.order.domain.Order;
import org.robotrader.order.service.Broker;
import org.robotrader.quote.domain.Quote;
import org.robotrader.strategy.TradingStrategy;

public class DefaultBroker implements Broker {

	@Override
	public List<Order> getOrders(List<Quote> quotes, TradingStrategy strategy) {
		strategy.addQuote(quote);
		strategy.
		return null;
	}

}
