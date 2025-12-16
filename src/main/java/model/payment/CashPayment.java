package model.payment;

public class CashPayment implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        System.out.println("Cash payment of " + amount + " on delivery");
    }

    @Override
    public String getMethod() {
        return "CASH";
    }
}
