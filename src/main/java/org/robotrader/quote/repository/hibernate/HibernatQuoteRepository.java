package org.robotrader.quote.repository.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;
import org.robotrader.core.repository.hibernate.AbstractGenericHibernateRepository;
import org.robotrader.quote.domain.Quote;
import org.robotrader.quote.repository.QuoteRepository;

public class HibernatQuoteRepository extends
		AbstractGenericHibernateRepository<Quote, Long> implements QuoteRepository {

	public HibernatQuoteRepository(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public void save(List<Quote> quotes) {
		for (Quote quote: quotes) {
			save(quote);
		}
	}

}
