package net.darkhax.tramplenomore;

import com.google.common.collect.Iterables;
import net.darkhax.bookshelf.api.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
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
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TrampleNoMoreCommon {

    private static final TagKey<Item> SOFT_BOOTS = Services.TAGS.itemTag(new ResourceLocation(Constants.MOD_ID, "soft_boots"));
    private static final TagKey<EntityType<?>> CANT_TRAMPLE = Services.TAGS.entityTag(new ResourceLocation(Constants.MOD_ID, "prevent_trampling"));
    private static final Map<Item, ITrampleFootwear> TRAMPLERS = new HashMap<>();

    private static final Config CONFIG = Config.load(new File(Services.PLATFORM.getConfigDirectory(), Constants.MOD_ID + ".json"));

    public static void init() {

        Constants.LOG.debug("Feather falling enchantment prevents trampling = {}", CONFIG.featherFalling);
        Constants.LOG.debug("Items in the soft_boots tag prevent trampling = {}", CONFIG.taggedItems);

        Services.EVENTS.addFarmlandTrampleListener(TrampleNoMoreCommon::onTrampleFarmland);
        Services.EVENTS.addRecipeSyncListener(TrampleNoMoreCommon::onRecipesUpdate);
    }

    /**
     * @param itemLike The item to register the trample footwear predicate to.
     * @param trampler The predicate to check if the item can trample farmland.
     * @return returns true if the item was registered, false if it was already registered.
     */
    public static boolean registerFootwear(ItemLike itemLike, ITrampleFootwear trampler) {
        Item item = itemLike.asItem();
        if (TRAMPLERS.containsKey(item)) {
            Constants.LOG.warn("Item {} is already registered as a trampling footwear.", Registry.ITEM.getKey(item));
            return false;
        }
        TRAMPLERS.put(item, trampler);
        return true;
    }

    private static boolean cancelsTrampling(ItemStack stack, LivingEntity trampler, BlockPos pos, BlockState state) {
        if (ITrampleFootwear.cancelsTrampling(stack, trampler, pos, state)) {

            return true;
        }
        if (TRAMPLERS.containsKey(stack.getItem())) {

            return TRAMPLERS.get(stack.getItem()).cancelTrample(stack, trampler, pos, state);
        }
        return false;
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

                if (cancelsTrampling(footwear, living, pos, state)) {

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

        final Iterable<Holder<Item>> softBoots = Registry.ITEM.getTagOrEmpty(SOFT_BOOTS);
        Constants.LOG.debug("{} contains {} entries.", SOFT_BOOTS.location().toString(), Iterables.size(softBoots));

        softBoots.forEach(s -> {

            if (s instanceof Holder.Reference holder) {

                Constants.LOG.debug("{} contains {}.", SOFT_BOOTS.location().toString(), holder.key().location().toString());
            }
        });

        final Iterable<Holder<EntityType<?>>> preventedMobs = Registry.ENTITY_TYPE.getTagOrEmpty(CANT_TRAMPLE);
        Constants.LOG.debug("{} contains {} entries.", CANT_TRAMPLE.location().toString(), Iterables.size(preventedMobs));

        preventedMobs.forEach(s -> {

            if (s instanceof Holder.Reference holder) {

                Constants.LOG.debug("{} contains {}.", CANT_TRAMPLE.location().toString(), holder.key().location().toString());
            }
        });
    }
}