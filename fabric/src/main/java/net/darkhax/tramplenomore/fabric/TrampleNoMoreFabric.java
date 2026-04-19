package net.darkhax.tramplenomore.fabric;

import net.darkhax.tramplenomore.common.impl.TrampleNoMore;
import net.fabricmc.api.ModInitializer;

public class TrampleNoMoreFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        TrampleNoMore.init();
    }
}