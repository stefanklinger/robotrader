package org.robotrader.strategy.impl;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.robotrader.order.domain.BuyOrSell;
import org.robotrader.order.domain.Order;
import org.robotrader.order.domain.PutOrCall;
import org.robotrader.quote.domain.Quote;
import org.robotrader.quote.domain.Stock;

public class MovingAverageTradingStrategyTest {

	@Test(expected = IllegalArgumentException.class)
	public void lowNumberOfAverageDays() {
		new MovingAverageTradingStrategy(2);
	}

	@Test
	public void buyCallOrder() {
		MovingAverageTradingStrategy strategy = new MovingAverageTradingStrategy(
				5);
		Stock stock = new Stock("TEST");
		LocalDate startDate = new LocalDate();
		for (int i = 0; i < 5; i++) {
			assertNull(strategy.addQuote(new Quote(stock, startDate.plusDays(i), 1.0, 1.0,
					1.0, 1.0)));
		}
		assertEquals(1.0, strategy.getAverage(), 0.001);

		Order order = strategy.addQuote(new Quote(stock, startDate.plusDays(5), 2.0, 2.0,
				2.0, 2.0));
		assertNotNull(order);
		assertEquals(BuyOrSell.Buy, order.buyOrSell);
		assertEquals(PutOrCall.CALL, order.putOrCall);
		assertEquals(2.0, order.price, 0.001);
		assertEquals(500.0, order.quantity, 0.001);
	}
	
	@Test
	public void sellCallOrder() {
		
	}

	@Test
	public void buyPutOrder() {

	}
}
