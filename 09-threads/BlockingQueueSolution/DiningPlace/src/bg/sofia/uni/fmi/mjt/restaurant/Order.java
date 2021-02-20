package bg.sofia.uni.fmi.mjt.restaurant;

import bg.sofia.uni.fmi.mjt.restaurant.customer.AbstractCustomer;

public record Order(Meal meal, AbstractCustomer customer) implements Comparable<Order> {

    @Override
    public int compareTo(Order otherOrder) {
        boolean thisHasVipStatus = this.customer.hasVipCard();
        boolean otherHasVipStatus = otherOrder.customer.hasVipCard();
        int thisMealCookingTime = this.meal.getCookingTime();
        int otherMealCookingTime = otherOrder.meal.getCookingTime();

        if (thisHasVipStatus && !otherHasVipStatus) {
            return -1;
        } else if (!thisHasVipStatus && otherHasVipStatus) {
            return 1;
        } else if (thisMealCookingTime < otherMealCookingTime) {
            return 1;
        }
        return -1;
    }
}
