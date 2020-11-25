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

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MapShoppingCartTest {
    @Mock
    private ProductCatalog catalogMock;

    private MapShoppingCart mapShoppingCart;
    private Item apple;
    private Item chocolate;

    @Before
    public void prepareMapShoppingCart() {
        mapShoppingCart = new MapShoppingCart(catalogMock);
        apple = new Apple("apple");
        chocolate = new Chocolate("chocolate");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddItemIllegalArgument() {
        mapShoppingCart.addItem(null);
    }

    @Test
    public void testAddItemFirstItem() {
        mapShoppingCart.addItem(apple);
        assertEquals(1, mapShoppingCart.getItems().size());
        assertTrue(mapShoppingCart.getItems().containsKey(apple));
        assertTrue(mapShoppingCart.getItems().containsValue(1));
    }

    @Test
    public void testAddItemSameItem() {
        mapShoppingCart.addItem(apple);
        mapShoppingCart.addItem(apple);
        assertEquals(1, mapShoppingCart.getItems().size());
        assertTrue(mapShoppingCart.getItems().containsKey(apple));
        assertTrue(mapShoppingCart.getItems().containsValue(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveItemIllegalArgument() {
        mapShoppingCart.removeItem(null);
    }

    @Test(expected = ItemNotFoundException.class)
    public void testRemoveItemNotFound() {
        mapShoppingCart.addItem(chocolate);
        mapShoppingCart.removeItem(apple);
    }

    @Test
    public void testRemoveItemWithMoreOccurrences() {
        mapShoppingCart.addItem(apple);
        mapShoppingCart.addItem(apple);

        mapShoppingCart.removeItem(apple);

        assertEquals(1, mapShoppingCart.getItems().size());
        assertTrue(mapShoppingCart.getItems().containsKey(apple));
        assertTrue(mapShoppingCart.getItems().containsValue(1));
    }

    @Test
    public void testRemoveItemWithOneOccurrence() {
        mapShoppingCart.addItem(apple);

        mapShoppingCart.removeItem(apple);

        assertEquals(0, mapShoppingCart.getItems().size());
    }

    @Test
    public void testGetUniqueItemsTwoItems() {
        mapShoppingCart.addItem(apple);
        mapShoppingCart.addItem(apple);
        assertEquals(1, mapShoppingCart.getUniqueItems().size());
        assertTrue(mapShoppingCart.getUniqueItems().contains(apple));
    }

    @Test
    public void testGetUniqueItemsThreeItems() {
        mapShoppingCart.addItem(apple);
        mapShoppingCart.addItem(apple);
        mapShoppingCart.addItem(chocolate);

        List<Item> uniqueItems = List.of(apple, chocolate);

        assertEquals(2, mapShoppingCart.getUniqueItems().size());
        assertEquals(uniqueItems, mapShoppingCart.getUniqueItems());
    }

    @Test
    public void testGetSortedItemsOnlyOneItem() {
        mapShoppingCart.addItem(apple);
        assertEquals(1, mapShoppingCart.getSortedItems().size());
        assertTrue(mapShoppingCart.getSortedItems().contains(apple));
    }

    @Test
    public void testGetSortedItemsFirstPriceMoreThanSecondPrice() {
        mapShoppingCart.addItem(apple);
        mapShoppingCart.addItem(chocolate);

        ProductInfo appleInfo = new ProductInfo("", "", 3.0);
        ProductInfo chocolateInfo = new ProductInfo("", "", 1.0);

        when(catalogMock.getProductInfo("apple")).thenReturn(appleInfo);
        when(catalogMock.getProductInfo("chocolate")).thenReturn(chocolateInfo);

        List<Item> sortedItems = List.of(chocolate, apple);

        assertEquals(2, mapShoppingCart.getSortedItems().size());
        assertEquals(sortedItems, mapShoppingCart.getSortedItems());
    }

    @Test
    public void testGetSortedItemsFirstPriceLessThanSecondPrice() {
        mapShoppingCart.addItem(apple);
        mapShoppingCart.addItem(chocolate);

        ProductInfo appleInfo = new ProductInfo("", "", 1.0);
        ProductInfo chocolateInfo = new ProductInfo("", "", 5.0);

        when(catalogMock.getProductInfo("apple")).thenReturn(appleInfo);
        when(catalogMock.getProductInfo("chocolate")).thenReturn(chocolateInfo);

        List<Item> sortedItems = List.of(apple, chocolate);

        assertEquals(2, mapShoppingCart.getSortedItems().size());
        assertEquals(sortedItems, mapShoppingCart.getSortedItems());
    }

    @Test
    public void testGetSortedItemsFirstPriceEqualsSecondPrice() {
        mapShoppingCart.addItem(apple);
        mapShoppingCart.addItem(chocolate);

        ProductInfo appleInfo = new ProductInfo("", "", 5.0);
        ProductInfo chocolateInfo = new ProductInfo("", "", 5.0);

        when(catalogMock.getProductInfo("apple")).thenReturn(appleInfo);
        when(catalogMock.getProductInfo("chocolate")).thenReturn(chocolateInfo);

        List<Item> sortedItems = List.of(apple, chocolate);

        assertEquals(2, mapShoppingCart.getSortedItems().size());
        assertEquals(sortedItems, mapShoppingCart.getSortedItems());
    }

    @Test
    public void testGetTotalWhenEmpty() {
        assertEquals(0.0, mapShoppingCart.getTotal());
    }

    @Test
    public void testGetTotal() {
        mapShoppingCart.addItem(apple);
        mapShoppingCart.addItem(chocolate);
        mapShoppingCart.addItem(apple);

        ProductInfo appleInfo = new ProductInfo("", "", 3.0);
        ProductInfo chocolateInfo = new ProductInfo("", "", 5.0);

        when(catalogMock.getProductInfo("apple")).thenReturn(appleInfo);
        when(catalogMock.getProductInfo("chocolate")).thenReturn(chocolateInfo);

        assertEquals(11.0, mapShoppingCart.getTotal());
    }
}
