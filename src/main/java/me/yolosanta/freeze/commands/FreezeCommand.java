package me.yolosanta.freeze.commands;

import me.yolosanta.freeze.Freeze;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FreezeCommand extends CustomCommand {

    public FreezeCommand() {
        Freeze.getFreeze().getCommand("freeze").setExecutor(this);
    }

    /**
     * Executes the given command, returning its success
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You cannot use this command from console!");
        } else {
            Player staff = (Player) sender;
            if (!staff.hasPermission("yolosanta.freeze")) {
                staff.sendMessage(ChatColor.RED + "No perms.");
            } else {
                if (args.length == 0) {
                    staff.sendMessage(ChatColor.RED + "Usage: /ss <player>");
                } else {
                    Player target = Bukkit.getPlayerExact(args[0]);
                    if (target != null) {
                        Freeze.getFreeze().toggleStatus(target, staff);
                    } else {
                        staff.sendMessage(ChatColor.RED + args[0] + " is not online or could not be found.");
                    }
                }
            }
        }
        return true;
    }
}
