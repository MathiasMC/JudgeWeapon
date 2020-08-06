package me.MathiasMC.JudgeWeapon.listeners;

import me.MathiasMC.JudgeWeapon.JudgeWeapon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {

    private final JudgeWeapon plugin;

    public PlayerDeath(final JudgeWeapon plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onDeath(PlayerDeathEvent e) {
        final Player player = e.getEntity();
        if (plugin.damageMap.contains(player)) {
            plugin.runCommands(player, player.getLocation(), plugin.config.get.getStringList("death"));
        }
        plugin.damageMap.remove(player);
        plugin.removePlayer(player);
        plugin.hasItem(player);
    }
}
