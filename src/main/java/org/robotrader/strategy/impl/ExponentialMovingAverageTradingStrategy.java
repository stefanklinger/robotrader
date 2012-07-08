package org.robotrader.strategy.impl;

import org.apache.log4j.Logger;
import org.robotrader.quote.domain.Quote;

public class ExponentialMovingAverageTradingStrategy extends
		SimpleMovingAverageTradingStrategy {

	private static final Logger log = Logger
			.getLogger(ExponentialMovingAverageTradingStrategy.class);

	private final double multiplier;

	public ExponentialMovingAverageTradingStrategy(int days) {
		super(days);
		multiplier = 2.0 / (days + 1);
	}

	@Override
	protected double calculateAverage(Quote latestQuote) {
		if (quotes.size() < days) {
			return super.calculateAverage(latestQuote);
		}

		double ema = (latestQuote.getClose() - average) * multiplier + average;
		log.debug(String.format("Average [%s] on [%s]", average,
				latestQuote.getDate()));
		return ema;
	}
}
