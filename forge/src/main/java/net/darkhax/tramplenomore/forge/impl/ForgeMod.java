package net.darkhax.tramplenomore.forge.impl;

import net.darkhax.tramplenomore.common.impl.TrampleNoMore;
import net.minecraftforge.fml.common.Mod;

@Mod(TrampleNoMore.MOD_ID)
public class ForgeMod {

    public ForgeMod() {
        TrampleNoMore.init();
    }
}