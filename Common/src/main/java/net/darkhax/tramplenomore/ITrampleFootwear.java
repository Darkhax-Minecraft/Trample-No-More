package net.darkhax.tramplenomore;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface ITrampleFootwear {

    /**
     * Called when an entity is about to trample farmland.
     * @param stack The ItemStack of the footwear being worn.
     * @param trampler The entity trampling the farmland.
     * @param pos The position of the farmland being trampled.
     * @param state The state of the farmland being trampled.
     * @return returns weather the wearer should be able to cancel the trampling.
     */
    boolean cancelTrample(ItemStack stack, LivingEntity trampler, BlockPos pos, BlockState state);

    /**
     * A helper method to check if an item is a trampling footwear and if it should cancel the trampling.
     * @return returns weather the trample should be canceled.
     */
    static boolean cancelsTrampling(ItemStack stack, LivingEntity trampler, BlockPos pos, BlockState state) {

        return stack.getItem() instanceof ITrampleFootwear trampleItem && trampleItem.cancelTrample(stack, trampler, pos, state);
    }
}
