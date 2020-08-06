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

        getServer().getPluginManager().registerEvents(new PlayerInteract(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(this), this);
        getServer().getPluginManager().registerEvents(new EntityPickup(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDropItem(this), this);

        getCommand("judgeweapon").setExecutor(new JudgeWeapon_Command(this));
        getCommand("judgeweapon").setTabCompleter(new JudgeWeapon_TabComplete(this));

        damageTimer(config.get.getInt("timer")
                , config.get.getStringList("commands")
                , config.get.getStringList("time-commands")
                , config.get.getInt("time")

        );
        new MetricsLite(this, 8437);
    }

    public void onDisable() {
        call = null;
    }

    private void damageTimer(final int delay, final List<String> commands, final List<String> timeCommands, final int timeTick) {
        new BukkitRunnable() {
            int time = timeTick;
            @Override
            public void run() {
                for (Player player : damageMap) {
                    if (player.isOnline()) {
                        final Location location = player.getLocation();
                        runCommands(player, location, commands);
                        spawnParticle(location);
                        if (config.get.getBoolean("damage")) {
                            player.damage(config.get.getDouble("damage-amount"));
                        }
                        if (time >= timeTick) {
                            runCommands(player, location, timeCommands);
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