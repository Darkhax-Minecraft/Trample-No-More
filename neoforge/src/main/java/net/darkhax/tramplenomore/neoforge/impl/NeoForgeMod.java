package net.darkhax.tramplenomore.neoforge.impl;

import net.darkhax.tramplenomore.common.impl.TrampleNoMore;
import net.neoforged.fml.common.Mod;

@Mod(TrampleNoMore.MOD_ID)
public class NeoForgeMod {

    public NeoForgeMod() {
        TrampleNoMore.init();
    }
}