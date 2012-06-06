package org.robotrader.quote.service;

import java.util.List;

import org.robotrader.quote.domain.Quote;
import org.robotrader.quote.domain.Stock;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface QuoteManager {

	void updateQuotes(Stock stock, List<Quote> quotes);
}
