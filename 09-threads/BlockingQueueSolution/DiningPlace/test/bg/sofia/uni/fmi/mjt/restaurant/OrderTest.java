package bg.sofia.uni.fmi.mjt.restaurant;

import bg.sofia.uni.fmi.mjt.restaurant.customer.AbstractCustomer;
import bg.sofia.uni.fmi.mjt.restaurant.customer.Customer;
import bg.sofia.uni.fmi.mjt.restaurant.customer.VipCustomer;
import org.junit.Test;

import java.util.PriorityQueue;
import java.util.Queue;

import static org.junit.Assert.assertEquals;

public class OrderTest {

    private static final Meal pizza = Meal.PIZZA; //time preparation: 20
    private static final Meal musaka = Meal.MUSAKA; //time preparation: 30
    private static final Meal salad = Meal.SALAD; //time preparation: 5
    private static final Meal spaghetti = Meal.SPAGHETTI; //time preparation 25

    private static final AbstractCustomer usualCustomer1 = new Customer(null);
    private static final AbstractCustomer usualCustomer2 = new Customer(null);
    private static final AbstractCustomer vipCustomer1 = new VipCustomer(null);
    private static final AbstractCustomer vipCustomer2 = new VipCustomer(null);

    @Test
    public void testComparatorWhenHaveUsualPizzaOrderAndUsualSaladOrder() {
        Queue<Order> queue = new PriorityQueue<>();
        Order usualPizzaOrder = new Order(pizza, usualCustomer1);
        Order usualSaladOrder = new Order(salad, usualCustomer2);
        queue.add(usualPizzaOrder);
        queue.add(usualSaladOrder);

        Order firstOrder = queue.poll();
        Order secondOrder = queue.poll();

        assertEquals("First order should be the usual pizza order", usualPizzaOrder, firstOrder);
        assertEquals("Second order should be the usual salad order", usualSaladOrder, secondOrder);
    }

    @Test
    public void testComparatorWhenHaveVipMusakaOrderAndVipSpaghettiOrder() {
        Queue<Order> queue = new PriorityQueue<>();
        Order vipMusakaOrder = new Order(musaka, vipCustomer1);
        Order vipSpaghettiOrder = new Order(spaghetti, vipCustomer2);
        queue.add(vipMusakaOrder);
        queue.add(vipSpaghettiOrder);

        Order firstOrder = queue.poll();
        Order secondOrder = queue.poll();

        assertEquals("First order should be the vip musaka order", vipMusakaOrder, firstOrder);
        assertEquals("Second order should be the vip spaghetti order", vipSpaghettiOrder, secondOrder);
    }

    @Test
    public void testComparatorWhenHaveUsualSaladOrderAndVipPizzaOrder() {
        Queue<Order> queue = new PriorityQueue<>();
        Order usualSaladOrder = new Order(salad, usualCustomer1);
        Order vipPizzaOrder = new Order(pizza, vipCustomer1);
        queue.add(usualSaladOrder);
        queue.add(vipPizzaOrder);

        Order firstOrder = queue.poll();
        Order secondOrder = queue.poll();

        assertEquals("First order should be the vip pizza order", vipPizzaOrder, firstOrder);
        assertEquals("Second order should be the usual salad order", usualSaladOrder, secondOrder);
    }

    @Test
    public void testComparatorWhenHaveVipPizzaOrderAndUsualSaladOrder() {
        Queue<Order> queue = new PriorityQueue<>();
        Order vipPizzaOrder = new Order(pizza, vipCustomer1);
        Order usualSaladOrder = new Order(salad, usualCustomer1);
        queue.add(vipPizzaOrder);
        queue.add(usualSaladOrder);

        Order firstOrder = queue.poll();
        Order secondOrder = queue.poll();

        assertEquals("First order should be the vip pizza order", vipPizzaOrder, firstOrder);
        assertEquals("Second order should be the usual salad order", usualSaladOrder, secondOrder);
    }
}
