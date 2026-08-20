package com.adwait.aitrading.service;

import com.adwait.aitrading.domain.OrderType;
import com.adwait.aitrading.model.Coin;
import com.adwait.aitrading.model.Order;
import com.adwait.aitrading.model.OrderItem;
import com.adwait.aitrading.model.User;

import java.util.List;

public interface OrderService {
    Order createOrder(User user, OrderItem orderItem, OrderType orderType);

    Order getOrderById(Long orderId);

    // List<Order> getAllOrdersOfUser(Long userId, OrderType order_type, String asset_symbol);
    List<Order> getAllOrdersOfUser(Long userId, String orderType, String assetSymbol);

    void cancelOrder(Long orderId);

    Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception;


}
