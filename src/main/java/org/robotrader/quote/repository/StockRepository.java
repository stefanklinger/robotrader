package org.robotrader.quote.repository;

import org.robotrader.core.repository.GenericRepository;
import org.robotrader.quote.domain.Stock;

public interface StockRepository extends GenericRepository<Stock, String> {

	Stock findByCode(String code);
}
