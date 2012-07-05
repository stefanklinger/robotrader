package org.robotrader.strategy;

import org.robotrader.order.domain.Order;
import org.robotrader.quote.domain.Quote;

public interface TradingStrategy {

	Order addQuote(Quote quote);
	
}
