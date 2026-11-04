package infernostone.smelting.mixin;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Vanilla furnaces only ever add 1 to the output slot when it already holds items, and don't check that a
 * multi-item output fits. These two injections make recipes with output count > 1 work properly.
 */
@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin {

    // Output slot already had items: vanilla added 1, add the remaining (count - 1).
    @Inject(method = "craftRecipe",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;increment(I)V", shift = At.Shift.AFTER))
    private static void overgeared$addExtraOutput(DynamicRegistryManager registryManager, @Nullable Recipe<?> recipe,
                                                  DefaultedList<ItemStack> slots, int count,
                                                  CallbackInfoReturnable<Boolean> cir) {
        if (recipe == null) return;
        int extra = recipe.getOutput(registryManager).getCount() - 1;
        if (extra > 0) slots.get(2).increment(extra);
    }

    // Refuse to smelt if the whole output wouldn't fit in the output slot.
    @Inject(method = "canAcceptRecipeOutput", at = @At("RETURN"), cancellable = true)
    private static void overgeared$requireRoomForAll(DynamicRegistryManager registryManager, @Nullable Recipe<?> recipe,
                                                     DefaultedList<ItemStack> slots, int count,
                                                     CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() || recipe == null) return;
        ItemStack out = recipe.getOutput(registryManager);
        ItemStack slot = slots.get(2);
        if (out.getCount() > 1 && !slot.isEmpty() && slot.getCount() + out.getCount() > slot.getMaxCount()) {
            cir.setReturnValue(false);
        }
    }
}
