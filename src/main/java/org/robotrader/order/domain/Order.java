package org.robotrader.order.domain;

public class Order {

	public int quantity;
	
	public double price;
	
	public BuyOrSell buyOrSell;
	
	public Order(int quantity, double price, BuyOrSell buyOrSell) {
		this.quantity = quantity;
		this.price = price;
		this.buyOrSell = buyOrSell;
	}
}
