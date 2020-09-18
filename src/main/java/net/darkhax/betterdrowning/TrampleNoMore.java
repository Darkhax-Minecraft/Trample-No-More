package net.darkhax.betterdrowning;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.FarmlandTrampleEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("tramplenomore")
public class TrampleNoMore {
    
    public static final INamedTag<Item> SOFT_BOOTS = ItemTags.makeWrapperTag("tramplenomore:soft_boots");
    
    public TrampleNoMore() {
        
        MinecraftForge.EVENT_BUS.addListener(this::onFarmlandTrampled);
    }
    
    private void onFarmlandTrampled (FarmlandTrampleEvent event) {
        
        final Entity entity = event.getEntity();
        
        if (entity instanceof LivingEntity) {
            
            final ItemStack boot = ((LivingEntity) entity).getItemStackFromSlot(EquipmentSlotType.FEET);
            
            // Prevent soft boots from trampling.
            if (SOFT_BOOTS.contains(boot.getItem())) {
                
                event.setCanceled(true);
            }
            
            // Prevent feather falling from trampling.
            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FEATHER_FALLING, boot) > 0) {
                
                event.setCanceled(true);
            }
            
            // Prevent creative mode players from trampling.
            if (entity instanceof PlayerEntity && ((PlayerEntity) entity).isCreative()) {
                
                event.setCanceled(true);
            }
        }
    }
}