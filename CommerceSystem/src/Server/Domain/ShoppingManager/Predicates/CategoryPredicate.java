package Server.Domain.ShoppingManager.Predicates;

import Server.Domain.ShoppingManager.ProductDTO;

import java.util.Map;

public class CategoryPredicate implements Predicate {
    private String category;
    private int minUnits;
    private int maxUnits;

    public CategoryPredicate(String category, int minUnits, int maxUnits) {
        this.category = category;
        this.minUnits = minUnits;
        this.maxUnits = maxUnits;
    }

    @Override
    public boolean isValid(Map<ProductDTO, Integer> shoppingBasket) {
        int numOfProducts = 0;
        for (Map.Entry<ProductDTO, Integer> entry : shoppingBasket.entrySet())
            if (entry.getKey().getCategories().contains(category))
                numOfProducts += entry.getValue();

        return numOfProducts >= minUnits && numOfProducts <= maxUnits;
    }
}