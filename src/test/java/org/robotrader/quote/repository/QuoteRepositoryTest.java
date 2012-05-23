package org.robotrader.quote.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robotrader.quote.domain.Quote;
import org.robotrader.quote.domain.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/robotrader-config.xml")
public class QuoteRepositoryTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private QuoteRepository quoteRepository;
	
	@Test
	public void foo() {
		Stock stock = new Stock("AAPL");
		Quote quote = new Quote();
		quote.setStock(stock);
		quoteRepository.save(quote);
	}
}
