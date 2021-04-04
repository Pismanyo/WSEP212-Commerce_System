package TestComponent.AcceptanceTestings.Bridge;

import Server.Domain.CommonClasses.Response;
import Server.Domain.ShoppingManager.*;
import Server.Domain.UserManager.Permissions;
import Server.Domain.UserManager.Purchase;
import Server.Domain.UserManager.User;
import Server.Service.IService;

import java.util.*;

public class ProxyBridge implements IService {
    private IService real;

    public ProxyBridge(){
        real = null;
    }

    public void serRealBridge(IService implementation){
        if (real == null){
            real = implementation;
        }
    }

    @Override
    public void init() {
        if (real != null){
            real.init();
        }
    }

    @Override
    public Response<String> addGuest() {
        if (real != null){
            return real.addGuest();
        }

        return new Response<>("yossi", false, null);
    }

    @Override
    public Response<String> removeGuest(String name) {
        if (real != null){
            return real.removeGuest(name);
        }

        return new Response<>("yossi", false, null);
    }

    @Override
    public Response<Boolean> register(String prevName, String username, String pwd) {
        if (real != null){
            return real.register(prevName, username, pwd);
        }
        return new Response<>(true, false, null);
    }

    @Override
    public Response<String> login(String prevName, String username, String pwd) {
        if (real != null){
            return real.login(prevName, username, pwd);
        }
        return new Response<>("yossi", false, null);
    }

    @Override
    public Response<Collection<Store>> getContent() {
        if (real != null){
            return real.getContent();
        }
        return new Response<>(null, true, null);
    }

    @Override
    public Response<List<Store>> searchByStoreName(String storeName) {
        if (real != null){
            return real.searchByStoreName(storeName);
        }
        return new Response<>(null, true, "not implemented");
    }

    @Override
    public Response<List<ProductDTO>> searchByProductName(String productName) {
        if (real != null){
            return real.searchByProductName(productName);
        }
        return new Response<>(null, true, "not implemented");
    }

    @Override
    public Response<List<ProductDTO>> searchByProductCategory(String category) {
        if (real != null){
            return real.searchByProductCategory(category);
        }
        return new Response<>(null, true, "not implemented");
    }

    @Override
    public Response<List<ProductDTO>> searchByProductKeyword(String keyword) {
        if (real != null){
            return real.searchByProductKeyword(keyword);
        }
        return new Response<>(null, true, "not implemented");
    }

    @Override
    public Response<Boolean> addToCart(String username, int storeID, int productID) {
        if (real != null){
            return real.addToCart(username, storeID, productID);
        }
        return new Response<>(true, false, null);
    }

    @Override
    public Response<Boolean> removeFromCart(String username, int storeID, int productID) {
        if (real != null){
            return real.removeFromCart(username, storeID, productID);
        }
        return new Response<>(true, false, null);
    }

    @Override
    public Response<Map<Integer, Map<ProductDTO, Integer>>> getCartDetails(String username) {
        if (real != null){
            return real.getCartDetails(username);
        }
        return new Response<>(null, true, "not implemented");
    }

    @Override
    public Response<Boolean> updateProductQuantity(String username, int storeID, int productID, int amount) {
        if (real != null){
            return real.updateProductQuantity(username, storeID, productID, amount);
        }
        return new Response<>(true, false, null);
    }

    @Override
    public Response<Boolean> purchaseCartItems(String username, String creditCardDetails) {
        if (real != null){
            return real.purchaseCartItems(username, creditCardDetails);
        }
        return new Response<>(true, false, null);
    }

    @Override
    public User getUserByName(String username) {
        if (real != null){
            return real.getUserByName(username);
        }
        return null;
    }

    @Override
    public Response<String> logout(String userName) {
        if (real != null){
            return real.logout(userName);
        }
        return new Response<>("yossi", false, null);
    }

    @Override
    public Response<Integer> openStore(String username, String storeName) {
        if (real != null){
            return real.openStore(username, storeName);
        }

        return new Response<>(1, false, null);
    }

    @Override
    public Response<Boolean> addProductReview(String username, int storeID, int productID, String review) {
        if (real != null){
            return real.addProductReview(username, storeID, productID, review);
        }

        return new Response<>(true, true, "not implemented");
    }

    @Override
    public Response<List<Purchase>> getPurchaseHistory(String username) {
        if (real != null){
            return real.getPurchaseHistory(username);
        }
        return new Response<>(null, true, "not implemented");
    }

    @Override
    public Response<Boolean> addProductsToStore(String username, ProductDTO productDTO, int amount) {
        if (real != null){
            return real.addProductsToStore(username, productDTO, amount);
        }
        return new Response<>(true, true, "not implemented");
    }

    @Override
    public Response<Boolean> removeProductsFromStore(String username, int storeID, int productID, int amount) {
        if (real != null){
            return real.removeProductsFromStore(username, storeID, productID, amount);
        }
        return new Response<>(true, false, null);
    }

    @Override
    public Response<Boolean> updateProductInfo(String username, int storeID, int productID, double newPrice, String newName) {
        if (real != null){
            return real.updateProductInfo(username, storeID, productID, newPrice, newName);
        }
        return new Response<>(true, false, null);
    }

    @Override
    public List<PurchasePolicy> getPurchasePolicy(String username, int storeID) {
        if (real != null){
            return real.getPurchasePolicy(username, storeID);
        }
        return new LinkedList<>();
    }

    @Override
    public List<DiscountPolicy> getDiscountPolicy(String username, int storeID) {
        if (real != null){
            return real.getDiscountPolicy(username, storeID);
        }
        return new LinkedList<>();
    }

    @Override
    public Response<Boolean> appointStoreOwner(String appointerName, String appointeeName, int storeID) {
        if (real != null){
            return real.appointStoreOwner(appointerName, appointeeName, storeID);
        }
        return new Response<>(true, false, null);
    }

    @Override
    public Response<Boolean> removeOwnerAppointment(String appointerName, String appointeeName, int storeID) {
        if (real != null){
            return real.removeOwnerAppointment(appointerName, appointeeName, storeID);
        }
        return new Response<>(true, false, null);
    }

    @Override
    public Response<Boolean> appointStoreManager(String appointerName, String appointeeName, int storeID) {
        if (real != null){
            return real.appointStoreManager(appointerName, appointeeName, storeID);
        }
        return new Response<>(true, false, null);
    }

    @Override
    public Response<Boolean> addPermission(String permitting, int storeId, String permitted, Permissions permission) {
        if (real != null){
            return real.addPermission(permitting, storeId, permitted, permission);
        }
        return new Response<>(true, false, null);
    }

    @Override
    public Response<Boolean> removePermission(String permitting, int storeId, String permitted, Permissions permission) {
        if (real != null){
            return real.removePermission(permitting, storeId, permitted, permission);
        }
        return new Response<>(true, false, null);
    }

    @Override
    public Response<Boolean> removeManagerAppointment(String appointerName, String appointeeName, int storeID) {
        if (real != null){
            return real.removeManagerAppointment(appointerName, appointeeName, storeID);
        }
        return new Response<>(true, false, null);
    }

    @Override
    public Response<List<User>> getStoreWorkersDetails(String username, int storeID) {
        if (real != null){
            return real.getStoreWorkersDetails(username, storeID);
        }
        return new Response<>(new LinkedList<>(), false, null);
    }

    @Override
    public Response<Purchase> getPurchaseDetails(String username, int storeID) {
        if (real != null){
            return real.getPurchaseDetails(username, storeID);
        }
        return new Response<>(new Purchase(), false, null);
    }

    @Override
    public Response<List<Purchase>> getUserPurchaseHistory(String adminName, String username) {
        if (real != null){
            return real.getUserPurchaseHistory(adminName, username);
        }
        return new Response<>(new LinkedList<>(), false, null);
    }

    @Override
    public Response<Map<ProductDTO, Integer>> getStorePurchaseHistory(String adminName, int storeID) {
        if (real != null){
            return real.getStorePurchaseHistory(adminName, storeID);
        }
        return new Response<>(null, true, "not implemented");
    }
}
