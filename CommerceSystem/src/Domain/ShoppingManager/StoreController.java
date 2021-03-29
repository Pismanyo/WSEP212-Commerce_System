package Domain.ShoppingManager;
import Domain.CommonClasses.Response;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class StoreController {
    List<Store> stores;

    public StoreController(){
        stores = new LinkedList<>();
    }

    public synchronized Response<Boolean> addStore(int id, String name, Inventory inventory,
                                                   DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy){
        for(Store store : stores)
            if(store.getStoreID() == id)
                return new Response<>(false, true, "Store id already exists.");
        stores.add(new Store(id, name, inventory, discountPolicy, purchasePolicy));
        return new Response<>(true, false, "Store has been added successfully.");
    }

    public List<Product> searchByProductName(String productName){
        List<Product> productList = new LinkedList<>();
        for(Store store : stores)
            for(Product product : store.getInventory().getInventory())
                if(product.getName().equals(productName))
                    productList.add(product);

        return productList;
    }

    public List<Product> searchByCategory(String category){
        List<Product> productList = new LinkedList<>();
        for(Store store : stores)
            for(Product product : store.getInventory().getInventory())
                if(product.containsCategory(category))
                    productList.add(product);

        return productList;
    }
}
