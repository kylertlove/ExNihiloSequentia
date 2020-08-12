package com.novamachina.exnihilosequentia.common.item.ore;

import com.novamachina.exnihilosequentia.common.utility.Color;
import com.novamachina.exnihilosequentia.common.utility.Constants;

public enum EnumModdedOre implements IOre {
    COPPER(Constants.Ore.COPPER, new Color("FF9933"),true),
    LEAD(Constants.Ore.LEAD, new Color("330066"), true),
    NICKEL(Constants.Ore.NICKEL, new Color("FFFFCC"), true),
    SILVER(Constants.Ore.SILVER, new Color("C2FAFF"), true),
    TIN(Constants.Ore.TIN, new Color("F5FEFF"), true),
    ALUMINUM(Constants.Ore.ALUMINUM, new Color("BFBFBF"), true),
    PLATINUM(Constants.Ore.PLATINUM, new Color("00FFF7"), true);

    private final String name;
    private final Color color;
    private final boolean isEnabled;


    EnumModdedOre(String name, Color color, boolean isEnabled) {
        this.name = name;
        this.color = color;
        this.isEnabled = isEnabled;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Color getColor() {
        return color;
    }

    public String getChunkName() {
        return "chunk_" + name;
    }

    public String getPieceName() {
        return "piece_" + name;
    }

    @Override
    public String getIngotName() {
        return "ingot_" + name;
    }
}