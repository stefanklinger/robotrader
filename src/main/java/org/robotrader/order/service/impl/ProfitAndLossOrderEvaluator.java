package org.robotrader.order.service.impl;

import java.util.List;

import org.robotrader.order.domain.Order;
import org.robotrader.order.service.OrderEvaluator;

public class ProfitAndLossOrderEvaluator implements
		OrderEvaluator<ProfitAndLossEvaluation> {

	@Override
	public ProfitAndLossEvaluation evaluate(List<Order> orders) {
		// TODO: do not hardcode this amount
		ProfitAndLossEvaluation evaluation = new ProfitAndLossEvaluation();
		for (Order order : orders) {
			evaluation.addOrder(order);
		}
		return evaluation;
	}
}
