package org.robotrader.order.service.impl;

import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robotrader.order.domain.Order;
import org.robotrader.order.service.Broker;
import org.robotrader.quote.domain.Quote;
import org.robotrader.quote.repository.QuoteRepository;
import org.robotrader.strategy.impl.ExponentialMovingAverageTradingStrategy;
import org.robotrader.strategy.impl.SimpleMovingAverageTradingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/robotrader-config.xml")
public class ProfitAndLossEvaluationTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private QuoteRepository quoteRepository;

	@Test
	@Ignore
	public void simpleMovingAverageStrategy() {
		List<Quote> quotes = quoteRepository.findAllByStockAndDates("CS",
				new LocalDate(2011, 1, 1), new LocalDate(2012, 1, 1));
		Broker broker = new DefaultBroker();
		List<Order> orders = broker.getOrders(quotes,
				new SimpleMovingAverageTradingStrategy(80));
		for (Order order: orders) {
			System.out.println(order);
		}
		System.out.println(new ProfitAndLossOrderEvaluator().evaluate(orders)
				.getProfitAndLossAmount());
	}
	
	@Test
	public void exponentialMovingAverageStrategy() {
		List<Quote> quotes = quoteRepository.findAllByStockAndDates("CS",
				new LocalDate(2011, 1, 1), new LocalDate(2012, 1, 1));
		Broker broker = new DefaultBroker();
		List<Order> orders = broker.getOrders(quotes,
				new ExponentialMovingAverageTradingStrategy(80));
		for (Order order: orders) {
			System.out.println(order);
		}
		System.out.println(new ProfitAndLossOrderEvaluator().evaluate(orders)
				.getProfitAndLossAmount());
	}

}
