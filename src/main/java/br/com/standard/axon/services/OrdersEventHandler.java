package br.com.standard.axon.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import br.com.standard.axon.event.OrderConfirmedEvent;
import br.com.standard.axon.event.OrderCreatedEvent;
import br.com.standard.axon.event.OrderShippedEvent;
import br.com.standard.axon.query.FindAllOrderedProductsQuery;
import br.com.standard.axon.query.Order;

@Service
public class OrdersEventHandler {

    private final Map<String, Order> orders = new HashMap<>();

    @EventHandler
    public void on(OrderCreatedEvent event) {
        String orderID = event.getOrderID();
        orders.put(orderID, new Order(orderID, event.getProductID()));
    }

    @EventHandler
    public void on(OrderConfirmedEvent event) {
        String orderID = event.getOrderID();
        Order order = orders.get(orderID);
        order.setOrderConfirmed();
        orders.put(orderID, order);
    }

    @EventHandler
    public void on(OrderShippedEvent event) {
        String orderID = event.getOrderID();
        Order order = orders.get(orderID);
        order.setOrderShipped();
        orders.put(orderID, order);
    }

    @QueryHandler
    public List<Order> handle(FindAllOrderedProductsQuery query) {
        return new ArrayList<>(orders.values());
    }

}
