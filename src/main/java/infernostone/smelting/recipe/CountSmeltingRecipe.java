package infernostone.smelting.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.recipe.book.CookingRecipeCategory;
import net.minecraft.util.Identifier;

/** A normal furnace (RecipeType.SMELTING) recipe whose output can be more than one item. */
public class CountSmeltingRecipe extends SmeltingRecipe {
    public CountSmeltingRecipe(Identifier id, String group, CookingRecipeCategory category, Ingredient input,
                               ItemStack output, float experience, int cookTime) {
        super(id, group, category, input, output, experience, cookTime);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CountCookingRecipes.SMELTING_SERIALIZER;
    }
}
