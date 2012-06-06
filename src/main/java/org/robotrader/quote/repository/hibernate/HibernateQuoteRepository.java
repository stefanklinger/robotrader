package org.robotrader.quote.repository.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;
import org.joda.time.LocalDate;
import org.robotrader.core.repository.hibernate.AbstractGenericHibernateRepository;
import org.robotrader.quote.domain.Quote;
import org.robotrader.quote.domain.Stock;
import org.robotrader.quote.repository.QuoteRepository;

public class HibernateQuoteRepository extends
		AbstractGenericHibernateRepository<Quote, Long> implements QuoteRepository {

	public HibernateQuoteRepository(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public void save(List<Quote> quotes) {
		for (Quote quote: quotes) {
			save(quote);
		}
	}

	@Override
	public Quote findByStockAndDate(Stock stock, LocalDate date) {
		return (Quote) getSession()
				.createQuery("from Quote where stock = :stock and date = :date")
				.setParameter("stock", stock).setParameter("date", date)
				.uniqueResult();
	}
}
