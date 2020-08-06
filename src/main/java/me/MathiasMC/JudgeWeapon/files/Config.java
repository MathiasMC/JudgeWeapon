package me.MathiasMC.JudgeWeapon.files;

import me.MathiasMC.JudgeWeapon.JudgeWeapon;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    public FileConfiguration get;
    private final File file;

    public Config(final JudgeWeapon plugin) {
        file = new File(plugin.getDataFolder(), "config.yml");
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    plugin.copy("config.yml", file);
                    plugin.textUtils.info("config.yml ( A change was made )");
                } else {
                    plugin.textUtils.info("config.yml ( Could not create file )");
                }
            } catch (IOException e) {
                plugin.textUtils.exception(e.getStackTrace(), e.getMessage());
            }
        } else {
            plugin.textUtils.info("config.yml ( Loaded )");
        }
        load();
    }

    public void load() {
        get = YamlConfiguration.loadConfiguration(file);
    }
}