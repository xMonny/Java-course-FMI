package bg.sofia.uni.fmi.mjt.restaurant.customer;

import bg.sofia.uni.fmi.mjt.restaurant.MJTDiningPlace;
import bg.sofia.uni.fmi.mjt.restaurant.Meal;
import bg.sofia.uni.fmi.mjt.restaurant.Order;
import bg.sofia.uni.fmi.mjt.restaurant.Restaurant;

public abstract class AbstractCustomer extends Thread {

    private final Restaurant restaurant;
    private final Meal chosenMeal = Meal.chooseFromMenu();

    public AbstractCustomer(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public void run() {
        if (((MJTDiningPlace) restaurant).isRestaurantOpened()) {
            restaurant.submitOrder(new Order(chosenMeal, this));
        }
    }

    public abstract boolean hasVipCard();
}
