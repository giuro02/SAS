package catering.businesslogic.recipe;

import java.util.ArrayList;

public class KitchenJobManager {

    public KitchenJobManager() {
        Recipe.loadAllRecipes();
        Preparation.loadAllPreparations();
    }

    public ArrayList<Recipe> getRecipes() {
        return Recipe.getAllRecipes();
    }

    public ArrayList<Preparation> getPreparations(){
        return Preparation.getAllPreparations();
    }
}
