package me.MathiasMC.JudgeWeapon.files;

import me.MathiasMC.JudgeWeapon.JudgeWeapon;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Data {

    public FileConfiguration get;
    private final File file;

    private final JudgeWeapon plugin;

    public Data(final JudgeWeapon plugin) {
        this.plugin = plugin;
        file = new File(plugin.getDataFolder(), "data.yml");
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    plugin.copy("data.yml", file);
                    plugin.textUtils.info("data.yml ( A change was made )");
                } else {
                    plugin.textUtils.info("data.yml ( Could not create file )");
                }
            } catch (IOException e) {
                plugin.textUtils.exception(e.getStackTrace(), e.getMessage());
            }
        } else {
            plugin.textUtils.info("data.yml ( Loaded )");
        }
        load();
    }

    public void load() {
        get = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            get.save(file);
        } catch (IOException exception) {
            plugin.textUtils.exception(exception.getStackTrace(), exception.getMessage());
        }
    }
}