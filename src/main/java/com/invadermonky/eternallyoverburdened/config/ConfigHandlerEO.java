package com.invadermonky.eternallyoverburdened.config;

import com.invadermonky.eternallyoverburdened.EternallyOverburdened;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = EternallyOverburdened.MOD_ID)
public class ConfigHandlerEO {
    @Config.Name("Client Settings")
    public static ClientSettingsCategory clientSettings = new ClientSettingsCategory();
    @Config.Name("Item Settings")
    public static ItemWeightsCategory itemSettings = new ItemWeightsCategory();
    @Config.Name("Enchantment Settings")
    public static EnchantmentCategory enchantmentSettings = new EnchantmentCategory();
    @Config.Name("Potion Settings")
    public static PotionEffectsCategory potionSettings = new PotionEffectsCategory();
    @Config.Name("Player Settings")
    public static PlayerSettingsCategory playerSettings = new PlayerSettingsCategory();

    public static class ClientSettingsCategory {
        @Config.Name("Overlay X Offset")
        @Config.Comment("The HUD encumbrance bar x offset.")
        public int overlayXOffset = 10;

        @Config.Name("Overlay Y Offset")
        @Config.Comment("The HUD encumbrance bar y offset.")
        public int overlayYOffset = -49;

        @Config.Name("Offset In Water")
        @Config.Comment("Causes the HUD encumbrance bar to automatically adjust it's position when the player is underwater.")
        public boolean offsetInWater = true;

        @Config.Name("Reverse Overlay Direction")
        @Config.Comment("Reverses the HUD encumbrance bar direction.")
        public boolean overlayReverseDirection = false;

        @Config.Name("Weight Units")
        @Config.Comment("The weight units used by this mod. This value is purely cosmetic and does not change value calculations.")
        public String weightUnits = "MU";
    }

    public static class PlayerSettingsCategory {
        @Config.RangeDouble(min = 1.0, max = 1000000.0)
        @Config.Name("Max Carry Weight")
        @Config.Comment("The maximum amount of weight a player can carry before being overburdened.")
        public double maxCarryWeight = 1000;

        @Config.RangeInt(min = 1, max = 600)
        @Config.Name("Update Interval")
        @Config.Comment("The time, in ticks, between each update of the player's carry weight.")
        public int updateInterval = 20;
    }

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
                        "A blacklist of items that should not calculate values of any internal storage. An example of this would be inventory or fluid container items.",
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
                        "A list of fluids and their respective weights. Weights are per 1mb and can be positive or negative. Negative fluid weights will decrease the weight of other items in the player's inventory.",
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
                        " Format: modId:itemId:meta=weight",
                        "  modId:itemId - the item registry name",
                        "  meta - [optional] the item metadata, leaving this value blank will match all items with this registry name",
                        "  weight - the weight of this item",
                        " Examples:",
                        "  minecraft:stone=2",
                        "  minecraft:stone:1=2",
                        "  minecraft:stick=0.2",
                        "  minecraft:feather=0.01"
                })
        public String[] itemWeights = new String[] {
                "baubles:ring=0.1",
                "eternallyoverburdened:splint=0.3",
                "minecraft:acacia_boat=3",
                "minecraft:acacia_door=2",
                "minecraft:anvil=28",
                "minecraft:apple=0.2",
                "minecraft:arrow=0.1",
                "minecraft:baked_potato=0.2",
                "minecraft:barrier=0",
                "minecraft:bed=2",
                "minecraft:bedrock=9001",
                "minecraft:beef=0.4",
                "minecraft:beetroot=0.2",
                "minecraft:beetroot_seeds=0.1",
                "minecraft:beetroot_soup=0.3",
                "minecraft:birch_boat=3",
                "minecraft:birch_door=2",
                "minecraft:blaze_powder=0.1",
                "minecraft:blaze_rod=0.2",
                "minecraft:boat=3",
                "minecraft:bone=0.3",
                "minecraft:book=0.3",
                "minecraft:bowl=0.1",
                "minecraft:bread=0.2",
                "minecraft:brick=0.2",
                "minecraft:brown_mushroom=0.1",
                "minecraft:carpet=0.1",
                "minecraft:carrot=0.2",
                "minecraft:cauldron=4",
                "minecraft:chest_minecart=2",
                "minecraft:chicken=0.3",
                "minecraft:chorus_flower=0.5",
                "minecraft:chorus_fruit=0.2",
                "minecraft:chorus_fruit_popped=0.2",
                "minecraft:chorus_plant=0.4",
                "minecraft:clay_ball=0.2",
                "minecraft:coal=0.1",
                "minecraft:compass=0.5",
                "minecraft:cooked_beef=0.2",
                "minecraft:cooked_chicken=0.2",
                "minecraft:cooked_fish=0.2",
                "minecraft:cooked_mutton=0.2",
                "minecraft:cooked_porkchop=0.2",
                "minecraft:cooked_rabbit=0.2",
                "minecraft:cookie=0.1",
                "minecraft:dark_oak_boat=3",
                "minecraft:dark_oak_door=2",
                "minecraft:deadbush=0.2",
                "minecraft:diamond=0.1",
                "minecraft:diamond_horse_armor=3",
                "minecraft:double_plant=0.4",
                "minecraft:dragon_breath=0.3",
                "minecraft:dye=0.1",
                "minecraft:egg=0.2",
                "minecraft:elytra=0.4",
                "minecraft:emerald=0.1",
                "minecraft:enchanted_book=0.3",
                "minecraft:end_rod=0.3",
                "minecraft:ender_eye=0.3",
                "minecraft:ender_pearl=0.2",
                "minecraft:experience_bottle=0.3",
                "minecraft:feather=0.01",
                "minecraft:fermented_spider_eye=0.2",
                "minecraft:filled_map=0.2",
                "minecraft:fire_charge=0.2",
                "minecraft:firework_charge=0.2",
                "minecraft:fireworks=0.2",
                "minecraft:fish=0.3",
                "minecraft:flint=0.3",
                "minecraft:flower_pot=0.5",
                "minecraft:furnace_minecart=2",
                "minecraft:ghast_tear=0.2",
                "minecraft:glass_bottle=0.2",
                "minecraft:glass_pane=0.3",
                "minecraft:glowstone_dust=0.1",
                "minecraft:gold_block=10",
                "minecraft:gold_ingot=1",
                "minecraft:gold_nugget=0.1",
                "minecraft:gold_ore=2",
                "minecraft:golden_axe=2.5",
                "minecraft:golden_boots=3",
                "minecraft:golden_carrot=0.5",
                "minecraft:golden_chestplate=7",
                "minecraft:golden_helmet=4.5",
                "minecraft:golden_hoe=2",
                "minecraft:golden_horse_armor=12",
                "minecraft:golden_leggings=6",
                "minecraft:golden_pickaxe=2.5",
                "minecraft:golden_sword=2",
                "minecraft:gunpowder=0.2",
                "minecraft:heavy_weighted_pressure_plate=2",
                "minecraft:hopper=2",
                "minecraft:hopper_minecart=3",
                "minecraft:iron_axe=1.5",
                "minecraft:iron_bars=0.4",
                "minecraft:iron_block=4",
                "minecraft:iron_boots=2",
                "minecraft:iron_chestplate=3",
                "minecraft:iron_door=3",
                "minecraft:iron_helmet=2",
                "minecraft:iron_horse_armor=5",
                "minecraft:iron_ingot=0.4",
                "minecraft:iron_leggings=2.5",
                "minecraft:iron_nugget=0.1",
                "minecraft:iron_pickaxe=1.5",
                "minecraft:iron_trapdoor=2",
                "minecraft:item_frame=0.2",
                "minecraft:jungle_boat=3",
                "minecraft:jungle_door=2",
                "minecraft:knowledge_book=0.3",
                "minecraft:ladder=0.2",
                "minecraft:lead=0.2",
                "minecraft:leather=0.2",
                "minecraft:leaves=0.5",
                "minecraft:leaves2=0.5",
                "minecraft:lever=0.2",
                "minecraft:lingering_potion=0.4",
                "minecraft:magma_cream=0.2",
                "minecraft:map=0.3",
                "minecraft:melon=0.2",
                "minecraft:melon_seeds=0.1",
                "minecraft:milk_bucket=2",
                "minecraft:monster_egg=0.5",
                "minecraft:mushroom_stew=0.3",
                "minecraft:mutton=0.3",
                "minecraft:name_tag=0.1",
                "minecraft:nether_star=0.5",
                "minecraft:nether_wart=0.1",
                "minecraft:painting=0.5",
                "minecraft:paper=0.1",
                "minecraft:poisonous_potato=0.2",
                "minecraft:porkchop=0.3",
                "minecraft:potato=0.2",
                "minecraft:potion=0.4",
                "minecraft:prismarine_crystals=0.2",
                "minecraft:prismarine_shard=0.2",
                "minecraft:pumpkin_seeds=0.1",
                "minecraft:purpur_slab=0.5",
                "minecraft:quartz=0.2",
                "minecraft:rabbit=0.3",
                "minecraft:rabbit_foot=0.2",
                "minecraft:rabbit_hide=0.2",
                "minecraft:rabbit_stew=0.3",
                "minecraft:record_11=0.2",
                "minecraft:record_13=0.2",
                "minecraft:record_cat=0.2",
                "minecraft:record_chirp=0.2",
                "minecraft:record_far=0.2",
                "minecraft:record_mall=0.2",
                "minecraft:record_mellohi=0.2",
                "minecraft:record_stal=0.2",
                "minecraft:record_strad=0.2",
                "minecraft:record_wait=0.2",
                "minecraft:record_ward=0.2",
                "minecraft:red_flower=0.1",
                "minecraft:red_mushroom=0.1",
                "minecraft:redstone=0.1",
                "minecraft:redstone_torch=0.2",
                "minecraft:reeds=0.2",
                "minecraft:rotten_flesh=0.2",
                "minecraft:sapling=0.2",
                "minecraft:shield=1.5",
                "minecraft:shulker_shell=0.2",
                "minecraft:sign=0.5",
                "minecraft:skull=0.5",
                "minecraft:slime_ball=0.2",
                "minecraft:snow_layer=0.2",
                "minecraft:snowball=0.2",
                "minecraft:spawn_egg=0.5",
                "minecraft:spectral_arrow=0.1",
                "minecraft:spider_eye=0.2",
                "minecraft:splash_potion=0.4",
                "minecraft:spruce_boat=3",
                "minecraft:spruce_door=2",
                "minecraft:stained_glass_pane=0.3",
                "minecraft:stick=0.2",
                "minecraft:stone_button=0.1",
                "minecraft:stone_pressure_plate=0.5",
                "minecraft:stone_slab=0.5",
                "minecraft:stone_slab2=0.5",
                "minecraft:string=0.2",
                "minecraft:sugar=0.1",
                "minecraft:tipped_arrow=0.1",
                "minecraft:tnt_minecart=2",
                "minecraft:torch=0.2",
                "minecraft:totem_of_undying=0.2",
                "minecraft:tripwire_hook=0.2",
                "minecraft:vine=0.2",
                "minecraft:waterlily=0.2",
                "minecraft:web=0.5",
                "minecraft:wheat=0.2",
                "minecraft:wheat_seeds=0.1",
                "minecraft:wooden_button=0.2",
                "minecraft:wooden_door=2.0",
                "minecraft:wooden_pressure_plate=0.5",
                "minecraft:wooden_slab=0.5",
                "minecraft:wool=0.5",
                "minecraft:writable_book=0.3",
                "minecraft:written_book=0.3",
                "minecraft:yellow_flower=0.1"
        };

        @Config.Name("Equipment Adjustments")
        @Config.Comment
                ({
                        "A list of carrying capacity increases or decreases that occur when players is wearing specific armors or baubles. These value can be positive or negative.",
                        " Format: modId:itemId:meta=adjustment",
                        "  modId:itemId - the item registry name",
                        "  meta - [optional] the item metadata, leaving this value blank will match all items with this registry name",
                        "  adjustment - the carrying capacity weight adjustment, can be positive or negative or decimal values",
                        " Examples:",
                        "  minecraft:leather_chestplate=20",
                        "  minecraft:iron_chestplate=-20",
                        "  quark:backpack=200",
                        "  baubles:ring:0=10"
                })
        public String[] equipmentAdjustments = new String[] {
                "quark:backpack=200"
        };

        @Config.Name("Enchantment Adjustments")
        @Config.Comment
                ({
                        "A list of carrying capacity increases or decreases that occur when players are wearing equipment enchanted with these effects. These value can be positive or negative. The enchantment level will be multiplied by the adjustment value.",
                        " Format: enchantment=adjustment",
                        "  enchantment - the enchantment registry name",
                        "  adjustment - the carrying capacity weight adjustment, can be positive or negative or decimal values",
                        " Examples:",
                        "  minecraft:protection=-5",
                        "  minecraft:unbreaking=20"
                })
        public String[] enchantmentAdjustments = new String[] {};

        @Config.Name("Potion Adjustments")
        @Config.Comment
                ({
                        "A list of carrying capacity increases or decreases that occur when players are afflicted with",
                        "potion effects. These value can be positive or negative. The potion level will be multiplied by",
                        "the adjustment value.",
                        " Format: potionId=adjustment",
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

    public static class EnchantmentCategory {
        @Config.Comment("Pack Mule Enchant")
        public PackMuleEnchantCategory packMule = new PackMuleEnchantCategory();

        public static class PackMuleEnchantCategory {
            @Config.RequiresMcRestart
            @Config.Name("Enable Pack Mule Enchant")
            @Config.Comment("Enables the Pack Mule chest enchant, increasing player maximum carry weight.")
            public boolean enablePackMule = true;

            @Config.RangeInt(min = 0, max = 100)
            @Config.Name("Min Enchantability")
            @Config.Comment("The minimum enchantability of the Pack Mule enchant. ")
            public int minEnchantability = 8;

            @Config.RangeDouble(min = 0, max = 100000)
            @Config.Name("Carry Weight Bonus")
            @Config.Comment("The amount of bonus carry weight granted by the Pack Mule enchant.")
            public double carryWeightBonus = 100;
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

            @Config.RangeInt(min = 1, max = 10000)
            @Config.Name("Injury Duration")
            @Config.Comment("The duration, in seconds, that an injury will last.")
            public int injuryDuration = 480;
        }

        public static class OverburdenedCategory {
            @Config.RangeInt(min = 1, max = 100000)
            @Config.Name("Weight Threshold")
            @Config.Comment("The amount of additional weight needed to reach the next level of overburdened. Players can carry up to four times this value while being overburdened.")
            public int overburdenedThreshold = 150;

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
                WeightSettings.syncConfig();
            }
        }
    }
}
