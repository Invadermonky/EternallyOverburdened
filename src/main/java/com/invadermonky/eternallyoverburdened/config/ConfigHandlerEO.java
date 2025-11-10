package com.invadermonky.eternallyoverburdened.config;

import com.invadermonky.eternallyoverburdened.EternallyOverburdened;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = EternallyOverburdened.MOD_ID)
public class ConfigHandlerEO {
    public static ItemWeightsCategory weights = new ItemWeightsCategory();
    public static EnchantmentCategory enchantments = new EnchantmentCategory();
    public static PotionEffectsCategory potions = new PotionEffectsCategory();
    public static SettingsCategory settings = new SettingsCategory();

    public static class ItemWeightsCategory {
        @Config.RangeDouble(min = 0, max = 10000)
        @Config.Name("Default Fluid Weight")
        @Config.Comment("The default weight used for fluid weight calculations. This value is equal to 1 mb of fluid.")
        public double defaultFluidWeight = 0.001;

        @Config.RangeDouble(min = 0, max = 10000)
        @Config.Name("Default Item Weight")
        @Config.Comment("The default weight used for item weight calculations.")
        public double defaultItemWeight = 1;

        @Config.Name("Capability Blacklist")
        @Config.Comment
                ({
                        "A blacklist of items that should not calculate values of any internal storage. An example of",
                        "this would be inventory or fluid container items.",
                        "Format:",
                        "  modid:itemid",
                        "Examples:",
                        "  minecraft:water_bucket",
                        "  quark:backpack"
                })
        public String[] capabilityBlacklist = new String[] {};

        @Config.Name("Fluid Weights")
        @Config.Comment
                ({
                        "A list of fluids and their respective weights. Weights are per 1mb and can be positive or negative.",
                        "Negative fluid weights will decrease the weight of other items in the player's inventory.",
                        " Format:",
                        "  fluidname=weight",
                        " Examples:",
                        "  water=0.001",
                        "  lava=-0.005"
                })
        public String[] fluidWeights = new String[] {};

        @Config.Name("Item Weights")
        @Config.Comment
                ({
                        "A list of items and their respective weights.",
                        " Format - modId:itemId=weight",
                        "  modId:itemId - the item registry name",
                        "  weight - the weight of this item",
                        " Examples:",
                        "  minecraft:stick=0.1",
                        "  minecraft:feather=0.01"
                })
        public String[] itemWeights = new String[] {

        };

        @Config.Name("Armor Adjustments")
        @Config.Comment
                ({
                        "A list of carrying capacity increases or decreases that occur when players is wearing specific",
                        "armors or baubles. These value can be positive or negative.",
                        " Format - modId:itemId=adjustment",
                        "  modId:itemId - the item registry name",
                        "  adjustment - the carrying capacity weight adjustment, can be positive or negative or decimal values",
                        " Examples:",
                        "  minecraft:leather_chestplate=20",
                        "  minecraft:iron_chestplate=-20",
                        "  quark:backpack=200",
                })
        public String[] armorAdjustments = new String[] {

        };

        @Config.Name("Enchantment Adjustments")
        @Config.Comment
                ({
                        "A list of carrying capacity increases or decreases that occur when players are wearing equipment",
                        "enchanted with these effects. These value can be positive or negative. The enchantment level will",
                        "be multiplied by the adjustment value.",
                        " Format - enchantment=adjustment",
                        "  enchantment - the enchantment registry name",
                        "  adjustment - the carrying capacity weight adjustment, can be positive or negative or decimal values",
                        " Examples:",
                        "  minecraft:protection=-5",
                        "  minecraft:unbreaking=20"
                })
        public String[] enchantmentAdjustments = new String[] {

        };

        @Config.Name("Potion Adjustments")
        @Config.Comment
                ({
                        "A list of carrying capacity increases or decreases that occur when players are afflicted with",
                        "potion effects. These value can be positive or negative. The potion level will be multiplied by",
                        "the adjustment value.",
                        " Format - potionId=adjustment",
                        "  potionId - the potion registry name",
                        "  adjustment - the carrying capacity weight adjustment, can be positive or negative or decimal values",
                        " Examples:",
                        "  minecraft:strength=100",
                        "  minecraft:weakness=-100"
                })
        public String[] potionAdjustments = new String[] {
                "minecraft:strength=100",
                "minecraft:weakness=-100"
        };
    }

    public static class SettingsCategory {
        @Config.Name("Overlay X Offset")
        @Config.Comment("The HUD encumbrance bar x offset.")
        public int overlayXOffset = 0;

        @Config.Name("Overlay Y Offset")
        @Config.Comment("The HUD encumbrance bar y offset.")
        public int overlayYOffset = 0;

        @Config.Name("Reverse Overlay Direction")
        @Config.Comment("Reverses the HUD encumbrance bar direction.")
        public boolean overlayReverseDirection = false;

        @Config.Name("Weight Units")
        @Config.Comment("The weight units used by this mod. This value is purely cosmetic and does not change value calculations.")
        public String weightUnits = "MU";

        @Config.RangeDouble(min = 1.0, max = 1000000.0)
        @Config.Name("Max Carry Weight")
        @Config.Comment("The maximum amount of weight a player can carry before being overburdened.")
        public double maxCarryWeight = 1000;

        @Config.RangeInt(min = 1, max = 600)
        @Config.Name("Update Interval")
        @Config.Comment("The time, in ticks, between each update of the player's carry weight.")
        public int updateInterval = 20;
    }

    public static class EnchantmentCategory {
        public PackMuleEnchantCategory packMule = new PackMuleEnchantCategory();

        public static class PackMuleEnchantCategory {
            @Config.RequiresMcRestart
            @Config.Name("Enable Pack Mule Enchant")
            @Config.Comment("Enables the Pack Mule chest enchant, increasing player maximum carry weight.")
            public boolean enablePackMule = true;

            @Config.RangeInt(min = 0, max = 100)
            @Config.Name("Min Enchantability")
            @Config.Comment("The minimum enchantability of the Pack Mule enchant. ")
            public int minEnchantability = 10;

            @Config.RangeDouble(min = 0, max = 100000)
            @Config.Name("Carry Weight Bonus")
            @Config.Comment("The amount of bonus carry weight granted by the Pack Mule enchant.")
            public double carryWeightBonus = 200;
        }
    }

    public static class PotionEffectsCategory {
        public InjuredCategory injured = new InjuredCategory();
        public OverburdenedCategory overburdened = new OverburdenedCategory();

        public static class InjuredCategory {
            @Config.Name("Prevent Jumping")
            @Config.Comment("Reduces the player's jump height while they are injured.")
            public boolean restrictJumping = true;

            @Config.Name("Injury Damage Chance")
            @Config.Comment("The chance the player will be damage while moving ")
            public int injuryDamageChance = 5;


            @Config.RangeInt(min = 0, max = 1000)
            @Config.Name("Injury Hurt Distance")
            @Config.Comment("The distance a player needs to move before the injury damage check occurs.")
            public int injuryDamageDistance = 80;
        }

        public static class OverburdenedCategory {
            @Config.RangeInt(min = 1, max = 10000)
            @Config.Name("Weight Threshold")
            @Config.Comment("The amount of additional weight needed to reach the next level of overburdened. Players can carry up to four times this value while being overburdened.")
            public int overburdenedThreshold = 200;

            @Config.RangeInt(min = 0, max = 100)
            @Config.Name("Fall Injury Chance")
            @Config.Comment("The chance the player will be injured if taking fall damage while overburdened. This value is multiplied by the overburdened effect level.")
            public int fallInjuryChance = 20;

            @Config.RangeInt(min = 0, max = 100)
            @Config.Name("Movement Injury Chance")
            @Config.Comment
                    ({
                            "The chance the player will be injured if moving while overburdened. This value is multiplied by the",
                            "overburdened effect level. Movement injuries only occur when overburdened is level 3 or higher."
                    })
            public int movementInjuryChance = 5;

            @Config.RangeInt(min = 0, max = 1000)
            @Config.Name("Movement Injury Distance")
            @Config.Comment("The distance a player needs to move before an injury check occurs.")
            public int movementInjuryDistance = 20;

            @Config.Name("Jump Height Restriction")
            @Config.Comment("Reduces player jump height when overburdened. High levels of overburdened will prevent jumping altogether.")
            public boolean restrictJump = true;
        }
    }

    @Mod.EventBusSubscriber
    public static class ConfigChangeListener {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if(event.getModID().equals(EternallyOverburdened.MOD_ID)) {
                ConfigManager.sync(EternallyOverburdened.MOD_ID, Config.Type.INSTANCE);
                ConfigTags.syncConfig();
            }
        }
    }
}
