package me.yolosanta.freeze.listener;

import me.yolosanta.freeze.Freeze;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class FrozenListeners implements Listener {

    @EventHandler
    public void onDamageReceived(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (Freeze.getFreeze().isFrozen(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamageDealt(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (Freeze.getFreeze().isFrozen(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (Freeze.getFreeze().isFrozen(player)) {
            log(player);
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        if (Freeze.getFreeze().isFrozen(player)) {
            log(player);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();
        Location from = event.getFrom();
        if (Freeze.getFreeze().isFrozen(player)) {
            if (to.getBlockX() != from.getBlockX() || to.getBlockZ() != from.getBlockZ()) {
                event.setCancelled(true);
            }
        }
    }

    private void log(Player target) {
        for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
            if (staff.hasPermission("yolosanta.freeze")) {
                staff.sendMessage(ChatColor.RED + "[Frozen LEAVE] " + target.getName() + " has left the server whilst frozen!");
            }
        }
    }
}
