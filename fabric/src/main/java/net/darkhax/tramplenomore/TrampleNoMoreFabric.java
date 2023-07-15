package net.darkhax.tramplenomore;

import net.fabricmc.api.ModInitializer;

public class TrampleNoMoreFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        TrampleNoMoreCommon.init();
    }
}