package org.robotrader.strategy.impl;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;
import org.junit.Test;
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
		for (int i = 0; i < 5; i++) {
			strategy.addQuote(new Quote(stock, new LocalDate(), 1.0, 1.0, 1.0,
					1.0));
		}
		assertEquals(1.0, strategy.getAverage(), 0.001);
	}

	@Test
	public void buyPutOrder() {

	}
}
