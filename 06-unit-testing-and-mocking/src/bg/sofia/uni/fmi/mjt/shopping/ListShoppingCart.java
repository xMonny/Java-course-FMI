package bg.sofia.uni.fmi.mjt.shopping;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

import bg.sofia.uni.fmi.mjt.shopping.item.Item;
import bg.sofia.uni.fmi.mjt.shopping.item.Pair;
import bg.sofia.uni.fmi.mjt.shopping.item.comparators.PairQuantityComparator;

public class ListShoppingCart implements ShoppingCart {
    private final List<Item> items;
    private final ProductCatalog catalog;

    public ListShoppingCart(ProductCatalog catalog) {
        this.catalog = catalog;
        this.items = new ArrayList<>();
    }

    private boolean isItemFound(Item item) {
        return items.contains(item);
    }

    public List<Item> getItems() {
        return this.items;
    }

    private Map<Item, Integer> createQuantityMap() {
        Map<Item, Integer> itemsAndQuantity = new HashMap<>();
        for (Item item : items) {
            if (itemsAndQuantity.containsKey(item)) {
                itemsAndQuantity.put(item, itemsAndQuantity.get(item) + 1);
            } else {
                itemsAndQuantity.put(item, 1);
            }
        }
        return itemsAndQuantity;
    }

    @Override
    public Collection<Item> getUniqueItems() {
        return new HashSet<>(items);
    }

    @Override
    public Collection<Item> getSortedItems() {
        Map<Item, Integer> itemsAndQuantity = createQuantityMap();
        List<Pair> sortedItemsByQuantity = new ArrayList<>();
        for (Map.Entry<Item, Integer> entry : itemsAndQuantity.entrySet()) {
            sortedItemsByQuantity.add(new Pair(entry.getKey(), entry.getValue()));
        }
        sortedItemsByQuantity.sort(new PairQuantityComparator());
        List<Item> sortedItems = new ArrayList<>();
        for (Pair p : sortedItemsByQuantity) {
            sortedItems.add(p.item());
        }
        return sortedItems;
    }

    @Override
    public void addItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add item: Null item is passed");
        }
        items.add(item);
    }

    @Override
    public void removeItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot remove item: Null item is passed");
        }
        if (!isItemFound(item)) {
            throw new ItemNotFoundException("Cannot remove item: Item \"" + item.getId() + "\" doesn't exist");
        }
        items.remove(item);
    }

    @Override
    public double getTotal() {
        double total = 0;
        ProductInfo info;
        for (Item item : items) {
            info = catalog.getProductInfo(item.getId());
            total += info.price();
        }
        return total;
    }
}
