package me.MathiasMC.JudgeWeapon.utils;

import me.MathiasMC.JudgeWeapon.JudgeWeapon;
import org.bukkit.Bukkit;

import java.util.logging.Logger;

public class TextUtils {

    private final JudgeWeapon plugin;

    private final Logger logger = Bukkit.getLogger();

    private final String prefix;

    public TextUtils(final JudgeWeapon plugin) {
        this.plugin = plugin;
        this.prefix = plugin.getDescription().getName();
    }

    public void info(final String text) {
        logger.info("[" + prefix + "] " + text);
    }

    public void warning(final String text) {
        logger.warning("[" + prefix + "] " + text);
    }

    public void error(final String text) {
        logger.severe("[" + prefix + "] " + text);
    }

    public void exception(final StackTraceElement[] stackTraceElement, final String text) {
        info("(!) " + prefix + " has being encountered an error, pasting below for support (!)");
        for (StackTraceElement traceElement : stackTraceElement) {
            error(traceElement.toString());
        }
        info("Message: " + text);
        info(prefix + " version: " + plugin.getDescription().getVersion());
        info("Please report this error to me on spigot");
        info("(!) " + prefix + " (!)");
    }
}