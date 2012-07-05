package org.robotrader.order.service.impl;

import org.robotrader.order.domain.Order;
import org.robotrader.order.domain.PutOrCall;
import org.robotrader.order.service.OrderEvaluation;
import org.springframework.util.Assert;

public class ProfitAndLossEvaluation implements OrderEvaluation {

	private double startBalance;
	
	private double balance;
	
	private Order buyOrder;
	
	public ProfitAndLossEvaluation() {
		// TODO: do not hardcode this amount
		this(1000.0);
	}
	
	public ProfitAndLossEvaluation(double startBalance) {
		this.startBalance = startBalance;
		this.balance = startBalance;
	}

	public void addOrder(Order order) {
		switch (order.buyOrSell) {
		case Buy:
			buyOrder = order;
			balance -= order.getAmount();
			break;
		case Sell:
			Assert.notNull(buyOrder,
					"Cannot Sell without a previous Buy Order.");
			Assert.isTrue(buyOrder.putOrCall == order.putOrCall,
					"Buy and Sell Order need to be of the same type.");

			double difference = order.putOrCall == PutOrCall.CALL ? order
					.getAmount() - buyOrder.getAmount() : buyOrder
					.getAmount() - order.getAmount();
			balance += buyOrder.getAmount();
			balance += difference;
			buyOrder = null;
		}
	}
	
	public double getProfitAndLossAmount() {
		return balance - startBalance;
	}
	
	public double getProfitAndLossPercentage() {
		return 100.0 * getProfitAndLossAmount() / startBalance;
	}
}
