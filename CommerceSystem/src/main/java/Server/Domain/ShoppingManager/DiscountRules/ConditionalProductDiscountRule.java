package Server.Domain.ShoppingManager.DiscountRules;

import Server.DAL.DiscountRuleDTOs.ConditionalCategoryDiscountRuleDTO;
import Server.DAL.DiscountRuleDTOs.ConditionalProductDiscountRuleDTO;
import Server.DAL.DiscountRuleDTOs.DiscountRuleDTO;
import Server.DAL.PredicateDTOs.CategoryPredicateDTO;
import Server.DAL.PredicateDTOs.ProductPredicateDTO;
import Server.Domain.ShoppingManager.Predicates.CategoryPredicate;
import Server.Domain.ShoppingManager.Predicates.ProductPredicate;
import Server.Domain.ShoppingManager.ProductDTO;

import java.util.Map;

public class ConditionalProductDiscountRule extends ProductDiscountRule {
    private ProductPredicate productPredicate;

    public ConditionalProductDiscountRule(int productID, double discount, ProductPredicate productPredicate) {
        super(productID, discount);
        this.productPredicate = productPredicate;
    }

    public ConditionalProductDiscountRule(ConditionalProductDiscountRuleDTO ruleDTO){
        super(ruleDTO.getProductID(), ruleDTO.getDiscount());
        this.setID(ruleDTO.getId());
        this.productPredicate = (ProductPredicate) ruleDTO.getProductPredicate().toConcretePredicate();
    }

    @Override
    public DiscountRuleDTO toDTO(){
        return new ConditionalProductDiscountRuleDTO(this.id, this.discount, this.productID, (ProductPredicateDTO) this.productPredicate.toDTO());
    }

    @Override
    public double calcDiscount(Map<ProductDTO, Integer> shoppingBasket) {
        for(Map.Entry<ProductDTO, Integer> entry : shoppingBasket.entrySet()) {
            if (entry.getKey().getProductID() == productID) {
                return productPredicate.isValid(shoppingBasket) ? (entry.getValue() * entry.getKey().getPrice()) * (discount / 100) : 0.0;
            }
        }
        return 0.0;
    }

    @Override
    public String getDescription() {
        return "Conditional Product discount: ProductID - " + productID + " with a discount of " + discount + "%";
    }
}
