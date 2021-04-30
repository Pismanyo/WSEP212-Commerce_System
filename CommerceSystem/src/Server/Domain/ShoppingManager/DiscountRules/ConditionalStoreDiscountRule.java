package Server.Domain.ShoppingManager.DiscountRules;

import Server.Domain.ShoppingManager.Predicates.StorePredicate;
import Server.Domain.ShoppingManager.ProductDTO;

import java.util.Map;

public class ConditionalStoreDiscountRule extends StoreDiscountRule {
    private int productID;
    private StorePredicate storePredicate;

    public ConditionalStoreDiscountRule(int ruleID, double discount, StorePredicate storePredicate) {
        super(ruleID, discount);
        this.storePredicate = storePredicate;
        this.productID = -1;
    }

    public ConditionalStoreDiscountRule(int ruleID, double discount, StorePredicate storePredicate, int productID) {
        super(ruleID, discount);
        this.storePredicate = storePredicate;
        this.productID = productID;
    }

    @Override
    public double calcDiscount(Map<ProductDTO, Integer> shoppingBasket) {
        if(storePredicate.isValid(shoppingBasket)) {
            double totalPrice = 0.0;
            if(productID != -1) {
                for (Map.Entry<ProductDTO, Integer> entry : shoppingBasket.entrySet())
                    totalPrice += entry.getKey().getPrice() * entry.getValue();
            }
            else{
                for (Map.Entry<ProductDTO, Integer> entry : shoppingBasket.entrySet())
                    if(entry.getKey().getProductID() == productID) {
                        totalPrice += entry.getKey().getPrice() * entry.getValue();
                        break;
                    }
            }
            return totalPrice * (this.discount / 100);
        }
        else
            return 0.0;
    }

    @Override
    public String getDescription() {
        return "Conditional store discount: " + this.discount + "% discount on the entire store.";
    }
}