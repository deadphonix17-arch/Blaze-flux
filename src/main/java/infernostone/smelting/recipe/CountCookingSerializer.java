package infernostone.smelting.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CookingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

/** Like vanilla's cooking serializer, but "result" may be {"item": "...", "count": N}. */
public class CountCookingSerializer<T extends AbstractCookingRecipe> implements RecipeSerializer<T> {
    public interface Factory<T extends AbstractCookingRecipe> {
        T create(Identifier id, String group, CookingRecipeCategory category, Ingredient input,
                 ItemStack output, float experience, int cookTime);
    }

    private final Factory<T> factory;
    private final int defaultCookTime;

    public CountCookingSerializer(Factory<T> factory, int defaultCookTime) {
        this.factory = factory;
        this.defaultCookTime = defaultCookTime;
    }

    @Override
    public T read(Identifier id, JsonObject json) {
        String group = JsonHelper.getString(json, "group", "");
        CookingRecipeCategory category = CookingRecipeCategory.CODEC.byId(
                JsonHelper.getString(json, "category", null), CookingRecipeCategory.MISC);
        JsonElement ing = JsonHelper.hasArray(json, "ingredient")
                ? JsonHelper.getArray(json, "ingredient")
                : JsonHelper.getObject(json, "ingredient");
        Ingredient input = Ingredient.fromJson(ing);

        ItemStack output;
        JsonElement result = json.get("result");
        if (result == null) throw new com.google.gson.JsonSyntaxException("Missing result for " + id);
        if (result.isJsonObject()) {
            JsonObject obj = result.getAsJsonObject();
            Item item = JsonHelper.getItem(obj, "item");
            output = new ItemStack(item, Math.max(1, JsonHelper.getInt(obj, "count", 1)));
        } else {
            output = new ItemStack(JsonHelper.asItem(result, "result"));
        }
        float xp = JsonHelper.getFloat(json, "experience", 0.0F);
        int time = JsonHelper.getInt(json, "cookingtime", defaultCookTime);
        return factory.create(id, group, category, input, output, xp, time);
    }

    @Override
    public T read(Identifier id, PacketByteBuf buf) {
        String group = buf.readString();
        CookingRecipeCategory category = buf.readEnumConstant(CookingRecipeCategory.class);
        Ingredient input = Ingredient.fromPacket(buf);
        ItemStack output = buf.readItemStack();
        float xp = buf.readFloat();
        int time = buf.readVarInt();
        return factory.create(id, group, category, input, output, xp, time);
    }

    @Override
    public void write(PacketByteBuf buf, T recipe) {
        buf.writeString(recipe.getGroup());
        buf.writeEnumConstant(recipe.getCategory());
        recipe.getIngredients().get(0).write(buf);
        buf.writeItemStack(recipe.getOutput(DynamicRegistryManager.EMPTY));
        buf.writeFloat(recipe.getExperience());
        buf.writeVarInt(recipe.getCookTime());
    }
}
