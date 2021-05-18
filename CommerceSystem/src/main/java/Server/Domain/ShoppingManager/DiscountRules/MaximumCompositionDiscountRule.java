package Server.Domain.ShoppingManager.DiscountRules;

import Server.Domain.ShoppingManager.DTOs.ProductClientDTO;

import java.util.List;
import java.util.Map;

public class MaximumCompositionDiscountRule extends CompoundDiscountRule {

    public MaximumCompositionDiscountRule(List<DiscountRule> discountRules){
        super(0, discountRules);
    }

    @Override
    public double calcDiscount(Map<ProductClientDTO, Integer> shoppingBasket) {
        double discount;
        double maxDiscount = 0.0;

        for (DiscountRule policyRule : discountRules) {
            discount = policyRule.calcDiscount(shoppingBasket);
            if (discount > maxDiscount)
                maxDiscount = discount;
        }
        return maxDiscount;
    }

    @Override
    public String getDescription() {
        return "Maximum Composition: " + id;
    }
}
