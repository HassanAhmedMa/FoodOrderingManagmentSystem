package model.review;

import model.restaurant.Restaurant;
import model.user.Customer;

public class Review {

    private int id;
    private Customer customer;
    private Restaurant restaurant;
    private int rating;
    private String comment;

    public Review(int id, Customer customer, Restaurant restaurant, int rating, String comment) {
        this.id = id;
        this.customer = customer;
        this.restaurant = restaurant;
        this.rating = rating;
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }
}
