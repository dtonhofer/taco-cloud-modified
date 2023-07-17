package tacos.model.ingredients;

import tacos.model.ingredients.IngredientRelation;

public interface IngredientsSource {

    // Create a new IngredientRelation with the latest data

    IngredientRelation refresh();

}
