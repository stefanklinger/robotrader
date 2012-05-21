package org.robotrader.quote.service;

import java.util.List;

import org.joda.time.LocalDate;
import org.robotrader.quote.domain.Quote;
import org.robotrader.quote.domain.Stock;

public interface QuoteReceiver {

	List<Quote> receive(Stock stock, LocalDate from, LocalDate to);
}
