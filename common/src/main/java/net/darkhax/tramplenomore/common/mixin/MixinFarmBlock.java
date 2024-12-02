package net.darkhax.tramplenomore.common.mixin;

import net.darkhax.tramplenomore.common.impl.TrampleNoMore;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmBlock.class)
public class MixinFarmBlock extends Block {

    private MixinFarmBlock() {
        super(null);
    }

    @Inject(method = "fallOn(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;F)V", at = @At("HEAD"), cancellable = true)
    private void onPlayerTrample(Level level, BlockState block, BlockPos pos, Entity faller, float distance, CallbackInfo cbi) {
        if (!level.isClientSide && faller instanceof LivingEntity living && TrampleNoMore.preventTrampling(living, pos, block)) {
            super.fallOn(level, block, pos, faller, distance);
            cbi.cancel();
        }
    }
}