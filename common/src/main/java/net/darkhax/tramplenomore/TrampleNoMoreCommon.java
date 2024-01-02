package net.darkhax.tramplenomore;

import com.google.common.collect.Iterables;
import net.darkhax.bookshelf.api.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;

import java.io.File;

public class TrampleNoMoreCommon {

    private static final TagKey<Item> SOFT_BOOTS = TagKey.create(Registries.ITEM, new ResourceLocation(Constants.MOD_ID, "soft_boots"));
    private static final TagKey<EntityType<?>> CANT_TRAMPLE = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Constants.MOD_ID, "prevent_trampling"));

    private static final Config CONFIG = Config.load(new File(Services.PLATFORM.getConfigDirectory(), Constants.MOD_ID + ".json"));

    public static void init() {

        Constants.LOG.debug("Feather falling enchantment prevents trampling = {}", CONFIG.featherFalling);
        Constants.LOG.debug("Items in the soft_boots tag prevent trampling = {}", CONFIG.taggedItems);

        Services.EVENTS.addFarmlandTrampleListener(TrampleNoMoreCommon::onTrampleFarmland);
        Services.EVENTS.addRecipeSyncListener(TrampleNoMoreCommon::onRecipesUpdate);
    }

    private static boolean onTrampleFarmland(Entity trampler, BlockPos pos, BlockState state) {

        if (trampler instanceof LivingEntity living) {

            final ItemStack footwear = living.getItemBySlot(EquipmentSlot.FEET);

            if (!footwear.isEmpty()) {

                if (CONFIG.taggedItems && footwear.is(SOFT_BOOTS)) {

                    return true;
                }

                if (CONFIG.featherFalling && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FALL_PROTECTION, footwear) > 0) {

                    return true;
                }
            }

            if (CONFIG.creativeMode && living instanceof ServerPlayer serverPlayer && serverPlayer.isCreative()) {

                return true;
            }
        }

        if (CONFIG.taggedEntities && trampler.getType().is(CANT_TRAMPLE)) {

            return true;
        }

        return false;
    }

    private static void onRecipesUpdate(RecipeManager manager) {

        final Iterable<Holder<Item>> softBoots = BuiltInRegistries.ITEM.getTagOrEmpty(SOFT_BOOTS);
        Constants.LOG.debug("{} contains {} entries.", SOFT_BOOTS.location().toString(), Iterables.size(softBoots));

        softBoots.forEach(s -> {

            if (s instanceof Holder.Reference holder) {

                Constants.LOG.debug("{} contains {}.", SOFT_BOOTS.location().toString(), holder.key().location().toString());
            }
        });

        final Iterable<Holder<EntityType<?>>> preventedMobs = BuiltInRegistries.ENTITY_TYPE.getTagOrEmpty(CANT_TRAMPLE);
        Constants.LOG.debug("{} contains {} entries.", CANT_TRAMPLE.location().toString(), Iterables.size(preventedMobs));

        preventedMobs.forEach(s -> {

            if (s instanceof Holder.Reference holder) {

                Constants.LOG.debug("{} contains {}.", CANT_TRAMPLE.location().toString(), holder.key().location().toString());
            }
        });
    }
}