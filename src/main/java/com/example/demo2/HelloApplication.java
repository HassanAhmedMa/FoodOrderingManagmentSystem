package com.example.demo2;

import model.controller.DeliveryController;
import model.controller.RestaurantOwnerController;
import model.command.OrderInvoker;
import model.command.PlaceOrderCommand;
import model.decorator.*;
import model.facade.OrderFacade;
import model.observer.CustomerObserver;
import model.order.Order;
import model.order.OrderItem;
import model.payment.CardPayment;
import model.restaurant.MenuItem;
import model.restaurant.Restaurant;
import model.user.Customer;
import model.user.DeliveryStaff;
import model.user.RestaurantOwner;

public class HelloApplication {

    public static void main(String[] args) {

        Customer customer = new Customer(1, "Ali", "ali@mail.com");
        RestaurantOwner owner = new RestaurantOwner(2, "Ahmed", "owner@mail.com");
        DeliveryStaff delivery = new DeliveryStaff(3, "Mohamed", "delivery@mail.com");

        Restaurant restaurant = new Restaurant(1, "Pizza House", owner);
        MenuItem pizza = new MenuItem(1, "Pizza", 100);
        MenuItem burger = new MenuItem(2, "Burger", 80);
        restaurant.addItem(pizza);
        restaurant.addItem(burger);

        OrderFacade facade = new OrderFacade();
        Order order = facade.createOrder(101, customer, restaurant, new CardPayment());

        order.addItem(new OrderItem(pizza, 2));
        order.addItem(new OrderItem(burger, 1));
        order.attach(new CustomerObserver());

        OrderInvoker invoker = new OrderInvoker();
        invoker.setCommand(new PlaceOrderCommand(order));
        invoker.pressButton();

        RestaurantOwnerController ownerController = new RestaurantOwnerController();
        ownerController.acceptOrder(order);

        order.assignDelivery(delivery);
        DeliveryController deliveryController = new DeliveryController();
        deliveryController.deliverOrder(order);

        OrderCost cost = new BasicOrderCost(order.calculateTotal());
        cost = new ExtraCheeseDecorator(cost);
        cost = new FastDeliveryDecorator(cost);

        order.pay(cost.cost());
    }
}
