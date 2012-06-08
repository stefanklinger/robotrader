package org.robotrader.quote.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.robotrader.quote.domain.Quote;
import org.robotrader.quote.domain.Stock;
import org.robotrader.quote.repository.QuoteRepository;
import org.robotrader.quote.repository.StockRepository;

public class DefaultQuoteManager implements QuoteManager {

	private static final Logger logger = Logger
			.getLogger(DefaultQuoteManager.class);
	private final QuoteRepository quoteRepository;

	private final StockRepository stockRepository;
	
	public DefaultQuoteManager(QuoteRepository quoteRepository, StockRepository stockRepository) {
		this.quoteRepository = quoteRepository;
		this.stockRepository = stockRepository;
	}

	@Override
	public void updateQuotes(Stock stock, List<Quote> quotes) {
		Stock stock2 = stockRepository.findByCode(stock.getCode());
		if (stock2 == null) {
			throw new RuntimeException();
		}
		for (Quote quote : quotes) {
			Quote quote2 = quoteRepository.findByStockAndDate(quote.getStock(),
					quote.getDate());
			if (quote2 == null) {
				quote.setStock(stock2);
				quoteRepository.save(quote);
			} else if (!quote.equals(quote2)) {
				logger.warn(String.format("Quote [%s] does not match quote [%s].", quote, quote2));
			}
		}
	}
}
