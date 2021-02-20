package bg.sofia.uni.fmi.mjt.restaurant;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MJTDiningPlace implements Restaurant {

    private final BlockingQueue<Order> orders = new PriorityBlockingQueue<>();
    private final Chef[] restaurantChefs;
    private boolean isOpened = true;
    private final AtomicInteger submittedOrders = new AtomicInteger();
    private int cookerID;

    public MJTDiningPlace(int numberOfCookers) {
        if (numberOfCookers < 0) {
            throw new IllegalArgumentException("Restaurant's number of chefs should be positive");
        }
        restaurantChefs = new Chef[numberOfCookers];
        for (int i = 0; i < numberOfCookers; i++) {
            Chef chef = new Chef(cookerID++, this);
            restaurantChefs[i] = chef;
            chef.start();
        }
    }

    private boolean hasCookingChef() {
        for (Chef c : restaurantChefs) {
            if (c.isAlive()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasOrders() {
        return !orders.isEmpty();
    }

    public boolean isRestaurantOpened() {
        return isOpened;
    }

    @Override
    public void submitOrder(Order order) {
        if (isOpened) {
            orders.add(order);
            submittedOrders.getAndIncrement();
        }
    }

    @Override
    public Order nextOrder() {
        Order orderToCook = null;
        try {
            orderToCook = orders.poll(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        orders.remove(orderToCook);
        return orderToCook;
    }

    @Override
    public int getOrdersCount() {
        return submittedOrders.get();
    }

    @Override
    public Chef[] getChefs() {
        return restaurantChefs;
    }

    @Override
    public void close() {
        isOpened = false;

        while (hasCookingChef()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
