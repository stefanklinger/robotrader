package org.robotrader.quote.service.stocklytics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.LocalDate;
import org.robotrader.quote.domain.Quote;
import org.robotrader.quote.domain.Stock;
import org.robotrader.quote.service.QuoteReceiver;
import org.springframework.http.MediaType;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class StockLyticsQuoteReceiver implements QuoteReceiver {

	private RestTemplate restTemplate = new RestTemplate();

	public void foo() {
		Map<String, String> vars = Collections.singletonMap("stock", "AAPL");
//		Map map = restTemplate.getForObject("http://api.stocklytics.com/historicalPrices/1.0/?api_key=b1305099774d5d67a84b5f1915909cfc6d2d85b5&stock={stock}&format=JSON&order=DESC&start=2011-01-01&end=2011-01-03", Map.class,
//				vars);
//		System.exit(0);
		
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new FormHttpMessageConverter());
		StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
		stringHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(new MediaType("text", "json")));
		messageConverters.add(stringHttpMessageConverter);
		MappingJacksonHttpMessageConverter mappingJacksonHttpMessageConverter = new MappingJacksonHttpMessageConverter();
		mappingJacksonHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(new MediaType("text", "json")));
		messageConverters.add(mappingJacksonHttpMessageConverter);
		restTemplate.setMessageConverters(messageConverters);
		
		restTemplate.setRequestFactory(new CommonsClientHttpRequestFactory());

//		MappingJacksonHttpMessageConverter c =  new MappingJacksonHttpMessageConverter();
//		c.setSupportedMediaTypes(Arrays.asList(new MediaType("text", "json")));
//		c.setObjectMapper(new ObjectMapper());
//		List<HttpMessageConverter<?>> list = new ArrayList<HttpMessageConverter<?>>();
//		list.add(c);
//		r.setMessageConverters(list);
		
		Map map = restTemplate.getForObject("http://api.stocklytics.com/historicalPrices/1.0/?api_key=b1305099774d5d67a84b5f1915909cfc6d2d85b5&stock={stock}&format=JSON&order=DESC&start=2011-01-01&end=2011-01-04", Map.class,
				vars);
		System.out.println(map);
	}

	@Override
	public List<Quote> receive(Stock stock, LocalDate from, LocalDate to) {
		// TODO Auto-generated method stub
		return null;
	}
}
