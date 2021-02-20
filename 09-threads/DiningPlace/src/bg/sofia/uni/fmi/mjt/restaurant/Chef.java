package bg.sofia.uni.fmi.mjt.restaurant;

public class Chef extends Thread {

    private final int id;
    private final Restaurant restaurant;
    private int cookedMeals;

    public Chef(int id, Restaurant restaurant) {
        this.id = id;
        this.restaurant = restaurant;
    }

    public int getChefId() {
        return this.id;
    }

    @Override
    public void run() {
        while (((MJTDiningPlace) restaurant).isRestaurantOpened() || ((MJTDiningPlace) restaurant).hasOrders()) {
            Order orderToCook = restaurant.nextOrder();
            if (orderToCook != null) {
                try {
                    Thread.sleep(orderToCook.meal().getCookingTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cookedMeals++;
            }
        }
    }

    /**
     * Returns the total number of meals that this chef has cooked.
     **/
    public int getTotalCookedMeals() {
        return cookedMeals;
    }
}
