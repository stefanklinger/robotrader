package org.robotrader.order.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.LocalDate;
import org.robotrader.quote.domain.Quote;
import org.springframework.util.Assert;

public class Order {

	public int quantity;

	public double price;

	public BuyOrSell buyOrSell;

	public PutOrCall putOrCall;
	
	public LocalDate date;
	
	public OrderType orderType;

	public Order(int quantity, double price, BuyOrSell buyOrSell,
			PutOrCall putOrCall, LocalDate date, OrderType orderType) {
		this.quantity = quantity;
		this.price = price;
		this.buyOrSell = buyOrSell;
		this.putOrCall = putOrCall;
		this.date = date;
		this.orderType = orderType;
	}

	public static Order createSellOrderFromBuyOrder(Order buyOrder, Quote quote, OrderType orderType) {
		Assert.isTrue(buyOrder.buyOrSell == BuyOrSell.Buy);
		return new Order(buyOrder.quantity, quote.getClose(), BuyOrSell.Sell,
				buyOrder.putOrCall, quote.getDate(), orderType);
	}

	public double getAmount() {
		return price * quantity;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
