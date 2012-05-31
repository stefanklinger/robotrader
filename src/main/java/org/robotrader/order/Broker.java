package org.robotrader.order;

import java.util.List;

import org.robotrader.order.domain.Order;
import org.robotrader.quote.domain.Quote;
import org.robotrader.strategy.TradingStrategy;

public interface Broker {

	List<Order> getOrders(List<Quote> quotes, TradingStrategy strategy);
}
