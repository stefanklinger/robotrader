package org.robotrader.order.domain;

import org.springframework.util.Assert;

public class Order {

	public int quantity;
	
	public double price;
	
	public BuyOrSell buyOrSell;
	
	public PutOrCall putOrCall;
	
	public Order(int quantity, double price, BuyOrSell buyOrSell, PutOrCall putOrCall) {
		this.quantity = quantity;
		this.price = price;
		this.buyOrSell = buyOrSell;
		this.putOrCall = putOrCall;
	}
	
	public static Order createSellOrderFromBuyOrder(Order buyOrder, double price) {
		Assert.isTrue(buyOrder.buyOrSell == BuyOrSell.Buy);
		return new Order(buyOrder.quantity, price, BuyOrSell.Sell, buyOrder.putOrCall);
	}
}
