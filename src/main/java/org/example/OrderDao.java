package org.example;

import java.util.List;

public interface OrderDao {
    Order GetOrder(String id);
    List<Order> GetAllOrders();
    void AddOrder(Order order);
}
