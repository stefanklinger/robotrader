package org.robotrader.quote.stocklytics;

import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robotrader.quote.domain.Quote;
import org.robotrader.quote.domain.Stock;
import org.robotrader.quote.repository.QuoteRepository;
import org.robotrader.quote.service.impl.StockLyticsQuoteReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

// TODO: mock WS for tests
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/robotrader-config.xml")
public class StockLyticsQuoteReceiverTest extends
		AbstractJUnit4SpringContextTests {

	@Autowired
	private StockLyticsQuoteReceiver quoteReceiver;

	@Autowired
	private QuoteRepository quoteRepository;

	@Test
	public void receiveAndStore() {
		List<Quote> quotes = quoteReceiver.receive(new Stock("AAPL"),
				new LocalDate(2001, 1, 1), new LocalDate(2012, 6, 1));
		quoteRepository.save(quotes);
	}
}
