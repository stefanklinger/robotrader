package org.robotrader.quote.repository.impl;

import org.hibernate.SessionFactory;
import org.robotrader.core.repository.impl.AbstractGenericHibernateRepository;
import org.robotrader.quote.domain.Stock;
import org.robotrader.quote.repository.StockRepository;

public class HibernateStockRepository extends
		AbstractGenericHibernateRepository<Stock, String> implements StockRepository {

	public HibernateStockRepository(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public Stock findByCode(String code) {
		return (Stock) getSession().createQuery("from Stock where code = :code").setParameter("code", code).uniqueResult();
	}
}
