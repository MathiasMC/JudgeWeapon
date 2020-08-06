package me.MathiasMC.JudgeWeapon.listeners;

import me.MathiasMC.JudgeWeapon.JudgeWeapon;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class EntityPickup implements Listener {

    private final JudgeWeapon plugin;

    public EntityPickup(final JudgeWeapon plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onPickup(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            final Player player = (Player) e.getEntity();
            final ItemStack itemStack = e.getItem().getItemStack();
            if (itemStack.getItemMeta() != null) {
                final String uuid = itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "judgeweapon_uuid"), PersistentDataType.STRING);
                if (uuid != null) {
                    final Location location = player.getLocation();
                    if (!player.getUniqueId().toString().equalsIgnoreCase(uuid)) {
                        plugin.runCommands(player, location, plugin.config.get.getStringList("pickup-other"));
                    } else {
                        plugin.runCommands(player, location, plugin.config.get.getStringList("pickup-owner"));
                    }
                }
            }
        }
    }
}