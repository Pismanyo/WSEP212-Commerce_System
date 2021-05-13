package Server.Domain.UserManager;

import Server.Domain.CommonClasses.Response;
import Server.Domain.ShoppingManager.StoreController;
import Server.Domain.UserManager.ExternalSystemsAdapters.PaymentDetails;
import Server.Domain.UserManager.ExternalSystemsAdapters.PaymentSystemAdapter;
import Server.Domain.UserManager.ExternalSystemsAdapters.ProductSupplyAdapter;
import Server.Domain.UserManager.ExternalSystemsAdapters.SupplyDetails;

import java.util.List;

public class PurchaseController {
        private StoreController storeController;
        private PaymentSystemAdapter paymentSystemAdapter;
        private ProductSupplyAdapter supplySystemAdapter;

        private PurchaseController() {
                this.storeController = StoreController.getInstance();
                this.paymentSystemAdapter = PaymentSystemAdapter.getInstance();
                this.supplySystemAdapter = ProductSupplyAdapter.getInstance();
        }

        private static class CreateSafeThreadSingleton {
                private static final PurchaseController INSTANCE = new PurchaseController();
        }

        public static PurchaseController getInstance() {
                return PurchaseController.CreateSafeThreadSingleton.INSTANCE;
        }

        public Response<List<PurchaseDTO>> handlePayment(ShoppingCart cart, PaymentDetails paymentDetails, SupplyDetails supplyDetails) {
                Response<List<PurchaseDTO>> res = storeController.purchase(cart);
                Response<Integer> paymentRes;
                Response<Integer> cancelRes;
                Response<Integer> supplyRes;

                if (res.isFailure())
                        return new Response<>(null, true, res.getErrMsg() + " | doesn't created external connection");

                paymentRes = paymentSystemAdapter.pay(paymentDetails);
                if(paymentRes.isFailure()) {
                        storeController.addProductsToInventories(cart);
                        return new Response<>(null, true, "Payment failed" + " | created external connection");
                }

                supplyRes = supplySystemAdapter.supply(supplyDetails);
                if(supplyRes.isFailure()) {
                        storeController.addProductsToInventories(cart);

                        cancelRes = paymentSystemAdapter.cancelPay(paymentRes.getResult() + "");
                        if(cancelRes.isFailure())
                                return  new Response<>(null, true, "Delivery failed and you have been charged but the payment cancellation failed | created external connection");

                        return new Response<>(null, true, "Delivery failed" + " | created external connection");
                }

                return new Response<>(res.getResult(), false, "The purchase was successful" + " | created external connection");
        }
}