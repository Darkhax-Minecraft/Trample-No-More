package net.darkhax.tramplenomore.common.impl;

import net.darkhax.pricklemc.common.api.config.ConfigManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrampleNoMore {

    public static final String MOD_ID = "tramplenomore";
    public static final String MOD_NAME = "TrampleNoMore";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
    public static final Config CONFIG = ConfigManager.load(MOD_ID, new Config());

    private static final TagKey<Item> SOFT_BOOTS = TagKey.create(Registries.ITEM, id("soft_boots"));
    private static final TagKey<EntityType<?>> CANT_TRAMPLE = TagKey.create(Registries.ENTITY_TYPE, id("prevent_trampling"));

    public static void init() {
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static boolean preventTrampling(Entity trampler, BlockPos pos, BlockState state) {
        if (trampler.is(CANT_TRAMPLE)) {
            return true;
        }
        if (CONFIG.creative_mode && trampler instanceof Player player && player.isCreative()) {
            return true;
        }
        if (trampler instanceof LivingEntity living) {
            final ItemStack footwear = living.getItemBySlot(EquipmentSlot.FEET);
            return footwear.is(SOFT_BOOTS) || (CONFIG.barefoot && footwear.isEmpty()) || (CONFIG.feather_falling > 0 && getLevel(trampler.level(), Enchantments.FEATHER_FALLING, footwear) >= CONFIG.feather_falling);
        }
        return false;
    }

    private static int getLevel(Level level, ResourceKey<Enchantment> enchantment, ItemStack stack) {
        return EnchantmentHelper.getItemEnchantmentLevel(level.holderLookup(Registries.ENCHANTMENT).getOrThrow(enchantment), stack);
    }
}