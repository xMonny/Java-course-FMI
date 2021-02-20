package bg.sofia.uni.fmi.mjt.restaurant;

import java.util.PriorityQueue;
import java.util.Queue;

public class MJTDiningPlace implements Restaurant {

    private final Queue<Order> orders = new PriorityQueue<>();
    private final Chef[] restaurantChefs;
    private boolean isOpened = true;
    private int submittedOrders;
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
    public synchronized void submitOrder(Order order) {
        if (isOpened) {
            orders.add(order);
            submittedOrders++;
            this.notifyAll();
        }
    }

    @Override
    public synchronized Order nextOrder() {
        while (isOpened && !hasOrders()) {
            try {
                this.wait(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Order orderToCook = orders.poll();
        if (orderToCook == null) {
            return null;
        }
        orders.remove(orderToCook);
        return orderToCook;
    }

    @Override
    public int getOrdersCount() {
        return submittedOrders;
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
