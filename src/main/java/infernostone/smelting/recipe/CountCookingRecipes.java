package infernostone.smelting.recipe;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class CountCookingRecipes {
    public static final RecipeSerializer<CountSmeltingRecipe> SMELTING_SERIALIZER =
            new CountCookingSerializer<>(CountSmeltingRecipe::new, 200);
    public static final RecipeSerializer<CountBlastingRecipe> BLASTING_SERIALIZER =
            new CountCookingSerializer<>(CountBlastingRecipe::new, 100);

    private CountCookingRecipes() {}

    public static void register() {
        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier("overgeared_smelting", "count_smelting"), SMELTING_SERIALIZER);
        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier("overgeared_smelting", "count_blasting"), BLASTING_SERIALIZER);
    }
}
