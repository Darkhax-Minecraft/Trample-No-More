package net.darkhax.tramplenomore.fabric.impl;

import net.darkhax.tramplenomore.common.impl.TrampleNoMore;
import net.fabricmc.api.ModInitializer;

public class FabricMod implements ModInitializer {

    @Override
    public void onInitialize() {
        TrampleNoMore.init();
    }
}