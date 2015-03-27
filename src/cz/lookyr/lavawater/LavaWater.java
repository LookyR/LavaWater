/*
 * Creative Commons License
 *
 * LavaWater by LookyR is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International License.
 * Based on a work at https://github.com/LookyR/LavaWater.
 */

package cz.lookyr.lavawater;

import java.util.logging.Level;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import org.bukkit.Bukkit;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import static org.bukkit.Material.LAVA;
import static org.bukkit.Material.WATER;
import static org.bukkit.Material.STATIONARY_LAVA;
import static org.bukkit.Material.STATIONARY_WATER;
import static org.bukkit.block.BlockFace.WEST;
import static org.bukkit.block.BlockFace.EAST;
import static org.bukkit.block.BlockFace.NORTH;
import static org.bukkit.block.BlockFace.SOUTH;

/**
 * This plugin allows you to change result of cobblestone generator depending on which block was in the space before.
 * It was created as a reaction to fixing <a href="https://bugs.mojang.com/browse/MC-4239">the obsidian-generator bug</a>.
 * This is the main class of plugin.
 * @author LookyR
 */
public final class LavaWater extends JavaPlugin implements Listener {
    
    /**
     * List of Rules
     * @see Rule
     */
    public final ArrayList<Rule> rules = new ArrayList();

    /**
     * List of Worlds
     * @see World
     */
    public final HashSet<World> worlds = new HashSet();
    
    /**
     * Registers event handlers, initializes list of replaced blocks
     * - called when server is loaded and plugin is enabled
     * @see org.bukkit.plugin.Plugin#onEnable()
     */ @Override
    public final void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        rules.clear();
        worlds.clear();
        worlds.addAll(Bukkit.getWorlds());
        for(String key : config.getKeys(true))
            if(key.startsWith("rules.")) {
                if(key.lastIndexOf(".") == 5)
                    try {
                        rules.add(new Rule(
                                key.substring(6),
                                config.getString(key + ".result"),
                                config.getString(key + ".needwater"),
                                config.getStringList(key + ".area"),
                                getLogger()
                        ));
                    } catch(IllegalArgumentException ex) {
                        getLogger().log(Level.WARNING, ex.getMessage());
                        getLogger().log(Level.INFO, "Skipping this rule.");
                    }
            } else
                switch(key) {
                    case "worlds":
                        try {
                            worlds.clear();
                            for(String world : config.getStringList("worlds"))
                                worlds.add(GetByName.world(world));
                        } catch(IllegalArgumentException ex) {
                            getLogger().log(Level.WARNING, ex.getMessage());
                            getLogger().log(Level.INFO, "Enabling for all worlds.");
                            worlds.clear();
                            worlds.addAll(Bukkit.getWorlds());
                        }
                        break;
                }
        save();
    }
    
    /**
     * Clears list of replaced blocks
     * - called when server is stopped/reloaded or plugin is disabled
     * @see org.bukkit.plugin.Plugin#onDisable()
     */ @Override
    public final void onDisable() {
        rules.clear();
        worlds.clear();
    }
    
    /**
     * Pseudo event handler
     * - called when somebody/something calls any command attached to this plugin in plugin.yml
     * - outputs paginated list of replaces
     * @param sender Source of the command - currently not used
     * @param cmd Command which was executed - currently not used
     * @param label Alias of the command which was used
     * @param args Passed command arguments - using 1st parameter as page number
     * @return Always true to prevent displaying ugly error message
     * @see org.bukkit.plugin.Plugin#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
     */ @Override @SuppressWarnings({"null"})
    public final boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        boolean player = sender instanceof Player;
        switch((args.length == 0) ? "" : args[0]) {
            case "?":
            case "help":
                switch((args.length == 1) ? "" : args[1]) {
                    case "":
                        //<editor-fold defaultstate="collapsed" desc="LavaWater help: ...">
                            if(player)
                                sender.sendMessage(
                                        "§6§lLava§3§lWater §f§lhelp:\n" +
                                                "§7§o/" + label + " help [§ncommand§7§o]\n" +
                                                "§f   Display this help.\n" +
                                                "§7§o/" + label + " list [§npage§7§o]\n" +
                                                "§f   List generator rules.\n" +
                                                "§7§o/" + label + " add §ninput§7§o §noutput§7§o [nowater]\n" +
                                                "§f   Add new rule and save changes to disk.\n" +
                                                "§7§o/" + label + " remove §ninput-block§7§o | #§nid§7§o\n" +
                                                "§f   Remove a rule and save changes to disk."
                                );
                            else
                                sender.sendMessage(
                                        "LavaWater help:\n" +
                                                label + " help [<command>]\n" +
                                                "   Display this help.\n" +
                                                label + " list [<page>]\n" +
                                                "   List all rules.\n" +
                                                label + " add <input> <output> [nowater]\n" +
                                                "   Add new rule and save changes to disk.\n" +
                                                label + " remove <input-block> | #<id>\n" +
                                                "   Remove a rule and save changes to disk."
                                );
                        //</editor-fold>
                        return true;
                    case "list":
                        //<editor-fold defaultstate="collapsed" desc="LavaWater help: list ...">
                            if(player)
                                sender.sendMessage(
                                        "§6§lLava§3§lWater §f§lhelp: §flist\n" +
                                                "   List generator rules.\n" +
                                                "§7§o/" + label + " list\n" +
                                                "§f   Same as: /" + label + " list 1\n" +
                                                "§7§o/" + label + " list *\n" +
                                                "§f   See whole list (may overflow chat window).\n" +
                                                "§7§o/" + label + " list §npage§7§o\n" +
                                                "§f   See part of list, divided into pages of 8 rules."
                                );
                            else
                                sender.sendMessage(
                                        "LavaWater help: list\n" +
                                                "   List generator rules.\n" +
                                                label + " list\n" +
                                                "   Same as: " + label + " list 1\n" +
                                                label + " list *\n" +
                                                "   See whole list (may overflow console window).\n" +
                                                label + " list <page>\n" +
                                                "   See part of list, divided into pages of 8 rules."
                                );
                        //</editor-fold>
                        return true;
                    case "add":
                        //<editor-fold defaultstate="collapsed" desc="LavaWater help: add ...">
                            if(player)
                                sender.sendMessage(
                                        "§6§lLava§3§lWater §f§lhelp: §fadd\n" +
                                                "§f   Add new rule and save changes to disk.\n" +
                                                "§7§o/" + label + " add §ninput-type§7§o §noutput-type§7§o\n" +
                                                "§f   Creates rule, which applies whenever lava flows too close\n" +
                                                "§f   to a water block, flooding block of type §ninput-type§f.\n" +
                                                "§f   A block of type §noutput-type§f is created.\n" +
                                                "§7§o/" + label + " add §ninput-type§7§o §noutput-type§7§o nowater\n" +
                                                "§f   Creates rule, which applies whenever lava floods a block of\n" +
                                                "§f   type §ninput-type§f. A block of type §noutput-type§f is created.\n" +
                                                "§f   Do not replace the §onowater§f keyword with anything else."
                                );
                            else
                                sender.sendMessage(
                                        "LavaWater help: add\n" +
                                                "   Add new rule and save changes to disk.\n" +
                                                label + " add <input-type> <output-type>\n" +
                                                "   Creates rule, which applies whenever lava flows too close\n" +
                                                "   to a water block, flooding block of type <input-type>.\n" +
                                                "   A block of type <output-type> is created.\n" +
                                                label + " add <input-type> <output-type> nowater\n" +
                                                "   Creates rule, which applies whenever lava floods a block of\\n" +
                                                "   type <input-type>. A block of type <output-type> is created.\n" +
                                                "   Do not replace the \"nowater\" keyword with anything else."
                                );
                        //</editor-fold>
                        return true;
                    case "remove":
                        //<editor-fold defaultstate="collapsed" desc="LavaWater help: remove ...">
                            if(player)
                                sender.sendMessage(
                                        "§6§lLava§3§lWater §f§lhelp: §fremove\n" +
                                                "§f   Remove a rule and save changes to disk.\n" +
                                                "§7§o/" + label + " remove §ntype§7§o\n" +
                                                "§f   Removes first rule, which has set input (sacrifice) to §ntype§f.\n" +
                                                "§7§o/" + label + " remove #§nid§7§o\n" +
                                                "§f   Removes rule with specified §nid§f. You can\n" +
                                                "§f   read ID in \"/" + label + " list\", after '#' character."
                                );
                            else
                                sender.sendMessage(
                                        "LavaWater help: remove\n" +
                                                "   Remove a rule and save changes to disk.\n" +
                                                label + " remove <type>\n" +
                                                "   Removes first rule, which has set input (sacrifice) to <type>.\n" +
                                                label + " remove #<id>\n" +
                                                "§f   Removes rule with specified <id>. You can\n" +
                                                "§f   read ID in \"" + label + " list\", after '#' character."
                                );
                        //</editor-fold>
                        return true;
                    default:
                        sender.sendMessage((player ? "§cNo such page: " : "No such page: ") + args[1]);
                        return true;
                }
            case "list":
                if(sender.hasPermission("lavawater.command.list"))
                    try {
                        if(rules.isEmpty()) {
                            if(player)
                                sender.sendMessage(
                                        "§6§lLava§3§lWater§f§l:\n" +
                                                "§c   No rules added yet. Use /" + label + " add\n" +
                                                "§c   or edit the config YAML file. It should be\n" +
                                                "§c   placed in <server dir>/plugins/LavaWater/config.yml"
                                );
                            else
                                sender.sendMessage(
                                        "LavaWater:\n" +
                                                "   No rules added yet. Use " + label + " add\n" +
                                                "   or edit the config YAML file. It should be\n" +
                                                "   placed in <server dir>/plugins/LavaWater/config.yml"
                                );
                            return true;
                        }
                        int
                                size = rules.size(),
                                maxpage = (int)((float)size / 8 + 0.875),
                                page = (args.length == 1) ? 1 : Integer.parseInt(args[1]),
                                offset = page * 8 - 8,
                                last = ((offset + 8) < size) ? offset + 8 : size; // first that is NOT included
                        if(offset < 0 || offset >= size) {
                            sender.sendMessage((player ? "§cNo such page: " : "No such page: ") + args[1]);
                            if(maxpage == 1)
                                sender.sendMessage(player ? "§c   There is only page 1" : "   There is only page 1");
                            else
                                sender.sendMessage((player ? "§c   See pages 1 - " : "   See pages 1 - ") + maxpage);
                            return true;
                        }
                        if(player) {
                            sender.sendMessage(
                                    "§6§lLava§3§lWater §f§lrules: §fpage " + page +
                                            " of " + maxpage +
                                            " (entries " + (offset + 1) +
                                            " - " + last +
                                            " of " + size +
                                            ")"
                                    );
                            for(int i = offset; i < last; i++)
                                sender.sendMessage(rules.get(i).coloredText);
                            sender.sendMessage("§6-§3-§6-§3-");
                        } else {
                            sender.sendMessage(
                                    "LavaWater rules: page " + page +
                                            " of " + maxpage +
                                            " (entries " + (offset + 1) +
                                            " - " + last +
                                            " of " + size +
                                            ")"
                                    );
                            for(int i = offset; i < last; i++)
                                sender.sendMessage(rules.get(i).text);
                            sender.sendMessage("----");
                        }
                    } catch(NumberFormatException ex) {
                        if(args[1].equals("*"))
                            if(player) {
                                sender.sendMessage("§6§lLava§3§lWater §f§lrules:");
                                for(Rule rule : rules)
                                    sender.sendMessage(rule.coloredText);
                                sender.sendMessage("§6-§3-§6-§3-");
                            } else {
                                sender.sendMessage("LavaWater rules:");
                                for(Rule rule : rules)
                                    sender.sendMessage(rule.text);
                                sender.sendMessage("----");
                            }
                        else
                            sender.sendMessage((player ? "§cNo such page: " : "No such page: ") + args[1]);
                    }
                else
                    sender.sendMessage(player ? "§cYou don't have permission" : "You don't have permission");
                break;
            case "add":
                if(sender.hasPermission("lavawater.command.add")) {
                    boolean noWater = (args.length == 4);
                    if(noWater && !args[3].equalsIgnoreCase("nowater")) {
                        sender.sendMessage((player ? "§cUsage: /" : "Usage: ") + label + " add <input> <output> [nowater]");
                        break;
                    }
                    if(noWater || args.length == 3)
                        try {
                            Rule rule = new Rule(
                                    args[1],
                                    args[2],
                                    noWater ? "false" : "true",
                                    Collections.EMPTY_LIST,
                                    null
                            );
                            addRule(rule);
                            sender.sendMessage(player ? "§aAdded: " + rule.coloredText : ("Added: " + rule.text));
                        } catch(IllegalArgumentException ex) {
                            sender.sendMessage((player ? "§cError: " : "Error: ") + ex.getMessage());
                        }
                }
                else
                    sender.sendMessage(player ? "§cYou don't have permission" : "You don't have permission");
                break;
            case "remove":
                if(sender.hasPermission("lavawater.command.remove")) {
                    if(args.length != 2) {
                        sender.sendMessage((player ? "§cUsage: /" : "Usage: ") + label + " remove <input-block or #id>");
                        break;
                    }
                    if(args[1].charAt(0) == '#')
                        try {
                            int id = Integer.parseInt(args[1].substring(1));
                            for(Rule rule : rules)
                                if(rule.id == id) {
                                    rules.remove(rule);
                                    save();
                                    sender.sendMessage(player ? "§aRemoved: " + rule.coloredText : "Removed: " + rule.text);
                                    return true;
                                }
                            sender.sendMessage((player ? "§cCouldn't find " : "Couldn't find ") + args[1]);
                        } catch(NumberFormatException ex) {
                            sender.sendMessage((player ? "§cCouldn't find " : "Couldn't find ") + args[1]);
                        }
                    else
                        try {
                            Material material = GetByName.material(args[1]);
                            for(Rule rule : rules)
                                if(rule.input == material) {
                                    rules.remove(rule);
                                    save();
                                    sender.sendMessage(player ? "§aRemoved: " + rule.coloredText : "Removed: " + rule.text);
                                    return true;
                                }
                            sender.sendMessage((player ? "§cCouldn't find " : "Couldn't find ") + args[1]);
                        } catch(IllegalArgumentException ex) {
                            sender.sendMessage((player ? "§cCouldn't find " : "Couldn't find ") + args[1]);
                        }
                } else
                    sender.sendMessage(player ? "§cYou don't have permission" : "You don't have permission");
                break;
            default:
                sender.sendMessage((player ? "§cUsage: /" : "Usage: ") + label + " help|list|add|remove [...]");
        }
        return true;
    }
    
    /**
     * Event handler - called when a liquid block spreads (flows from a source)
     * @param e Event object given by server.
     */ @EventHandler
    public final void onBlockFromTo(BlockFromToEvent e) {
        if(e.isCancelled())
            return;
        Block toBlock = e.getToBlock();
        if(!worlds.contains(toBlock.getWorld()))
            return;
        Material from = e.getBlock().getType(), to = toBlock.getType();
        if((from == LAVA || from == STATIONARY_LAVA)) {
            Material
                    west=toBlock.getRelative(WEST).getType(),
                    east=toBlock.getRelative(EAST).getType(),
                    north=toBlock.getRelative(NORTH).getType(),
                    south=toBlock.getRelative(SOUTH).getType();
            boolean haveWater = 
                    west == WATER || west == STATIONARY_WATER ||
                    east == WATER || east == STATIONARY_WATER ||
                    north == WATER || north == STATIONARY_WATER ||
                    south == WATER || south == STATIONARY_WATER;
            for(Rule rule : rules)
                if(to == rule.input && (haveWater || !rule.needWater) && rule.appliesTo(toBlock.getLocation())) {
                    toBlock.setType(rule.output);
                    e.setCancelled(true);
                    return;
                }
        }
    }
    
    /**
     * Adds a rule to list and deletes old ones with same input block
     * @param rule New rule to add
     */
    public final void addRule(Rule rule) {
        for(Iterator<Rule> iterator = rules.iterator(); iterator.hasNext(); )
            if(iterator.next().input == rule.input)
                iterator.remove();
        rules.add(rule);
        save();
    }
    
    /**
     * Called after configuration change
     */
    private void save() {
        FileConfiguration config = getConfig();
        for(String key : config.getKeys(false))
            config.set(key, null);
        for(Rule rule : rules) {
            config.set("rules." + rule.inputString + ".result", rule.outputString);
            config.set("rules." + rule.inputString + ".needwater", rule.needWater ? "true" : "false");
            config.set("rules." + rule.inputString + ".area", rule.areasList());
        }
        ArrayList<String> list = new ArrayList(worlds.size());
        for(World world : worlds)
            list.add(world.getUID().toString());
        config.set("worlds", list);
        saveConfig();
    }
}
