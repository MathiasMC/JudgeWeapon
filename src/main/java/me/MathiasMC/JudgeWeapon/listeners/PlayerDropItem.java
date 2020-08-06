package me.MathiasMC.JudgeWeapon.listeners;

import me.MathiasMC.JudgeWeapon.JudgeWeapon;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class PlayerDropItem implements Listener {

    private final JudgeWeapon plugin;

    public PlayerDropItem(final JudgeWeapon plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onDrop(PlayerDropItemEvent e) {
        final Player player = e.getPlayer();
        final ItemStack itemStack = e.getItemDrop().getItemStack();
        if (itemStack.getItemMeta() != null) {
            final String uuid = itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "judgeweapon_uuid"), PersistentDataType.STRING);
            if (uuid != null) {
                if (player.getUniqueId().toString().equalsIgnoreCase(uuid)) {
                    plugin.runCommands(player, player.getLocation(), plugin.config.get.getStringList("drop-owner"));
                } else {
                    plugin.runCommands(player, player.getLocation(), plugin.config.get.getStringList("drop-other"));
                }
            }
        }
    }
}
