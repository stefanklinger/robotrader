package org.robotrader.quote.repository;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robotrader.quote.domain.Quote;
import org.robotrader.quote.domain.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

// TODO: use in-memory DB for tests
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/robotrader-config.xml")
public class QuoteRepositoryTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private QuoteRepository quoteRepository;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Test
	public void save() {
		Stock stock = new Stock("AAPL");
		sessionFactory.getCurrentSession().save(stock);
		
		Quote quote = new Quote();
		quote.setStock(stock);
		quoteRepository.save(quote);
	}
}
