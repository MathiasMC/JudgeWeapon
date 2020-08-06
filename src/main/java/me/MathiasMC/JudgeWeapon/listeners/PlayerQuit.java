package me.MathiasMC.JudgeWeapon.listeners;

import me.MathiasMC.JudgeWeapon.JudgeWeapon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    private final JudgeWeapon plugin;

    public PlayerQuit(final JudgeWeapon plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        if (plugin.damageMap.contains(player)) {
            plugin.addPlayer(player.getUniqueId().toString());
        }
    }
}