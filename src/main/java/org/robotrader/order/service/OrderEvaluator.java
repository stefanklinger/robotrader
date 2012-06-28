package org.robotrader.order.service;

import java.util.List;

import org.robotrader.order.domain.Order;

public interface OrderEvaluator<T> {

	T evaluate(List<Order> orders);

}
