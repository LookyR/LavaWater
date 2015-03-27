/*
 * Creative Commons License
 *
 * LavaWater by LookyR is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International License.
 * Based on a work at https://github.com/LookyR/LavaWater.
 *
 * See http://creativecommons.org/licenses/by-nc-sa/4.0/
 */

package cz.lookyr.lavawater;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;

import static org.bukkit.Material.*;

/**
 * This class is part of LavaWater Bukkit plugin.
 * It provides string parsing functions.
 * @author LookyR
 */
public final class GetByName {
    
    /**
     * Throws UnsupportedOperationException when called
     * @throws UnsupportedOperationException when called
     */
    private GetByName() {
        throw new UnsupportedOperationException("Singleton");
    }
    
    /**
     * Get world with given name or UUID
     * @param name Name or UUID as String or "null" (not to be confused with null pointer)
     * @throws IllegalArgumentException If world doesn't exist
     * @throws NullPointerException If name is null (not "null")
     * @return World object or null for "null"
     */
    public static World world(String name) {
        name = name.trim();
        if(name.equalsIgnoreCase("null"))
            return null;
        World world = Bukkit.getWorld(name);
        if(world != null)
            return world;
        try {
            world = Bukkit.getWorld(UUID.fromString(name));
        } catch(IllegalArgumentException ex) {
            throw new IllegalArgumentException("World not found: " + name, ex);
        }
        if(world != null)
            return world;
        throw new IllegalArgumentException("World not found: " + name);
    }
    
    /**
     * Get material with given name or id
     * @param name Name or id as String
     * @throws IllegalArgumentException If material doesn't exist or is not a block
     * @throws NullPointerException If name is null
     * @return Material enum instance
     */
    public static Material material(String name) {
        switch(name.trim().toLowerCase()) {
            case "0": case "air": return AIR;
            case "1": case "stone": return STONE;
            case "2": case "grass": return GRASS;
            case "3": case "dirt": return DIRT;
            case "4": case "cobblestone": case "cobble": return COBBLESTONE;
            case "5": case "wood": case "plank": case "planks": return WOOD;
            case "6": case "sapling": return SAPLING;
            case "7": case "bedrock": return BEDROCK;
            case "8": case "water": return WATER;
            case "9": case "stationary_water": case "stationary-water": return STATIONARY_WATER;
            case "10": case "lava": return LAVA;
            case "11": case "stationary_lava": case "stationary-lava": return STATIONARY_LAVA;
            case "12": case "sand": return SAND;
            case "13": case "gravel": return GRAVEL;
            case "14": case "gold_ore": case "gold-ore": return GOLD_ORE;
            case "15": case "iron_ore": case "iron-ore": return IRON_ORE;
            case "16": case "coal_ore": case "coal-ore": return COAL_ORE;
            case "17": case "log": return LOG;
            case "18": case "leaves": return LEAVES;
            case "19": case "sponge": return SPONGE;
            case "20": case "glass": return GLASS;
            case "21": case "lapis_ore": case "lapis-ore": return LAPIS_ORE;
            case "22": case "lapis_block": case "lapis-block": case "lapis": case "lapiz": return LAPIS_BLOCK;
            case "23": case "dispenser": return DISPENSER;
            case "24": case "sandstone": return SANDSTONE;
            case "25": case "note_block": case "note-block": return NOTE_BLOCK;
            case "26": case "bed_block": case "bed-block": case "bed": return BED_BLOCK;
            case "27": case "powered_rail": case "powered-rail": return POWERED_RAIL;
            case "28": case "detector_rail": case "detector-rail": return DETECTOR_RAIL;
            case "29": case "piston_sticky_base": case "piston-sticky-base": return PISTON_STICKY_BASE;
            case "30": case "web": return WEB;
            case "31": case "long_grass": case "long-grass": return LONG_GRASS;
            case "32": case "dead_bush": case "dead-bush": return DEAD_BUSH;
            case "33": case "piston_base": case "piston-base": return PISTON_BASE;
            case "34": case "piston_extension": case "piston-extension": return PISTON_EXTENSION;
            case "35": case "wool": return WOOL;
            case "36": case "piston_moving_piece": case "piston-moving-piece": return PISTON_MOVING_PIECE;
            case "37": case "yellow_flower": case "yellow-flower": return YELLOW_FLOWER;
            case "38": case "red_rose": case "red-rose": return RED_ROSE;
            case "39": case "brown_mushroom": case "brown-mushroom": return BROWN_MUSHROOM;
            case "40": case "red_mushroom": case "red-mushroom": return RED_MUSHROOM;
            case "41": case "gold_block": case "gold-block": case "gold": return GOLD_BLOCK;
            case "42": case "iron_block": case "iron-block": case "iron": return IRON_BLOCK;
            case "43": case "double_step": case "double-step": return DOUBLE_STEP;
            case "44": case "step": return STEP;
            case "45": case "brick": return BRICK;
            case "46": case "tnt": return TNT;
            case "47": case "bookshelf": return BOOKSHELF;
            case "48": case "mossy_cobblestone": case "mossy-cobblestone": return MOSSY_COBBLESTONE;
            case "49": case "obsidian": return OBSIDIAN;
            case "50": case "torch": return TORCH;
            case "51": case "fire": return FIRE;
            case "52": case "mob_spawner": case "mob-spawner": return MOB_SPAWNER;
            case "53": case "wood_stairs": case "wood-stairs": return WOOD_STAIRS;
            case "54": case "chest": return CHEST;
            case "55": case "redstone_wire": case "redstone-wire": case "redstone": return REDSTONE_WIRE;
            case "56": case "diamond_ore": case "diamond-ore": return DIAMOND_ORE;
            case "57": case "diamond_block": case "diamond-block": case "diamond": return DIAMOND_BLOCK;
            case "58": case "workbench": return WORKBENCH;
            case "59": case "crops": return CROPS;
            case "60": case "soil": return SOIL;
            case "61": case "furnace": return FURNACE;
            case "62": case "burning_furnace": case "burning-furnace": return BURNING_FURNACE;
            case "63": case "sign_post": case "sign-post": return SIGN_POST;
            case "64": case "wooden_door": case "wooden-door": return WOODEN_DOOR;
            case "65": case "ladder": return LADDER;
            case "66": case "rails": return RAILS;
            case "67": case "cobblestone_stairs": case "cobblestone-stairs": return COBBLESTONE_STAIRS;
            case "68": case "wall_sign": case "wall-sign": return WALL_SIGN;
            case "69": case "lever": return LEVER;
            case "70": case "stone_plate": case "stone-plate": return STONE_PLATE;
            case "71": case "iron_door_block": case "iron-door-block": case "iron_door": case "iron-door": return IRON_DOOR_BLOCK;
            case "72": case "wood_plate": case "wood-plate": return WOOD_PLATE;
            case "73": case "redstone_ore": case "redstone-ore": return REDSTONE_ORE;
            case "74": case "glowing_redstone_ore": case "glowing-redstone-ore": return GLOWING_REDSTONE_ORE;
            case "75": case "redstone_torch_off": case "redstone-torch-off": return REDSTONE_TORCH_OFF;
            case "76": case "redstone_torch_on": case "redstone-torch-on": return REDSTONE_TORCH_ON;
            case "77": case "stone_button": case "stone-button": return STONE_BUTTON;
            case "78": case "snow": return SNOW;
            case "79": case "ice": return ICE;
            case "80": case "snow_block": case "snow-block": return SNOW_BLOCK;
            case "81": case "cactus": return CACTUS;
            case "82": case "clay": return CLAY;
            case "83": case "sugar_cane_block": case "sugar-cane-block": case "sugar_cane": case "sugar-cane": return SUGAR_CANE_BLOCK;
            case "84": case "jukebox": return JUKEBOX;
            case "85": case "fence": return FENCE;
            case "86": case "pumpkin": return PUMPKIN;
            case "87": case "netherrack": return NETHERRACK;
            case "88": case "soul_sand": case "soul-sand": return SOUL_SAND;
            case "89": case "glowstone": return GLOWSTONE;
            case "90": case "portal": return PORTAL;
            case "91": case "jack_o_lantern": case "jack-o-lantern": return JACK_O_LANTERN;
            case "92": case "cake_block": case "cake-block": case "cake": return CAKE_BLOCK;
            case "93": case "diode_block_off": case "diode-block-off": case "diode_off": case "diode-off": return DIODE_BLOCK_OFF;
            case "94": case "diode_block_on": case "diode-block-on": case "diode_on": case "diode-on": return DIODE_BLOCK_ON;
            //case "95": case "stained_glass": case "stained-glass": return STAINED_GLASS;
            case "96": case "trap_door": case "trap-door": return TRAP_DOOR;
            case "97": case "monster_eggs": case "monster-eggs": return MONSTER_EGGS;
            case "98": case "smooth_brick": case "smooth-brick": return SMOOTH_BRICK;
            case "99": case "huge_mushroom_1": case "huge-mushroom-1": return HUGE_MUSHROOM_1;
            case "100": case "huge_mushroom_2": case "huge-mushroom-2": return HUGE_MUSHROOM_2;
            case "101": case "iron_fence": case "iron-fence": return IRON_FENCE;
            case "102": case "thin_glass": case "thin-glass": return THIN_GLASS;
            case "103": case "melon_block": case "melon-block": case "melon": return MELON_BLOCK;
            case "104": case "pumpkin_stem": case "pumpkin-stem": return PUMPKIN_STEM;
            case "105": case "melon_stem": case "melon-stem": return MELON_STEM;
            case "106": case "vine": return VINE;
            case "107": case "fence_gate": case "fence-gate": return FENCE_GATE;
            case "108": case "brick_stairs": case "brick-stairs": return BRICK_STAIRS;
            case "109": case "smooth_stairs": case "smooth-stairs": return SMOOTH_STAIRS;
            case "110": case "mycel": return MYCEL;
            case "111": case "water_lily": case "water-lily": return WATER_LILY;
            case "112": case "nether_brick": case "nether-brick": return NETHER_BRICK;
            case "113": case "nether_fence": case "nether-fence": return NETHER_FENCE;
            case "114": case "nether_brick_stairs": case "nether-brick-stairs": return NETHER_BRICK_STAIRS;
            case "115": case "nether_warts": case "nether-warts": return NETHER_WARTS;
            case "116": case "enchantment_table": case "enchantment-table": return ENCHANTMENT_TABLE;
            case "117": case "brewing_stand": case "brewing-stand": return BREWING_STAND;
            case "118": case "cauldron": return CAULDRON;
            case "119": case "ender_portal": case "ender-portal": return ENDER_PORTAL;
            case "120": case "ender_portal_frame": case "ender-portal-frame": return ENDER_PORTAL_FRAME;
            case "121": case "ender_stone": case "ender-stone": return ENDER_STONE;
            case "122": case "dragon_egg": case "dragon-egg": return DRAGON_EGG;
            case "123": case "redstone_lamp_off": case "redstone-lamp-off": return REDSTONE_LAMP_OFF;
            case "124": case "redstone_lamp_on": case "redstone-lamp-on": return REDSTONE_LAMP_ON;
            case "125": case "wood_double_step": case "wood-double-step": return WOOD_DOUBLE_STEP;
            case "126": case "wood_step": case "wood-step": return WOOD_STEP;
            case "127": case "cocoa": return COCOA;
            case "128": case "sandstone_stairs": case "sandstone-stairs": return SANDSTONE_STAIRS;
            case "129": case "emerald_ore": case "emerald-ore": return EMERALD_ORE;
            case "130": case "ender_chest": case "ender-chest": return ENDER_CHEST;
            case "131": case "tripwire_hook": case "tripwire-hook": return TRIPWIRE_HOOK;
            case "132": case "tripwire": case "string": return TRIPWIRE;
            case "133": case "emerald_block": case "emerald-block": case "emerald": return EMERALD_BLOCK;
            case "134": case "spruce_wood_stairs": case "spruce-wood-stairs": return SPRUCE_WOOD_STAIRS;
            case "135": case "birch_wood_stairs": case "birch-wood-stairs": return BIRCH_WOOD_STAIRS;
            case "136": case "jungle_wood_stairs": case "jungle-wood-stairs": return JUNGLE_WOOD_STAIRS;
            case "137": case "command": return COMMAND;
            case "138": case "beacon": return BEACON;
            case "139": case "cobble_wall": case "cobble-wall": return COBBLE_WALL;
            case "140": case "flower_pot": case "flower-pot": return FLOWER_POT;
            case "141": case "carrot": return CARROT;
            case "142": case "potato": return POTATO;
            case "143": case "wood_button": case "wood-button": return WOOD_BUTTON;
            case "144": case "skull": return SKULL;
            case "145": case "anvil": return ANVIL;
            case "146": case "trapped_chest": case "trapped-chest": return TRAPPED_CHEST;
            case "147": case "gold_plate": case "gold-plate": return GOLD_PLATE;
            case "148": case "iron_plate": case "iron-plate": return IRON_PLATE;
            case "149": case "redstone_comparator_off": case "redstone-comparator-off": return REDSTONE_COMPARATOR_OFF;
            case "150": case "redstone_comparator_on": case "redstone-comparator-on": return REDSTONE_COMPARATOR_ON;
            case "151": case "daylight_detector": case "daylight-detector": return DAYLIGHT_DETECTOR;
            case "152": case "redstone_block": case "redstone-block": return REDSTONE_BLOCK;
            case "153": case "quartz_ore": case "quartz-ore": return QUARTZ_ORE;
            case "154": case "hopper": return HOPPER;
            case "155": case "quartz_block": case "quartz-block": case "quartz": return QUARTZ_BLOCK;
            case "156": case "quartz_stairs": case "quartz-stairs": return QUARTZ_STAIRS;
            case "157": case "activator_rail": case "activator-rail": return ACTIVATOR_RAIL;
            case "158": case "dropper": return DROPPER;
            case "159": case "stained_clay": case "stained-clay": return STAINED_CLAY;
            //case "160": case "stained_glass_pane": case "stained-glass-pane": return STAINED_GLASS_PANE;
            //case "161": case "leaves_2": case "leaves-2": return LEAVES_2;
            //case "162": case "log_2": case "log-2": return LOG_2;
            //case "163": case "acacia_stairs": case "acacia-stairs": return ACACIA_STAIRS;
            //case "164": case "dark_oak_stairs": case "dark-oak-stairs": return DARK_OAK_STAIRS;
            case "170": case "hay_block": case "hay-block": case "hay": case "haystack": case "hay_bale": case "hay-bale": return HAY_BLOCK;
            case "171": case "carpet": return CARPET;
            case "172": case "hard_clay": case "hard-clay": return HARD_CLAY;
            case "173": case "coal_block": case "coal-block": case "coal": return COAL_BLOCK;
            //case "174": case "packed_ice": case "packed-ice": return PACKED_ICE;
            //case "175": case "double_plant": case "double-plant": return DOUBLE_PLANT;
            default:
                throw new IllegalArgumentException(name + " doesn't exist or is not a block.");
        }
    }
}
