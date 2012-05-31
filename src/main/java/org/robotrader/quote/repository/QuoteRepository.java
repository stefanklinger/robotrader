package org.robotrader.quote.repository;

import java.util.List;

import org.robotrader.core.repository.GenericRepository;
import org.robotrader.quote.domain.Quote;

public interface QuoteRepository extends GenericRepository<Quote, Long>{

	void save(List<Quote> quotes);
}
