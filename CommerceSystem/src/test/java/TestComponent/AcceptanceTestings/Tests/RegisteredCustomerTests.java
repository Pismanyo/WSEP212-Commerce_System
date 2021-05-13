package TestComponent.AcceptanceTestings.Tests;

import Server.Domain.CommonClasses.Response;
import Server.Domain.ShoppingManager.ProductDTO;
import Server.Domain.ShoppingManager.Review;
import Server.Domain.ShoppingManager.Store;
import Server.Domain.ShoppingManager.StoreDTO;
import Server.Domain.UserManager.ExternalSystemsAdapters.PaymentDetails;
import Server.Domain.UserManager.ExternalSystemsAdapters.SupplyDetails;
import Server.Domain.UserManager.PurchaseDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class RegisteredCustomerTests extends ProjectAcceptanceTests{

    private static boolean initialized = false;

    @Before
    public void setUp(){
        if(!initialized) {
            super.setUp();

            String guestName = bridge.addGuest().getResult();
            bridge.register(guestName, "aviad", "123456");
            bridge.register(guestName, "shalom", "123456");
            bridge.register(guestName, "tzemah", "123456");

            bridge.login(guestName, "aviad", "123456");
            int storeID = bridge.openStore("aviad", "hacol la sefer").getResult();

            ProductDTO product = new ProductDTO("simania zoheret", storeID, 20,
                    new LinkedList<String>(Arrays.asList("bookmark")),
                    new LinkedList<String>(Arrays.asList("simania")));

            bridge.addProductsToStore("aviad", product, 20);

            product = new ProductDTO("mavrik sfarim", storeID, 30,
                    new LinkedList<String>(Arrays.asList("polish")),
                    new LinkedList<String>(Arrays.asList("mavrik")));

            bridge.addProductsToStore("aviad", product, 100);

            product = new ProductDTO("martiv sfarim", storeID, 30,
                    new LinkedList<String>(Arrays.asList("wet")),
                    new LinkedList<String>(Arrays.asList("martiv")));

            bridge.addProductsToStore("aviad", product, 100);

            bridge.appointStoreOwner("aviad", "shalom", storeID);
            bridge.appointStoreOwner("aviad", "tzemah", storeID);

            initialized = true;
        }
    }

    @Test
    public void logoutTestSuccess(){ // 3.1 good
        // logging in to an existing user.
        String guestName = bridge.addGuest().getResult();
        bridge.login(guestName, "shalom", "123456");

        // logging out
        Response<String> logoutResponse = bridge.logout("shalom");
        Assert.assertFalse(logoutResponse.isFailure());
        Assert.assertTrue(logoutResponse.getResult().contains("Guest")); // checking if the user became a guest

        // trying to log out from an already logged out user
        logoutResponse = bridge.logout("shalom");
        Assert.assertTrue(logoutResponse.isFailure());
    }

    @Test
    public void guestLoggingOutTest(){ // 3.1 bad
        String guestName = bridge.addGuest().getResult();
        // logging out
        Response<String> logoutResponse = bridge.logout(guestName);
        Assert.assertTrue(logoutResponse.isFailure());
    }

    @Test
    public void LoginDisplayStoredNotificationTest(){ // 9.1 good (2.4)
        PaymentDetails paymentDetails = new PaymentDetails("2222333344445555", "4", "2021", "Israel Israelovice", "262", "20444444");
        SupplyDetails supplyDetails = new SupplyDetails("Israel Israelovice", "Rager Blvd 12", "Beer Sheva", "Israel", "8458527");

        bridge.login(bridge.addGuest().getResult(), "shalom", "123456");

        // store owner subscribed to receive notifications, not logged in
        notifier.addConnection("tzemah", null);

        // logged in user adding a product to his cart and buying it
        Response<List<ProductDTO>> searchResult = bridge.searchByProductName("simania zoheret");
        ProductDTO productDTO = searchResult.getResult().get(0);
        bridge.addToCart("shalom", productDTO.getStoreID(), productDTO.getProductID());
        bridge.directPurchase("shalom", paymentDetails, supplyDetails);

        // now he reviews it
        Response<Boolean> reviewResult = bridge.addProductReview("shalom", productDTO.getStoreID(),
                productDTO.getProductID(), "best simania i ever bought! solid 5/7");
        Assert.assertTrue(reviewResult.getResult());

        // checking if the review was added
        searchResult = bridge.searchByProductName("simania zoheret");
        productDTO = searchResult.getResult().get(0);

        boolean added = false;
        for(Review review: productDTO.getReviews()){
            if(review.getReview().equals("best simania i ever bought! solid 5/7")){
                added = true;
                break;
            }
        }

        Assert.assertTrue(added);

        // log in owner
        bridge.login(bridge.addGuest().getResult(), "tzemah", "123456");

        //checking the store owner received notifications (one for purchase, one for review) upon login
        Assert.assertEquals(2, notifier.getMessages("tzemah").size());
    }

    @Test
    public void createStoreTestSuccess(){ // 3.2 good
        // opening a store from a logged in user
        Response<Integer> openStoreResponse = bridge.openStore("aviad", "hacol la even");
        Assert.assertFalse(openStoreResponse.isFailure());

        // looking the store up on the search just to be sure
        Response<List<StoreDTO>> searchResult = bridge.searchByStoreName("hacol la even");
        boolean exists = true;
        for(StoreDTO store: searchResult.getResult()){
            if(!store.getStoreName().contains("hacol la even")){
                exists = false;
            }
        }

        Assert.assertTrue(exists);
    }

    @Test
    public void notRegisteredUserCreatesStore() { // 3.2 good
        // trying to open a store from a user which is a guest. should fail
        String guestName = bridge.addGuest().getResult();
        Response<Integer> openStoreResponse = bridge.openStore(guestName, "bug");
        Assert.assertTrue(openStoreResponse.isFailure());

        // making sure we can't find it
        Response<List<StoreDTO>> searchResult = bridge.searchByStoreName("bug");
        boolean exists = false;
        for(StoreDTO store: searchResult.getResult()){
            if(store.getStoreName().contains("bug")){
                exists = true;
            }
        }

        Assert.assertFalse(exists);
    }

    @Test
    public void reviewProductSuccess(){ // 3.3 good
        PaymentDetails paymentDetails = new PaymentDetails("2222333344445555", "4", "2021", "Israel Israelovice", "262", "20444444");
        SupplyDetails supplyDetails = new SupplyDetails("Israel Israelovice", "Rager Blvd 12", "Beer Sheva", "Israel", "8458527");

        bridge.login(bridge.addGuest().getResult(), "shalom", "123456");
        // logged in user adding a product to his cart and buying it
        Response<List<ProductDTO>> searchResult = bridge.searchByProductName("simania zoheret");
        ProductDTO productDTO = searchResult.getResult().get(0);
        bridge.addToCart("shalom", productDTO.getStoreID(), productDTO.getProductID());
        bridge.directPurchase("shalom", paymentDetails, supplyDetails);

        // now he reviews it
        Response<Boolean> reviewResult = bridge.addProductReview("shalom", productDTO.getStoreID(),
                productDTO.getProductID(), "best simania i ever bought! solid 5/7");
        Assert.assertTrue(reviewResult.getResult());

        // checking if the review was added
        searchResult = bridge.searchByProductName("simania zoheret");
        productDTO = searchResult.getResult().get(0);

        boolean added = false;
        for(Review review: productDTO.getReviews()){
            if(review.getReview().equals("best simania i ever bought! solid 5/7")){
                added = true;
                break;
            }
        }

        Assert.assertTrue(added);
    }

    @Test
    public void reviewProductNotBoughtPreviously() { // 3.3 bad
        bridge.login(bridge.addGuest().getResult(), "shalom", "123456");
        // the user is trying to review a product he didn't buy. should fail
        Response<List<ProductDTO>> searchResult = bridge.searchByProductName("martiv sfarim");
        ProductDTO productDTO = searchResult.getResult().get(0);
        Response<Boolean> reviewResult = bridge.addProductReview("shalom", productDTO.getStoreID(),
                productDTO.getProductID(), "meh martiv! 3/10");
        Assert.assertFalse(reviewResult.getResult());

        bridge.logout("shalom");
    }

    @Test
    public void guestReviewingAProduct(){ // 3.3 bad
        // logging out and trying to review a product. should fail.
        Response<List<ProductDTO>> searchResult = bridge.searchByProductName("martiv sfarim");
        ProductDTO productDTO = searchResult.getResult().get(0);
        String guestName = bridge.addGuest().getResult();
        Response<Boolean> reviewResult = bridge.addProductReview(guestName, productDTO.getStoreID(),
                productDTO.getProductID(), "meh mavriv! 3/10");
        Assert.assertFalse(reviewResult.getResult());
    }

    @Test
    public void emptyReviewTest(){ // 3.3 bad
        bridge.login(bridge.addGuest().getResult(), "shalom", "123456");
        // the user is trying to review a product he didn't buy. should fail
        Response<List<ProductDTO>> searchResult = bridge.searchByProductName("martiv sfarim");
        ProductDTO productDTO = searchResult.getResult().get(0);
        Response<Boolean> reviewResult = bridge.addProductReview("shalom", productDTO.getStoreID(),
                productDTO.getProductID(), "");
        Assert.assertFalse(reviewResult.getResult());

        bridge.logout("shalom");
    }

    @Test
    public void getPurchaseHistorySuccess(){ // 3.7 good
        PaymentDetails paymentDetails = new PaymentDetails("2222333344445555", "4", "2021", "Israel Israelovice", "262", "20444444");
        SupplyDetails supplyDetails = new SupplyDetails("Israel Israelovice", "Rager Blvd 12", "Beer Sheva", "Israel", "8458527");

        // logging in and buying a product
        String guestName = bridge.addGuest().getResult();
        bridge.login(guestName, "shalom", "123456");
        Response<List<ProductDTO>> searchResult = bridge.searchByProductName("mavrik sfarim");
        ProductDTO productDTO = searchResult.getResult().get(0);
        bridge.addToCart("shalom", productDTO.getStoreID(), productDTO.getProductID());
        bridge.directPurchase("shalom", paymentDetails, supplyDetails);

        // looking the purchase in the purchase history
        Response<List<PurchaseDTO>> historyResult = bridge.getPurchaseHistory("shalom");
        Assert.assertFalse(historyResult.isFailure());

        boolean exists = false;
        for(PurchaseDTO purchased: historyResult.getResult()){
            for(ProductDTO product: purchased.getBasket().keySet()){
                if(product.getProductID() == productDTO.getProductID() && product.getStoreID() == productDTO.getStoreID()) {
                    exists = true;
                }
            }
        }

        Assert.assertTrue(exists);

        // logging out and trying to view the purchase history as a guest. should fail
        bridge.logout("shalom").getResult();
    }

    @Test
    public void guestGetPurchaseHistory(){ // 3.7 bad
        String guestName = bridge.addGuest().getResult();
        Response<List<PurchaseDTO>> historyResult = bridge.getPurchaseHistory(guestName);
        Assert.assertTrue(historyResult.isFailure());
    }

    @Test
    public void emptyPurchaseHistory(){ // 3.7 bad
        bridge.login(bridge.addGuest().getResult(), "tzemah", "123456");
        Response<List<PurchaseDTO>> historyResult = bridge.getPurchaseHistory("tzemah");
        Assert.assertTrue(historyResult.getResult().isEmpty());
    }

    @Test
    public void reviewProductSuccessNotificationTest(){ // 9.1 good (3.3)
        PaymentDetails paymentDetails = new PaymentDetails("2222333344445555", "4", "2021", "Israel Israelovice", "262", "20444444");
        SupplyDetails supplyDetails = new SupplyDetails("Israel Israelovice", "Rager Blvd 12", "Beer Sheva", "Israel", "8458527");

        bridge.login(bridge.addGuest().getResult(), "shalom", "123456");
        bridge.login(bridge.addGuest().getResult(), "tzemah", "123456");

        notifier.addConnection("aviad", null);
        notifier.addConnection("shalom", null);
        notifier.addConnection("tzemah", null);

        // logged in user adding a product to his cart and buying it
        Response<List<ProductDTO>> searchResult = bridge.searchByProductName("simania zoheret");
        ProductDTO productDTO = searchResult.getResult().get(0);
        bridge.addToCart("shalom", productDTO.getStoreID(), productDTO.getProductID());
        bridge.directPurchase("shalom", paymentDetails, supplyDetails);

        // now he reviews it
        Response<Boolean> reviewResult = bridge.addProductReview("shalom", productDTO.getStoreID(),
                productDTO.getProductID(), "best simania i ever bought! solid 5/7");
        Assert.assertTrue(reviewResult.getResult());

        // checking if the review was added
        searchResult = bridge.searchByProductName("simania zoheret");
        productDTO = searchResult.getResult().get(0);

        boolean added = false;
        for(Review review: productDTO.getReviews()){
            if(review.getReview().equals("best simania i ever bought! solid 5/7")){
                added = true;
                break;
            }
        }

        Assert.assertTrue(added);

        //checking the store owners received notifications (one for purchase, one for review)
        Assert.assertEquals(2, notifier.getMessages("aviad").size());
        Assert.assertEquals(2, notifier.getMessages("shalom").size());
        Assert.assertEquals(2, notifier.getMessages("tzemah").size());
    }

    @Test
    public void reviewProductFailureNotificationTest() { // 9.1 bad (3.3)
        notifier.addConnection("aviad", null);
        bridge.login(bridge.addGuest().getResult(), "shalom", "123456");
        // the user is trying to review a product he didn't buy. should fail
        Response<List<ProductDTO>> searchResult = bridge.searchByProductName("martiv sfarim");
        ProductDTO productDTO = searchResult.getResult().get(0);
        Response<Boolean> reviewResult = bridge.addProductReview("shalom", productDTO.getStoreID(),
                productDTO.getProductID(), "meh martiv! 3/10");
        Assert.assertFalse(reviewResult.getResult());

        bridge.logout("shalom");

        //checking the store owners received no notifications
        Assert.assertEquals(0, notifier.getMessages("aviad").size());
    }

    @Test
    public void reviewProductSuccessStoredNotificationTest(){ // 9.1 good (3.3)
        PaymentDetails paymentDetails = new PaymentDetails("2222333344445555", "4", "2021", "Israel Israelovice", "262", "20444444");
        SupplyDetails supplyDetails = new SupplyDetails("Israel Israelovice", "Rager Blvd 12", "Beer Sheva", "Israel", "8458527");

        bridge.login(bridge.addGuest().getResult(), "shalom", "123456");

        notifier.addConnection("aviad", null);
        notifier.addConnection("shalom", null);
        notifier.addConnection("tzemah", null);

        // logged in user adding a product to his cart and buying it
        Response<List<ProductDTO>> searchResult = bridge.searchByProductName("simania zoheret");
        ProductDTO productDTO = searchResult.getResult().get(0);
        bridge.addToCart("shalom", productDTO.getStoreID(), productDTO.getProductID());
        bridge.directPurchase("shalom", paymentDetails, supplyDetails);

        // now he reviews it
        Response<Boolean> reviewResult = bridge.addProductReview("shalom", productDTO.getStoreID(),
                productDTO.getProductID(), "best simania i ever bought! solid 5/7");
        Assert.assertTrue(reviewResult.getResult());

        // checking if the review was added
        searchResult = bridge.searchByProductName("simania zoheret");
        productDTO = searchResult.getResult().get(0);

        boolean added = false;
        for(Review review: productDTO.getReviews()){
            if(review.getReview().equals("best simania i ever bought! solid 5/7")){
                added = true;
                break;
            }
        }

        Assert.assertTrue(added);

        bridge.login(bridge.addGuest().getResult(), "tzemah", "123456");

        //checking the store owner received notifications (one for purchase, one for review)
        Assert.assertEquals(2, notifier.getMessages("tzemah").size());
    }

    @Test
    public void reviewProductFailureStoredNotificationTest() { // 9.1 bad (3.3)
        notifier.addConnection("aviad", null);
        bridge.logout("aviad");
        bridge.login(bridge.addGuest().getResult(), "shalom", "123456");
        // the user is trying to review a product he didn't buy. should fail
        Response<List<ProductDTO>> searchResult = bridge.searchByProductName("martiv sfarim");
        ProductDTO productDTO = searchResult.getResult().get(0);
        Response<Boolean> reviewResult = bridge.addProductReview("shalom", productDTO.getStoreID(),
                productDTO.getProductID(), "meh martiv! 3/10");
        Assert.assertFalse(reviewResult.getResult());

        bridge.logout("shalom");
        bridge.login(bridge.addGuest().getResult(), "aviad", "123456");

        //checking the store owners received no notifications
        Assert.assertEquals(0, notifier.getMessages("aviad").size());
    }
}