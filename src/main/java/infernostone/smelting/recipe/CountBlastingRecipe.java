package infernostone.smelting.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.BlastingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CookingRecipeCategory;
import net.minecraft.util.Identifier;

/** A normal blast furnace (RecipeType.BLASTING) recipe whose output can be more than one item. */
public class CountBlastingRecipe extends BlastingRecipe {
    public CountBlastingRecipe(Identifier id, String group, CookingRecipeCategory category, Ingredient input,
                               ItemStack output, float experience, int cookTime) {
        super(id, group, category, input, output, experience, cookTime);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CountCookingRecipes.BLASTING_SERIALIZER;
    }
}
