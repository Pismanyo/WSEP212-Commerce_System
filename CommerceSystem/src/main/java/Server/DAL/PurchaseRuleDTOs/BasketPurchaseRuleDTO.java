package Server.DAL.PurchaseRuleDTOs;

import Server.DAL.PredicateDTOs.PredicateDTO;
import Server.Domain.ShoppingManager.PurchaseRules.BasketPurchaseRule;
import Server.Domain.ShoppingManager.PurchaseRules.PurchaseRule;
import dev.morphia.annotations.Embedded;

@Embedded
public class BasketPurchaseRuleDTO extends LeafPurchaseRuleDTO{

    public BasketPurchaseRuleDTO() {
        super();
        // For Morphia
    }

    public BasketPurchaseRuleDTO(int id, PredicateDTO predicate) {
        super(id, predicate);
    }


    @Override
    public PurchaseRule toConcretePurchaseRule() {
        return new BasketPurchaseRule(this);
    }
}
