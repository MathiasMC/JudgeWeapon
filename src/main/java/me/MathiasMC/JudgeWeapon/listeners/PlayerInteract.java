package me.MathiasMC.JudgeWeapon.listeners;

import me.MathiasMC.JudgeWeapon.JudgeWeapon;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class PlayerInteract implements Listener {

    private final JudgeWeapon plugin;

    public PlayerInteract(final JudgeWeapon plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onInteract(PlayerInteractEvent e) {
        final Player player = e.getPlayer();
        final ItemStack hand = player.getInventory().getItemInMainHand();
        final Action action = e.getAction();
        if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
        if (hand.getItemMeta() != null) {
            final String uuid = hand.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "judgeweapon_uuid"), PersistentDataType.STRING);
            if (uuid != null && !player.getUniqueId().toString().equalsIgnoreCase(uuid)) {
                if (plugin.config.get.getBoolean("remove")) {
                    player.getInventory().remove(hand);
                }
                plugin.damageMap.add(player);
                final Location location = player.getLocation();
                if (plugin.config.get.getBoolean("explode")) {
                    location.getWorld().createExplosion(location.getX(), location.getY(), location.getZ(), Float.parseFloat(Objects.requireNonNull(plugin.config.get.getString("explode-power"))), plugin.config.get.getBoolean("explode-fire"), plugin.config.get.getBoolean("explode-block"));
                }
                if (plugin.config.get.getBoolean("knockback")) {
                    player.setVelocity(player.getLocation().getDirection().normalize().setY(0).multiply(plugin.config.get.getDouble("knockback-power")));
                }
                if (plugin.config.get.getBoolean("damage")) {
                    player.damage(plugin.config.get.getDouble("damage-amount"));
                }
                plugin.spawnParticle(player.getLocation());
                plugin.runCommands(player, location, plugin.config.get.getStringList("run"));
            }
        }
        }
    }
}