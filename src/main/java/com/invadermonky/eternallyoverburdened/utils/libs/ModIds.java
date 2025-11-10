package com.invadermonky.eternallyoverburdened.utils.libs;

import com.invadermonky.eternallyoverburdened.utils.helpers.ModHelper;
import org.jetbrains.annotations.Nullable;

public enum ModIds {
    baubles(ConstIds.baubles);

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
        public static final String baubles = "baubles";
    }
}
