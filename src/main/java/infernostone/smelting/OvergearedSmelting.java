package infernostone.smelting;

import infernostone.smelting.recipe.CountCookingRecipes;
import net.fabricmc.api.ModInitializer;

public class OvergearedSmelting implements ModInitializer {
    @Override
    public void onInitialize() {
        CountCookingRecipes.register();
    }
}
