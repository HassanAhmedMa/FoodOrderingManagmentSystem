package model.order;

import model.order.Order;
import model.order.OrderItem;
import model.payment.PaymentStrategy;
import model.restaurant.Restaurant;
import model.state.OrderState;
import model.user.Customer;
import model.user.DeliveryStaff;

public interface OrderBuilder {

    void createOrder();

    void buildId(int id);

    void buildCustomer(Customer customer);

    void buildRestaurant(Restaurant restaurant);

    void buildDeliveryStaff(DeliveryStaff staff);

    void buildItem(OrderItem item);

    void buildPaymentStrategy(PaymentStrategy strategy);

    void buildDeliveryAddress(String address);

    void buildState(OrderState state);

    Order getResult();
}
