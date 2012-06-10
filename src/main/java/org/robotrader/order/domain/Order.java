package org.robotrader.order.domain;

import org.robotrader.order.PutOrCall;

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
}
