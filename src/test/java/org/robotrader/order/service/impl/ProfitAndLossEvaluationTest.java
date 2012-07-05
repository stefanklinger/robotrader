package org.robotrader.order.service.impl;

import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robotrader.order.domain.Order;
import org.robotrader.order.service.Broker;
import org.robotrader.quote.domain.Quote;
import org.robotrader.quote.repository.QuoteRepository;
import org.robotrader.strategy.impl.MovingAverageTradingStrategy;
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
	public void foo() {
		List<Quote> quotes = quoteRepository.findAllByStockAndDates("CS",
				new LocalDate(2011, 1, 1), new LocalDate(2012, 1, 1));
		Broker broker = new DefaultBroker();
		List<Order> orders = broker.getOrders(quotes,
				new MovingAverageTradingStrategy(40));
		for (Order order: orders) {
			System.out.println(order);
		}
		System.out.println(new ProfitAndLossOrderEvaluator().evaluate(orders)
				.getProfitAndLossAmount());
	}
}
