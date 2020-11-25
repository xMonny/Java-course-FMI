package bg.sofia.uni.fmi.mjt.shopping.item.comparators;

import bg.sofia.uni.fmi.mjt.shopping.ProductCatalog;
import bg.sofia.uni.fmi.mjt.shopping.ProductInfo;
import bg.sofia.uni.fmi.mjt.shopping.item.Item;

import java.util.Comparator;

public class ItemPriceComparator implements Comparator<Item> {
    private final ProductCatalog catalog;

    public ItemPriceComparator(ProductCatalog catalog) {
        this.catalog = catalog;
    }

    public int compare(Item itemOne, Item itemTwo) {
        ProductInfo infoItemOne = catalog.getProductInfo(itemOne.getId());
        ProductInfo infoItemTwo = catalog.getProductInfo(itemTwo.getId());
        double comparedPrice = Double.compare(infoItemOne.price(), infoItemTwo.price());
        if (comparedPrice > 0) {
            return 1;
        } else if (comparedPrice < 0) {
            return -1;
        }
        return 0;
    }
}
