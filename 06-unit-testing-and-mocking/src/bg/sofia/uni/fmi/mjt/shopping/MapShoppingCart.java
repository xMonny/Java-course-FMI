package bg.sofia.uni.fmi.mjt.shopping;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import bg.sofia.uni.fmi.mjt.shopping.item.Item;
import bg.sofia.uni.fmi.mjt.shopping.item.comparators.ItemPriceComparator;

public class MapShoppingCart implements ShoppingCart {
    private final Map<Item, Integer> items;
    private final ProductCatalog catalog;

    public MapShoppingCart(ProductCatalog catalog) {
        this.catalog = catalog;
        this.items = new HashMap<>();
    }

    private boolean isItemFound(Item item) {
        return items.containsKey(item);
    }

    public Map<Item, Integer> getItems() {
        return this.items;
    }

    @Override
    public Collection<Item> getUniqueItems() {
        return new ArrayList<>(items.keySet());
    }

    @Override
    public Collection<Item> getSortedItems() {
        List<Item> sortedItems = new ArrayList<>(items.keySet());
        sortedItems.sort(new ItemPriceComparator(catalog));
        return sortedItems;
    }

    @Override
    public void addItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add item: Null item is passed");
        }
        if (isItemFound(item)) {
            items.put(item, items.get(item) + 1);
        } else {
            items.put(item, 1);
        }
    }

    @Override
    public void removeItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot remove item: Null item is passed");
        }
        if (!isItemFound(item)) {
            throw new ItemNotFoundException("Cannot remove item: Item \"" + item.getId() + "\" doesn't exist");
        }
        int occurrences = items.get(item);
        if (occurrences == 1) {
            items.remove(item);
        } else {
            items.put(item, occurrences - 1);
        }
    }

    @Override
    public double getTotal() {
        int total = 0;
        ProductInfo info;
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            info = catalog.getProductInfo(entry.getKey().getId());
            total += info.price() * entry.getValue();
        }
        return total;
    }
}
