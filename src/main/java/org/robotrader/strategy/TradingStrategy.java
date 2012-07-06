package org.robotrader.strategy;

import java.util.List;

import org.robotrader.order.domain.Order;
import org.robotrader.quote.domain.Quote;

public interface TradingStrategy {

	List<Order> addQuote(Quote quote);
	
}
