package me.MathiasMC.JudgeWeapon.listeners;

import me.MathiasMC.JudgeWeapon.JudgeWeapon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class PlayerJoin implements Listener {

    private final JudgeWeapon plugin;

    public PlayerJoin(final JudgeWeapon plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onJoin(PlayerJoinEvent e) {
        if (plugin.data.get.contains("players")) {
            final List<String> list = plugin.data.get.getStringList("players");
            final Player player = e.getPlayer();
            if (!list.isEmpty() && list.contains(player.getUniqueId().toString())) {
                plugin.damageMap.add(player);
            }
        }
    }
}