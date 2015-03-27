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

import org.bukkit.Location;
import org.bukkit.World;

/**
 * This class is part of LavaWater Bukkit plugin.
 * It represents any region, residence, plot, or another part of world.
 * Multiple Areas can be attached to one Rule.
 * @author LookyR
 */
public final class Area {
    
    /**
     * Use contains() or toString()
     */
    private final World w;
    
    /**
     * Use contains() or toString()
     */
    private final int x1, y1, z1, x2, y2, z2;
    
    /**
     * Use toString()
     */
    private final byte type;
    
    /**
     * false means inside, true means outside
     */
    public final boolean invert;
    
    /**
     * Creates new Area object with all coordinates set to given values.
     * Eventually swaps respective coordinates so first is always smaller (x1≤x2, 0≤y1≤y2≤256, z1≤z2).
     * @param w World
     * @param x1 First X (longitude) coordinate - east (positive) or west (negative)
     * @param y1 First Y (altitude) coordinate - up (positive) or down (negative)
     * @param z1 First Z (latitude) coordinate - south (positive) or north (negative)
     * @param x2 Second X (longitude) coordinate - east (positive) or west (negative)
     * @param y2 Second Y (altitude) coordinate - up (positive) or down (negative)
     * @param z2 Second Z (latitude) coordinate - south (positive) or north (negative)
     * @param invert false means inside, true means outside
     */
    public Area(World w, int x1, int y1, int z1, int x2, int y2, int z2, boolean invert) {
        this.w = w;
        this.x1 = Math.min(x1, x2);
        this.y1 = Math.min(0, Math.min(y1, y2));
        this.z1 = Math.min(z1, z2);
        this.x2 = Math.max(x1, x2);
        this.y2 = Math.max(256, Math.max(y1, y2));
        this.z2 = Math.max(z1, z2);
        this.invert = invert;
        type = 0;
    }
    
    /**
     * Creates new Area object with X (longitude) and Z (latitude) coordinates set.
     * Eventually swaps respective coordinates so first is always smaller (x1≤x2, z1≤z2).
     * Y (altitude) coordinates are set to 0 and 256.
     * @param w World
     * @param x1 First X (longitude) coordinate - east (positive) or west (negative)
     * @param z1 First Z (latitude) coordinate - south (positive) or north (negative)
     * @param x2 Second X (longitude) coordinate - east (positive) or west (negative)
     * @param z2 Second Z (latitude) coordinate - south (positive) or north (negative)
     * @param invert false means inside, true means outside
     */
    public Area(World w, int x1, int z1, int x2, int z2, boolean invert) {
        this.w = w;
        this.x1 = Math.min(x1, x2);
        this.y1 = 0;
        this.z1 = Math.min(z1, z2);
        this.x2 = Math.max(x1, x2);
        this.y2 = 256;
        this.z2 = Math.max(z1, z2);
        this.invert = invert;
        type = 1;
    }
    
    /**
     * Creates new Area object without coordinates.
     * X (longitude) coordinates are set to Integer.MIN_VALUE and Integer.MAX_VALUE.
     * Y (altitude) coordinates are set to 0 and 256.
     * Z (latitude) coordinates are set to Integer.MIN_VALUE and Integer.MAX_VALUE.
     * @param w World
     * @param invert false means inside, true means outside
     */
    public Area(World w, boolean invert) {
        this.w = w;
        this.x1 = Integer.MIN_VALUE;
        this.y1 = 0;
        this.z1 = Integer.MIN_VALUE;
        this.x2 = Integer.MAX_VALUE;
        this.y2 = 256;
        this.z2 = Integer.MAX_VALUE;
        this.invert = invert;
        type = 2;
    }
    
    /**
     * Checks if a block is within an instance of Area
     * @param block Block to check
     * @return true if a block is within the area, false if outside
     */
    public final boolean contains(Location block) {
        int x = block.getBlockX(), y = block.getBlockY(), z = block.getBlockZ();
        return (w == null || w.equals(block.getWorld())) && x1 <= x && x <= x2 && y1 <= y && y <= y2 && z1 <= z && z <= z2;
    }
    
    /**
     * Returns computer- and human-readable form of this object.
     * @return String representing this object
     */ @Override
    public final String toString() {
        switch(type) {
            case 0:
                return (invert ? "!" + w.getUID() : w.getUID()) +
                        ", " + x1 + ", " + y1 + ", " + z1 + ", " + x2 + ", " + y2 + ", " + z2;
            case 1:
                return (invert ? "!" + w.getUID() : w.getUID()) +
                        ", " + x1 + ", " + z1 + ", " + x2 + ", " + z2;
            case 2:
                return (invert ? "!" + w.getUID() : w.getUID().toString());
            default:
                throw new AssertionError();
        }
    }
}
