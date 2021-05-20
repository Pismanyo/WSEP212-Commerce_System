package Server.DAL.PurchaseRuleDTOs;

import Server.DAL.PredicateDTOs.PredicateDTO;
import Server.Domain.ShoppingManager.PurchaseRules.ProductPurchaseRule;
import Server.Domain.ShoppingManager.PurchaseRules.PurchaseRule;
import dev.morphia.annotations.Embedded;

@Embedded
public class ProductPurchaseRuleDTO extends LeafPurchaseRuleDTO{

    public ProductPurchaseRuleDTO() {
        super();
        // For Morphia
    }

    public ProductPurchaseRuleDTO(int id, PredicateDTO predicate) {
        super(id, predicate);
    }

    @Override
    public PurchaseRule toConcretePurchaseRule() {
        return new ProductPurchaseRule(this);
    }
}
