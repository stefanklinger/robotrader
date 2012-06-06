package org.robotrader.quote.service;

import java.util.Arrays;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robotrader.quote.domain.Quote;
import org.robotrader.quote.domain.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/robotrader-config.xml")
public class QuoteManagerTest {

	@Autowired
	private QuoteManager quoteManager;
	
	@Autowired
	private QuoteReceiver quoteReceiver;
	
	@Test
	@Ignore
	public void updateQuotes() {
		quoteManager.updateQuotes(new Stock("AAPL"), (Arrays.asList(new Quote(new Stock("AAPL"), new LocalDate(), 1.0, 1.0, 1.0, 1.0))));
	}

	@Test
	@Ignore
	public void foo() {
		List<Quote> quotes = quoteReceiver.receive(new Stock("AAPL"), new LocalDate(2000, 1,1 ), new LocalDate());
		quoteManager.updateQuotes(new Stock("AAPL"), quotes);
	}
}
