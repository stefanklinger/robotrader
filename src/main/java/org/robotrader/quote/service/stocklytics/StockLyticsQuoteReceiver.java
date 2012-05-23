package org.robotrader.quote.service.stocklytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.LocalDate;
import org.robotrader.quote.domain.Quote;
import org.robotrader.quote.domain.Stock;
import org.robotrader.quote.service.QuoteReceiver;
import org.springframework.web.client.RestTemplate;

public class StockLyticsQuoteReceiver implements QuoteReceiver {

	private final RestTemplate restTemplate;

	private final String apiKey;

	public StockLyticsQuoteReceiver(RestTemplate restTemplate, String apiKey) {
		this.restTemplate = restTemplate;
		this.apiKey = apiKey;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Quote> receive(Stock stock, LocalDate from, LocalDate to) {
		Map<String, String> vars = new HashMap<String, String>();
		vars.put("stock", stock.getCode());
		vars.put("api_key", apiKey);
		vars.put("start", from.toString("YYYY-MM-DD"));
		vars.put("end", to.toString("YYYY-MM-DD"));

		Map<String, Object> map = restTemplate
				.getForObject(
						"http://api.stocklytics.com/historicalPrices/1.0/?api_key={api_key}&stock={stock}&format=JSON&order=DESC&start={start}&end={end}",
						Map.class, vars);
		List<Quote> quotes = new ArrayList<Quote>();
		for (Entry<String, Object> entry : map.entrySet()) {
			Quote quote = new Quote();
			quote.setDate(LocalDate.parse(entry.getKey()));
			Map<String, Object> quoteInfo = (Map<String, Object>)entry.getValue();
			quote.setClose(NumberUtils.toDouble((String) quoteInfo.get("close")));
			quote.setHigh(NumberUtils.toDouble((String) quoteInfo.get("high")));
			quote.setLow(NumberUtils.toDouble((String) quoteInfo.get("low")));
			quote.setOpen(NumberUtils.toDouble((String) quoteInfo.get("open")));
			quote.setVolume(NumberUtils.toLong((String) quoteInfo.get("volume")));
			quote.setStock(stock);
		}
		return quotes;
	}
}
