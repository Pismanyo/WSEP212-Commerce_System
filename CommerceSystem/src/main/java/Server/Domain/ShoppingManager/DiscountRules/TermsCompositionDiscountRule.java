package Server.Domain.ShoppingManager.DiscountRules;

import Server.DAL.DiscountRuleDTOs.DiscountRuleDTO;
import Server.DAL.DiscountRuleDTOs.SumCompositionDiscountRuleDTO;
import Server.DAL.DiscountRuleDTOs.TermsCompositionDiscountRuleDTO;
import Server.DAL.PredicateDTOs.PredicateDTO;
import Server.Domain.ShoppingManager.Predicates.Predicate;
import Server.Domain.ShoppingManager.ProductDTO;

import java.util.List;
import java.util.Map;
import java.util.Vector;

public class TermsCompositionDiscountRule extends CompoundDiscountRule {
    private String category;
    private List<Predicate> predicates;

    public TermsCompositionDiscountRule(double discount, String category, List<Predicate> predicates) {
        super(discount, null);
        this.category = category;
        this.predicates = predicates;
    }

    public TermsCompositionDiscountRule(TermsCompositionDiscountRuleDTO ruleDTO){
        super(ruleDTO.getDiscount(), ruleDTO.getConcreteDiscountRules());
        this.setID(ruleDTO.getId());
        this.category = ruleDTO.getCategory();

        this.predicates = new Vector<>();
        List<PredicateDTO> predicateDTOS = ruleDTO.getPredicates();
        if(predicateDTOS != null){
            for(PredicateDTO predicateDTO : predicateDTOS){
                this.predicates.add(predicateDTO.toConcretePredicate());
            }
        }
    }

    @Override
    public DiscountRuleDTO toDTO(){
        List<PredicateDTO> predicateDTOS = new Vector<>();
        for(Predicate predicate : this.predicates){
            predicateDTOS.add(predicate.toDTO());
        }

        return new TermsCompositionDiscountRuleDTO(this.id, this.getDiscountRulesDTO(), this.discount, this.category, predicateDTOS);
    }

    @Override
    public double calcDiscount(Map<ProductDTO, Integer> shoppingBasket) {
        double totalPriceToDiscount = 0.0;
        for(Predicate predicate : predicates)
            if(!predicate.isValid(shoppingBasket))
                return 0;

       for(Map.Entry<ProductDTO, Integer> entry : shoppingBasket.entrySet())
           if(entry.getKey().getCategories().contains(category))
               totalPriceToDiscount += entry.getKey().getPrice() * entry.getValue();

      return totalPriceToDiscount * (discount / 100);
    }

    @Override
    public String getDescription() {
        return "Terms Composition: " + id;
    }
}
