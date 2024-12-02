package net.darkhax.tramplenomore.common.impl;

import net.darkhax.pricklemc.common.api.annotations.RangedInt;
import net.darkhax.pricklemc.common.api.annotations.Value;

public class Config {

    @Value(comment = "Boots with the feather falling enchantment at a level greater than or equal to this value will not trample crops. Setting this to 0 will disable this feature.")
    @RangedInt(min = 0)
    public int feather_falling = 1;

    @Value(comment = "When enabled, players in creative mode can not trample crops.")
    public boolean creative_mode = true;

    @Value(comment = "When enabled, players with no footwear will not trample crops.")
    public boolean barefoot = false;
}
