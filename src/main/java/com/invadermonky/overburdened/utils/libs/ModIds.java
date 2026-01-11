package com.invadermonky.overburdened.utils.libs;

import com.invadermonky.overburdened.utils.helpers.ModHelper;
import org.jetbrains.annotations.Nullable;

public enum ModIds {
    actually_additions(ConstIds.actually_additions),
    baubles(ConstIds.baubles),
    hbm_nuclear(ConstIds.hbm_nuclear),
    immersive_engineering(ConstIds.immersive_engineering),
    travelers_backpack(ConstIds.travelers_backpack)
    ;

    public final String modId;
    public final String version;
    public final boolean isLoaded;

    ModIds(String modId, @Nullable String version, boolean isMinVersion, boolean isMaxVersion) {
        this.modId = modId;
        this.version = version;
        this.isLoaded = ModHelper.isModLoaded(modId, version, isMinVersion, isMaxVersion);
    }

    ModIds(String modId, @Nullable String version) {
        this.modId = modId;
        this.version = version;
        this.isLoaded = ModHelper.isModLoaded(modId, version);
    }

    ModIds(String modId) {
        this(modId, null);
    }

    @Override
    public String toString() {
        return this.modId;
    }

    public static class ConstIds {
        public static final String actually_additions = "actuallyadditions";
        public static final String baubles = "baubles";
        public static final String hbm_nuclear = "hbm";
        public static final String immersive_engineering = "immersiveengineering";
        public static final String travelers_backpack = "travelersbackpack";
    }
}
