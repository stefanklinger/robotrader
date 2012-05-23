package org.robotrader.quote.stocklytics;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robotrader.quote.domain.Stock;
import org.robotrader.quote.service.stocklytics.StockLyticsQuoteReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/robotrader-config.xml")
public class StockLyticsQuoteReceiverTest {

	@Autowired
	private StockLyticsQuoteReceiver quoteReceiver;
	
	@Test
	public void foo() {
		quoteReceiver.receive(new Stock("AAPL"), new LocalDate(2012, 1, 1), new LocalDate(2012, 1, 10));
	}
}
