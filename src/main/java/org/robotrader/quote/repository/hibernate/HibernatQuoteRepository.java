package org.robotrader.quote.repository.hibernate;

import org.hibernate.SessionFactory;
import org.robotrader.core.repository.hibernate.AbstractGenericHibernateRepository;
import org.robotrader.quote.domain.Quote;
import org.robotrader.quote.repository.QuoteRepository;

public class HibernatQuoteRepository extends
		AbstractGenericHibernateRepository<Quote, Long> implements QuoteRepository {

	public HibernatQuoteRepository(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

}
