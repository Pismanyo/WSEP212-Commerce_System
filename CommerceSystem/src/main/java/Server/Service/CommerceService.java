package Server.Service;

import Server.Domain.CommonClasses.Response;
import Server.Domain.ShoppingManager.DiscountPolicy;
import Server.Domain.ShoppingManager.ProductDTO;
import Server.Domain.ShoppingManager.PurchasePolicy;
import Server.Domain.ShoppingManager.Store;
import Server.Domain.UserManager.CommerceSystem;
import Server.Domain.UserManager.Permissions;
import Server.Domain.UserManager.PurchaseDTO;
import Server.Domain.UserManager.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * this class also serves as the real Bridge in implementing the acceptance testings
 */
public class CommerceService implements IService{

    private final CommerceSystem commerceSystem;
    private CommerceService()
    {
        commerceSystem = CommerceSystem.getInstance();
    }

    // Inner class to provide instance of class
    private static class CreateThreadSafeSingleton
    {
        private static final CommerceService INSTANCE = new CommerceService();
    }

    public static CommerceService getInstance()
    {
        return CreateThreadSafeSingleton.INSTANCE;
    }


    @Override
    public void init() {
        commerceSystem.init();
    }

    @Override
    public Response<String> addGuest() {
        return commerceSystem.addGuest();
    }

    @Override
    public Response<String> removeGuest(String name) {
        return commerceSystem.removeGuest(name);
    }

    @Override
    public Response<Boolean> register(String prevName, String username, String pwd) {
        return commerceSystem.register(prevName, username, pwd);
    }

    @Override
    public Response<String> login(String prevName, String username, String pwd) {
        return commerceSystem.login(prevName, username, pwd);
    }

    @Override
    public Response<Collection<Store>> getContent() {
        return commerceSystem.getContent();
    }

    @Override
    public Response<List<Store>> searchByStoreName(String storeName) {
        return commerceSystem.searchByStoreName(storeName);
    }

    @Override
    public Response<List<ProductDTO>> searchByProductName(String productName) {
        return commerceSystem.searchByProductName(productName);
    }

    @Override
    public Response<List<ProductDTO>> searchByProductCategory(String category) {
        return commerceSystem.searchByProductCategory(category);
    }

    @Override
    public Response<List<ProductDTO>> searchByProductKeyword(String keyword) {
        return commerceSystem.searchByProductKeyword(keyword);
    }

    @Override
    public Response<Boolean> addToCart(String username, int storeID, int productID) {
        return CommerceSystem.getInstance().addToCart(username, storeID, productID);
    }

    @Override
    public Response<Boolean> removeFromCart(String username,  int storeID, int productID) {
        return CommerceSystem.getInstance().removeFromCart(username, storeID, productID);
    }

    @Override
    public Response<Map<Integer, Map<ProductDTO, Integer>>> getCartDetails(String username) {
        return CommerceSystem.getInstance().getCartDetails(username);
    }

    @Override
    public Response<Boolean> updateProductQuantity(String username,  int storeID, int productID, int amount){
        return CommerceSystem.getInstance().updateProductQuantity(username, storeID, productID, amount);
    }

    @Override
    public Response<Boolean> directPurchase(String username, String bankAccount, String location) {
        return commerceSystem.directPurchase(username, bankAccount, location);
    }

    @Override
    public User getUserByName(String username) {
        return commerceSystem.getUserByName(username);
    }

    @Override
    public Response<String> logout(String userName) {
        return commerceSystem.logout(userName);
    }

    @Override
    public Response<Integer> openStore(String username, String storeName) {
        return commerceSystem.openStore(username, storeName);
    }

    @Override
    public Response<Boolean> addProductReview(String username, int storeID, int productID, String review) {
        return commerceSystem.addProductReview(username, storeID, productID, review);
    }

    @Override
    public Response<List<PurchaseDTO>> getPurchaseHistory(String username) {
        return commerceSystem.getPurchaseHistory(username);
    }

    @Override
    public Response<Boolean> addProductsToStore(String username, ProductDTO productDTO, int amount) {
        return CommerceSystem.getInstance().addProductsToStore(username, productDTO, amount);
    }

    @Override
    public Response<Boolean> removeProductsFromStore(String username, int storeID, int productID, int amount) {
        return CommerceSystem.getInstance().removeProductsFromStore(username, storeID, productID, amount);
    }

    @Override
    public Response<Boolean> updateProductInfo(String username, int storeID, int productID, double newPrice, String newName) {
        return commerceSystem.updateProductInfo(username, storeID, productID, newPrice, newName);
    }

    @Override
    public Response<List<PurchasePolicy>> getPurchasePolicy(String username, int storeID) {
        return commerceSystem.getPurchasePolicy(username, storeID);
    }

    @Override
    public Response<List<DiscountPolicy>> getDiscountPolicy(String username, int storeID) {
        return commerceSystem.getDiscountPolicy(username, storeID);
    }

    @Override
    public Response<Boolean> appointStoreOwner(String appointerName, String appointeeName, int storeID) {
        return commerceSystem.appointStoreOwner(appointerName, appointeeName, storeID);
    }

    @Override
    public Response<Boolean> removeOwnerAppointment(String appointerName, String appointeeName, int storeID) {
        return commerceSystem.removeOwnerAppointment(appointerName, appointeeName, storeID);
    }

    @Override
    public Response<Boolean> appointStoreManager(String appointerName, String appointeeName, int storeID) {
        return commerceSystem.appointStoreManager(appointerName, appointeeName, storeID);
    }

    @Override
    public Response<Boolean> addPermission(String permitting, int storeId, String permitted, Permissions permission) {
        return commerceSystem.addPermission(permitting, storeId, permitted, permission);
    }

    @Override
    public Response<Boolean> removePermission(String permitting, int storeId, String permitted, Permissions permission) {
        return commerceSystem.removePermission(permitting, storeId, permitted, permission);
    }

    @Override
    public Response<Boolean> removeManagerAppointment(String appointerName, String appointeeName, int storeID) {
        return commerceSystem.removeManagerAppointment(appointerName, appointeeName, storeID);
    }

    @Override
    public Response<List<Permissions>> getUserPermissions(String username, int storeID){
        return commerceSystem.getUserPermissions(username, storeID);
    }

    @Override
    public Response<List<User>> getStoreWorkersDetails(String username, int storeID) {
        return commerceSystem.getStoreWorkersDetails(username, storeID);
    }

    @Override
    public Response<Collection<PurchaseDTO>> getPurchaseDetails(String username, int storeID) {
        return commerceSystem.getPurchaseDetails(username, storeID);
    }

    @Override
    public Response<List<PurchaseDTO>> getUserPurchaseHistory(String adminName, String username) {
        return commerceSystem.getUserPurchaseHistory(adminName, username);
    }

    @Override
    public Response<Collection<PurchaseDTO>> getStorePurchaseHistory(String adminName, int storeID) {
        return commerceSystem.getStorePurchaseHistory(adminName, storeID);
    }
}