package me.MathiasMC.JudgeWeapon;

import com.google.common.io.ByteStreams;
import me.MathiasMC.JudgeWeapon.commands.JudgeWeapon_Command;
import me.MathiasMC.JudgeWeapon.commands.JudgeWeapon_TabComplete;
import me.MathiasMC.JudgeWeapon.files.Config;
import me.MathiasMC.JudgeWeapon.files.Data;
import me.MathiasMC.JudgeWeapon.files.Language;
import me.MathiasMC.JudgeWeapon.listeners.*;
import me.MathiasMC.JudgeWeapon.utils.MetricsLite;
import me.MathiasMC.JudgeWeapon.utils.TextUtils;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class JudgeWeapon extends JavaPlugin {

    public static JudgeWeapon call;

    public final ConsoleCommandSender consoleSender = Bukkit.getServer().getConsoleSender();

    public TextUtils textUtils;
    public Config config;
    public Language language;
    public Data data;

    public final HashSet<Player> damageMap = new HashSet<>();

    public void onEnable() {
        call = this;
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        textUtils = new TextUtils(this);
        config = new Config(this);
        language = new Language(this);
        data = new Data(this);

        if (config.get.getBoolean("events.EntityDamageByEntity")) {
            getServer().getPluginManager().registerEvents(new EntityDamageByEntity(this), this);
        }
        if (config.get.getBoolean("events.EntityPickupItem")) {
            getServer().getPluginManager().registerEvents(new EntityPickupItem(this), this);
        }
        if (config.get.getBoolean("events.PlayerDeath")) {
            getServer().getPluginManager().registerEvents(new PlayerDeath(this), this);
        }
        if (config.get.getBoolean("events.PlayerDropItem")) {
            getServer().getPluginManager().registerEvents(new PlayerDropItem(this), this);
        }
        if (config.get.getBoolean("events.PlayerInteract")) {
            getServer().getPluginManager().registerEvents(new PlayerInteract(this), this);
        }
        if (config.get.getBoolean("events.PlayerInteractAtEntity")) {
            getServer().getPluginManager().registerEvents(new PlayerInteractAtEntity(this), this);
        }
        if (config.get.getBoolean("events.PlayerJoin")) {
            getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        }
        if (config.get.getBoolean("events.PlayerQuit")) {
            getServer().getPluginManager().registerEvents(new PlayerQuit(this), this);
        }

        getCommand("judgeweapon").setExecutor(new JudgeWeapon_Command(this));
        getCommand("judgeweapon").setTabCompleter(new JudgeWeapon_TabComplete(this));

        damageTimer(config.get.getInt("timer"));
        new MetricsLite(this, 8437);
    }

    public void onDisable() {
        call = null;
    }

    private void damageTimer(final int delay) {
        new BukkitRunnable() {
            int time = config.get.getInt("time");
            @Override
            public void run() {
                for (Player player : damageMap) {
                    if (player.isOnline()) {
                        final Location location = player.getLocation();
                        runCommands(player, location, config.get.getStringList("commands"));
                        spawnParticle(location);
                        if (config.get.getBoolean("damage")) {
                            player.damage(config.get.getDouble("damage-amount"));
                        }
                        if (time >= config.get.getInt("time")) {
                            runCommands(player, location, config.get.getStringList("time-commands"));
                            time = 0;
                        }
                        time++;
                    }
                }
            }
        }.runTaskTimer(this, delay, delay);
    }

    public void runCommands(final Player player, final Location location, final List<String> commands) {
        for (String command : commands) {
            getServer().dispatchCommand(consoleSender, replacePlaceholders(player, command
                    .replace("{world}", player.getWorld().getName())
                    .replace("{x}", String.valueOf(location.getX()))
                    .replace("{y}", String.valueOf((location.getY() + 2)))
                    .replace("{z}", String.valueOf(location.getZ()))
            ));
        }
    }

    public void hasItem(final Player player) {
        if (config.get.getBoolean("death-scan")) {
            for (ItemStack itemStack : player.getInventory().getContents()) {
                if (itemStack != null) {
                    final ItemMeta itemMeta = itemStack.getItemMeta();
                    if (itemMeta != null) {
                        final String uuid = itemMeta.getPersistentDataContainer().get(new NamespacedKey(this, "judgeweapon_uuid"), PersistentDataType.STRING);
                        if (uuid != null && player.getUniqueId().toString().equalsIgnoreCase(uuid)) {
                            runCommands(player, player.getLocation(), config.get.getStringList("death-owner"));
                            break;
                        }
                    }
                }
            }
        }
    }

    public void checkHand(final Player player) {
        final ItemStack hand = player.getInventory().getItemInMainHand();
        final ItemStack offHand = player.getInventory().getItemInOffHand();
        if (hand.getItemMeta() != null) {
            checkItem(player, hand.getItemMeta(), true);
        }
        if (offHand.getItemMeta() != null) {
            checkItem(player, offHand.getItemMeta(), false);
        }
    }

    public void checkItem(final Player player, final ItemMeta itemMeta, final boolean main) {
        final String uuid = itemMeta.getPersistentDataContainer().get(new NamespacedKey(this, "judgeweapon_uuid"), PersistentDataType.STRING);
        if (uuid != null && !player.getUniqueId().toString().equalsIgnoreCase(uuid)) {
            if (config.get.getBoolean("remove")) {
                final PlayerInventory inventory = player.getInventory();
                if (main) {
                    inventory.setItemInMainHand(new ItemStack(Material.AIR));
                } else {
                    inventory.setItemInOffHand(new ItemStack(Material.AIR));
                }
            }
            damageMap.add(player);
            final Location location = player.getLocation();
            if (config.get.getBoolean("explode")) {
                location.getWorld().createExplosion(location.getX(), location.getY(), location.getZ(), Float.parseFloat(Objects.requireNonNull(config.get.getString("explode-power"))), config.get.getBoolean("explode-fire"), config.get.getBoolean("explode-block"));
            }
            if (config.get.getBoolean("knockback")) {
                player.setVelocity(player.getLocation().getDirection().normalize().setY(0).multiply(config.get.getDouble("knockback-power")));
            }
            if (config.get.getBoolean("damage")) {
                player.damage(config.get.getDouble("damage-amount"));
            }
            spawnParticle(player.getLocation());
            runCommands(player, location, config.get.getStringList("run"));
        }
    }

    public void spawnParticle(final Location location) {
        if (config.get.getBoolean("particle")) {
            final String material = config.get.getString("particle-material").toUpperCase();
            try {
                final BlockData blockData = Material.valueOf(material).createBlockData();
                final World world = location.getWorld();
                if (world == null) {
                    return;
                }
                world.spawnParticle(Particle.BLOCK_CRACK, location.clone().add(0, config.get.getDouble("particle-y-offset"), 0), config.get.getInt("particle-amount"), blockData);
            } catch (IllegalArgumentException e) {
                textUtils.warning("Particle material ( " + material + " ) not found.");
            }
        }
    }

    public void removePlayer(final Player player) {
        final List<String> list = data.get.getStringList("players");
        if (!list.isEmpty() && list.contains(player.getUniqueId().toString())) {
            list.remove(player.getUniqueId().toString());
            data.get.set("players", list);
            data.save();
        }
    }

    public void addPlayer(final String uuid) {
        List<String> list = new ArrayList<>();
        if (data.get.contains("players") && !data.get.getStringList("players").isEmpty()) {
            list = data.get.getStringList("players");
        }
        if (!list.contains(uuid)) {
            list.add(uuid);
            data.get.set("players", list);
            data.save();
        }
    }

    public String replacePlaceholders(final Player player, String message) {
        message = message.replace("{player}", player.getName());
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            message = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, message);
        }
        return message;
    }

    public boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        }
        return true;
    }

    public void copy(final String filename, final File file) {
        try {
            ByteStreams.copy(getResource(filename), new FileOutputStream(file));
        } catch (IOException e) {
            textUtils.exception(e.getStackTrace(), e.getMessage());
        }
    }
}