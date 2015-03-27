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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.Location;

/**
 * This class is part of LavaWater Bukkit plugin.
 * It represents a rule for replacing block after flooding by lava.
 * A Rule defines sacrifice (input), result (output), areas, where it will be applied and required conditions (presence of water).
 * @author LookyR
 */
public final class Rule {
    
    /**
     * Used in constructors
     */
    private static int counter = 0;
    
    /**
     * Unique number of the Rule
     */
    public final int id;
    
    /**
     * Type of input block (sacrifice)
     */
    public final Material input;
    
    /**
     * Type of output block (result)
     */
    public final Material output;
    
    /**
     * Type of input block (sacrifice), as it was given in command or config.yml
     */
    public final String inputString;
    
    /**
     * Type of output block (result), as it was given in command or config.yml
     */
    public final String outputString;
    
    /**
     * true if water is needed to generate the block
     */
    public final boolean needWater;
    
    /**
     * Use appliesTo()
     */
    private final Area[] areas;
    
    /**
     * String representation of this rule, colorless
     */
    public final String text;
    
    /**
     * String representation of this rule, including chat colors
     */
    public final String coloredText;
    
    /**
     * Creates new replacing rule
     * @param in The input block in the generator ("sacrifice"), e.g. Material.REDSTONE_WIRE
     * @param out The input block in the generator ("sacrifice"), e.g. Material.OBSIDIAN
     * @param nw Boolean (true, false, yes, no, t, f, ...) - true if water is needed for successfull generating
     * @param a Areas where this Rule can/cannot be applied, empty array for "everywhere"
     * @throws IllegalArgumentException When either "in" or "out" are not blocks
     * @throws NullPointerException When either "in", "out" or "a" are null
     * @throws NullPointerException When a contains a null value
     */
    public Rule(Material in, Material out, boolean nw, Area[] a) {
        if(!in.isBlock())
            throw new IllegalArgumentException(in.name() + " is not a block!");
        if(!out.isBlock())
            throw new IllegalArgumentException(out.name() + " is not a block!");
        for(Area area : a)
            if(area == null)
                throw new NullPointerException();
        id = counter++;
        input = in;
        output = out;
        inputString = in.name();
        outputString = out.name();
        needWater = nw;
        areas = a;
        text = getText(id, inputString, outputString, nw, a);
        coloredText = getColoredText(id, inputString, outputString, nw, a);
    }
    
    /**
     * Creates new replacing rule
     * @param in The input block in the generator ("sacrifice"), e.g. "redstone"
     * @param out The input block in the generator ("sacrifice"), e.g. "obsidian"
     * @param nw Boolean (true, false, yes, no, t, f, ...) - true if water is needed for successfull generating
     * @param a Areas where this Rule can/cannot be applied
     * @param logger Logger object or null
     * @throws EnumConstantNotPresentException When no Material matches "in" or "out" or it is not a block
     * @throws NullPointerException When either "in", "out" or "a" are null
     * @throws NullPointerException When a contains a null value
     */
    public Rule(String in, String out, String nw, List<String> a, Logger logger) {
        boolean b = true;
        if(nw != null)
            switch(nw.toLowerCase().trim()) {
                case "false": case "f": case "no": case "n": case "disallow": case "deny": case "d":
                    b = false;
                case "true": case "t": case "yes": case "y": case "allow": case "a":
                    break;
                default:
                    if(logger != null)
                        logger.log(Level.WARNING, "Invalid boolean value \"{0}\" - using default (true)", nw);
            }
        ArrayList<Area> list = new ArrayList();
        for(String area : a) {
            boolean inv = false;
            int i;
            for(i = 0; i < area.length(); i++)
                if(area.charAt(i) == '!' || area.charAt(i) == '^')
                    inv = !inv;
                else
                    break;
            String[] split = area.substring(i).split("\\s*,\\s*");
            try {
                switch(split.length) {
                    case 1:
                        list.add(new Area(
                                GetByName.world(split[0]),
                                inv
                        ));
                        break;
                    case 5:
                        list.add(new Area(
                                GetByName.world(split[0]),
                                Integer.parseInt(split[1]),
                                Integer.parseInt(split[2]),
                                Integer.parseInt(split[3]),
                                Integer.parseInt(split[4]),
                                inv
                        ));
                        break;
                    case 7:
                        list.add(new Area(
                                GetByName.world(split[0]),
                                Integer.parseInt(split[1]),
                                Integer.parseInt(split[2]),
                                Integer.parseInt(split[3]),
                                Integer.parseInt(split[4]),
                                Integer.parseInt(split[5]),
                                Integer.parseInt(split[6]),
                                inv
                        ));
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid area declaration: " + area);
                }
            } catch(NumberFormatException ex) {
                throw new IllegalArgumentException("Invalid area declaration: " + area, ex);
            }
        }
        id = counter++;
        input = GetByName.material(in);
        output = GetByName.material(out);
        inputString = in;
        outputString = out;
        needWater = b;
        areas = list.toArray(new Area[0]);
        text = getText(id, inputString, outputString, b, areas);
        coloredText = getColoredText(id, inputString, outputString, b, areas);
    }
    
    /**
     * Checks, whether this rule applies to a location
     * @param location Position of a block
     * @return true if this rule applies to the location
     */
    public final boolean appliesTo(Location location) {
        if(areas.length == 0)
            return true;
        boolean applies = areas[0].invert;
        for(Area area : areas)
            if(area.invert) {
                if(area.contains(location))
                    applies = false;
            } else
                if(area.contains(location))
                    applies = true;
        return applies;
    }
    
    /**
     * Returns Areas array as ArrayList of Strings
     * @return ArrayList of Strings
     */
    public final ArrayList<String> areasList() {
        ArrayList<String> list = new ArrayList(areas.length);
        for(Area area : areas)
            list.add(area.toString());
        return list;
    }
    
    /**
     * Used in constructors
     * @param id
     * @param in
     * @param out
     * @param nw
     * @param a
     * @return String representation saved to the 'text' field
     */
    private static String getText(int id, String in, String out, boolean nw, Area[] a) {
        return (nw ? " Lava + water + " : " Lava + ") + in + " = " + out + " (#" + id + ")";
    }
    
    /**
     * Used in constructors
     * @param id
     * @param in
     * @param out
     * @param nw
     * @param a
     * @return String representation saved to the 'coloredText' field
     */
    private static String getColoredText(int id, String in, String out, boolean nw, Area[] a) {
        return (nw ? "§e Lava §f+ §bwater §f+ §7" : "§e Lava §f+ §7") + in + " §f= §7" + out + " §f(#" + id + ")";
    }
}
