package model.restaurant;


public class MenuItem {
    private int id;
    private String name;
    private double price;


    public MenuItem(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }


    public double getPrice() { return price; }
    public String getName() { return name; }
}