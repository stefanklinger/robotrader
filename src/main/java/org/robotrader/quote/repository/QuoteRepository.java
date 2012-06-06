package org.robotrader.quote.repository;

import java.util.List;

import org.joda.time.LocalDate;
import org.robotrader.core.repository.GenericRepository;
import org.robotrader.quote.domain.Quote;
import org.robotrader.quote.domain.Stock;

public interface QuoteRepository extends GenericRepository<Quote, Long>{

	void save(List<Quote> quotes);

	Quote findByStockAndDate(Stock stock, LocalDate date);
}
