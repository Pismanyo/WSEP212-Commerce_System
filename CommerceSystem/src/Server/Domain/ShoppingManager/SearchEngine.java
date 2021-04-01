package Server.Domain.ShoppingManager;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * singleton class
 * supplies searching service for store controller
 */
public class SearchEngine {

    private static volatile SearchEngine searcher = null;

    public static SearchEngine getInstance(){
        if(searcher == null){
            synchronized (StoreController.class){
                if(searcher == null)
                    searcher = new SearchEngine();
            }
        }
        return searcher;
    }
    public List<Product> searchByProductName(String productName){
        Collection<Store> stores = StoreController.getInstance().getStores();

        List<Product> productList = new LinkedList<>();
        if(productName != null) {
            for (Store store : stores)
                for (Product product : store.getInventory().getInventory())
                    if (product.getName().equals(productName))
                        productList.add(product);
        }

        return productList;
    }

    public List<Product> searchByCategory(String category){
        Collection<Store> stores = StoreController.getInstance().getStores();

        List<Product> productList = new LinkedList<>();
        if(category != null) {
            for (Store store : stores)
                for (Product product : store.getInventory().getInventory())
                    if (product.containsCategory(category))
                        productList.add(product);
        }

        return productList;
    }

    public List<Product> searchByKeyWord(String keyword){
        Collection<Store> stores = StoreController.getInstance().getStores();

        List<Product> productList = new LinkedList<>();
        if(keyword != null) {
            for (Store store : stores)
                for (Product product : store.getInventory().getInventory())
                    if (product.containsKeyword(keyword))
                        productList.add(product);
        }
        return productList;
    }
}