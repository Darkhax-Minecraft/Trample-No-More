package net.darkhax.tramplenomore.common.mixin;

import net.darkhax.tramplenomore.common.impl.TrampleNoMore;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmlandBlock.class)
public class MixinFarmBlock extends Block {

    private MixinFarmBlock() {
        super(null);
    }

    @Inject(method = "fallOn(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;D)V", at = @At("HEAD"), cancellable = true)
    private void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance, CallbackInfo ci) {
        if (level instanceof ServerLevel sLevel && entity != null && TrampleNoMore.preventTrampling(entity, pos, state)) {
            super.fallOn(level, state, pos, entity, fallDistance);
            ci.cancel();
        }
    }
}