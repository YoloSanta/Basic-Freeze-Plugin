package me.yolosanta.freeze;

import lombok.Getter;
import me.yolosanta.freeze.commands.FreezeCommand;
import me.yolosanta.freeze.listener.FrozenListeners;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Freeze extends JavaPlugin {

    @Getter
    private static Freeze freeze;
    @Getter
    private List<UUID> frozen;

    @Override
    public void onEnable() {
        freeze = this;
        frozen = new ArrayList<>();

        /* Config Stuff */
        getConfig().options().copyDefaults(true);
        saveConfig();

        /* Listeners */
        getServer().getPluginManager().registerEvents(new FrozenListeners(), this);

        /* Enabling Commands */
        new FreezeCommand();

        /* Timers */
        initMessageTimer();
    }

    private void denyToMove(Player player) {
        player.setWalkSpeed(0.0F);
        player.setFlySpeed(0.0F);
        player.setSprinting(false);
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 200));
    }

    private void allowToMove(Player player) {
        player.setWalkSpeed(0.2F);
        player.setFlySpeed(0.0001F);
        player.setSprinting(true);
        player.removePotionEffect(PotionEffectType.JUMP);
    }

    public boolean isFrozen(Player player) {
        return frozen.contains(player.getUniqueId());
    }

    public void toggleStatus(Player player, Player staffMember) {
        if (isFrozen(player)) {
            allowToMove(player);
            frozen.remove(player.getUniqueId());
            staffMember.sendMessage(ChatColor.GREEN + player.getName() + " has been unfrozen!");
            player.sendMessage(ChatColor.GREEN + "You have been unfrozen!");
            return;
        }
        if (!isFrozen(player)) {
            denyToMove(player);
            frozen.add(player.getUniqueId());
            staffMember.sendMessage(ChatColor.RED + player.getName() + " has been frozen!");
            player.sendMessage(ChatColor.RED + "You have been frozen!");
        }
    }

    private void initMessageTimer() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for (Player allMembers : getServer().getOnlinePlayers()) {
                    if (frozen.contains(allMembers.getUniqueId())) {
                        for (String msg : getConfig().getStringList("Message")) {
                            allMembers.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        }
                    }
                }
            }
        }, 0, 200);
    }
}