package bg.sofia.uni.fmi.mjt.shopping;

import bg.sofia.uni.fmi.mjt.shopping.item.Apple;
import bg.sofia.uni.fmi.mjt.shopping.item.Chocolate;
import bg.sofia.uni.fmi.mjt.shopping.item.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ListShoppingCartTest {
    @Mock
    private ProductCatalog catalogMock;

    private ListShoppingCart listShoppingCart;
    private Item apple;
    private Item chocolate;

    @Before
    public void prepareListShoppingCart() {
        listShoppingCart = new ListShoppingCart(catalogMock);
        apple = new Apple("apple");
        chocolate = new Chocolate("chocolate");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddItemIllegalArgument() {
        listShoppingCart.addItem(null);
    }

    @Test
    public void testAddItemLegalArgument() {
        listShoppingCart.addItem(apple);
        assertEquals(apple, listShoppingCart.getItems().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveItemIllegalArgument() {
        listShoppingCart.removeItem(null);
    }

    @Test(expected = ItemNotFoundException.class)
    public void testRemoveItemNotFound() {
        listShoppingCart.addItem(chocolate);
        listShoppingCart.removeItem(apple);
    }

    @Test
    public void testRemoveItemLegalArgument() {
        listShoppingCart.addItem(apple);
        assertEquals("Before removing the item", 1, listShoppingCart.getItems().size());
        listShoppingCart.removeItem(apple);
        assertEquals("After removing the item", 0, listShoppingCart.getItems().size());
    }

    @Test
    public void testGetUniqueItemsTwoItems() {
        listShoppingCart.addItem(apple);
        listShoppingCart.addItem(apple);
        assertEquals(1, listShoppingCart.getUniqueItems().size());
        assertTrue(listShoppingCart.getUniqueItems().contains(apple));
    }

    @Test
    public void testGetUniqueItemsThreeItems() {
        listShoppingCart.addItem(apple);
        listShoppingCart.addItem(apple);
        listShoppingCart.addItem(chocolate);

        Set<Item> uniqueItems = Set.of(apple, chocolate);

        assertEquals(2, listShoppingCart.getUniqueItems().size());
        assertEquals(uniqueItems, listShoppingCart.getUniqueItems());
    }

    @Test
    public void testGetSortedItemsOnlyOneItem() {
        listShoppingCart.addItem(apple);
        assertEquals(1, listShoppingCart.getSortedItems().size());
        assertTrue(listShoppingCart.getSortedItems().contains(apple));
    }

    @Test
    public void testGetSortedItemsFirstMoreThanSecond() {
        listShoppingCart.addItem(apple);
        listShoppingCart.addItem(chocolate);
        listShoppingCart.addItem(apple);

        List<Item> sortedItemsByQuantity = List.of(apple, chocolate);

        assertEquals("2 apples, 1 chocolate", listShoppingCart.getSortedItems(), sortedItemsByQuantity);
    }

    @Test
    public void testGetSortedItemsFirstLessThanSecond() {
        listShoppingCart.addItem(apple);
        listShoppingCart.addItem(chocolate);
        listShoppingCart.addItem(apple);
        listShoppingCart.addItem(chocolate);
        listShoppingCart.addItem(chocolate);

        List<Item> sortedItemsByQuantity = List.of(chocolate, apple);

        assertEquals("3 chocolate, 2 apples", listShoppingCart.getSortedItems(), sortedItemsByQuantity);
    }

    @Test
    public void testGetSortedItemsFirstEqualsSecond() {
        listShoppingCart.addItem(apple);
        listShoppingCart.addItem(apple);
        listShoppingCart.addItem(chocolate);
        listShoppingCart.addItem(apple);
        listShoppingCart.addItem(chocolate);
        listShoppingCart.addItem(chocolate);

        List<Item> sortedItemsByQuantity = List.of(apple, chocolate);

        assertEquals("3 apples, 3 chocolates", listShoppingCart.getSortedItems(), sortedItemsByQuantity);
    }

    @Test
    public void testGetTotalWhenEmpty() {
        assertEquals(0.0, listShoppingCart.getTotal());
    }

    @Test
    public void testGetTotal() {
        listShoppingCart.addItem(apple);
        listShoppingCart.addItem(chocolate);
        listShoppingCart.addItem(apple);

        ProductInfo appleInfo = new ProductInfo("", "", 3.0);
        ProductInfo chocolateInfo = new ProductInfo("", "", 5.0);

        when(catalogMock.getProductInfo("apple")).thenReturn(appleInfo);
        when(catalogMock.getProductInfo("chocolate")).thenReturn(chocolateInfo);

        assertEquals(11.0, listShoppingCart.getTotal());
    }
}
