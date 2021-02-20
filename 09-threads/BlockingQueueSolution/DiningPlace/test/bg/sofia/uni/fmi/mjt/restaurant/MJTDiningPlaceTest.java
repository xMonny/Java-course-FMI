package bg.sofia.uni.fmi.mjt.restaurant;

import bg.sofia.uni.fmi.mjt.restaurant.customer.AbstractCustomer;
import bg.sofia.uni.fmi.mjt.restaurant.customer.Customer;
import bg.sofia.uni.fmi.mjt.restaurant.customer.VipCustomer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MJTDiningPlaceTest {

    @Test(expected = IllegalArgumentException.class, timeout = 4000)
    public void testMJTDiningPlaceConstructorWhenIllegalArgument() {
        Restaurant diningPlace = new MJTDiningPlace(-1);
        diningPlace.close();
    }

    @Test(timeout = 4000)
    public void testMJTDiningPlaceConstructorWhenCookersAre0() {
        Restaurant diningPlace = new MJTDiningPlace(0);
        diningPlace.close();
        assertEquals(0, diningPlace.getChefs().length);
    }

    @Test(timeout = 4000)
    public void testMJTDiningPlaceConstructorWhenCookersAre1() {
        Restaurant diningPlace = new MJTDiningPlace(1);
        diningPlace.close();
        assertEquals("Number of chefs should be 1", 1, diningPlace.getChefs().length);
        assertEquals("ID of chef should be 0", 0, diningPlace.getChefs()[0].getChefId());
    }

    @Test(timeout = 4000)
    public void testMJTDiningPlaceConstructorWhenCookersAre2() {
        Restaurant diningPlace = new MJTDiningPlace(2);
        diningPlace.close();
        assertEquals("Number of chefs should be 2", 2, diningPlace.getChefs().length);
        assertEquals("ID of first chef should be 0", 0, diningPlace.getChefs()[0].getChefId());
        assertEquals("ID of second chef should be 1", 1, diningPlace.getChefs()[1].getChefId());
    }

    @Test(timeout = 4000)
    public void testSubmitOrderWhenHaveOnlyOneCustomer() throws Exception {
        Restaurant diningPlace = new MJTDiningPlace(0);
        AbstractCustomer customer = new Customer(diningPlace);
        customer.start();
        customer.join();
        diningPlace.close();
        assertEquals(1, diningPlace.getOrdersCount());
    }

    @Test(timeout = 4000)
    public void testSubmitOrderWhenHaveTwoCustomers() throws Exception {
        Restaurant diningPlace = new MJTDiningPlace(0);
        AbstractCustomer customer = new Customer(diningPlace);
        AbstractCustomer vipCustomer = new VipCustomer(diningPlace);
        customer.start();
        vipCustomer.start();
        customer.join();
        vipCustomer.join();
        diningPlace.close();
        assertEquals(2, diningPlace.getOrdersCount());
    }

    @Test(timeout = 4000)
    public void testNextOrderWhenHaveOneCustomerAndOneChef() throws Exception {
        Restaurant diningPlace = new MJTDiningPlace(1);
        AbstractCustomer customer = new Customer(diningPlace);
        customer.start();
        customer.join();
        diningPlace.close();
        assertEquals("Submitted orders should be 1", 1, diningPlace.getOrdersCount());
        assertEquals("The only chef should have only 1 cooked meal",
                1, diningPlace.getChefs()[0].getTotalCookedMeals());
    }

    @Test(timeout = 4000)
    public void testNextOrderWhenHaveEightCustomersAndThreeChefs() throws Exception {
        Restaurant diningPlace = new MJTDiningPlace(3);

        AbstractCustomer usualCustomer = new Customer(diningPlace);
        usualCustomer.start();
        AbstractCustomer vipCustomer = new VipCustomer(diningPlace);
        vipCustomer.start();
        AbstractCustomer usualCustomer1 = new Customer(diningPlace);
        usualCustomer1.start();
        AbstractCustomer vipCustomer1 = new VipCustomer(diningPlace);
        vipCustomer1.start();
        AbstractCustomer usualCustomer2 = new Customer(diningPlace);
        usualCustomer2.start();
        AbstractCustomer vipCustomer2 = new VipCustomer(diningPlace);
        vipCustomer2.start();
        AbstractCustomer usualCustomer3 = new Customer(diningPlace);
        usualCustomer3.start();
        AbstractCustomer vipCustomer3 = new VipCustomer(diningPlace);
        vipCustomer3.start();

        usualCustomer.join();
        vipCustomer.join();
        usualCustomer1.join();
        vipCustomer1.join();
        usualCustomer2.join();
        vipCustomer2.join();
        usualCustomer3.join();
        vipCustomer3.join();

        diningPlace.close();

        int chefsNumberCookedMeals = 0;
        for (Chef c : diningPlace.getChefs()) {
            chefsNumberCookedMeals += c.getTotalCookedMeals();
        }
        assertEquals("Submitted orders should be 8", 8, diningPlace.getOrdersCount());
        assertEquals("Number of all chefs' cooked meals should be 8", 8, chefsNumberCookedMeals);
    }
}
