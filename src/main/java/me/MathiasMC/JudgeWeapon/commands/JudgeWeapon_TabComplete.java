package me.MathiasMC.JudgeWeapon.commands;

import me.MathiasMC.JudgeWeapon.JudgeWeapon;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.StringUtil;

import java.util.*;

public class JudgeWeapon_TabComplete implements TabCompleter {

    private final JudgeWeapon plugin;

    public JudgeWeapon_TabComplete(final JudgeWeapon plugin) {
        this.plugin = plugin;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            final List<String> commands = new ArrayList<>();
            final List<String> list = new ArrayList<>();

            if (player.hasPermission("judgeweapon.help")) {
                if (args.length == 1) {
                    commands.add("help");
                }
            }
            if (player.hasPermission("judgeweapon.reload")) {
                if (args.length == 1) {
                    commands.add("reload");
                } else if (args.length > 1 && args[0].equalsIgnoreCase("reload")) {
                    if (args.length == 2) {
                        commands.add("all");
                        commands.add("config");
                        commands.add("language");
                        commands.add("data");
                    }
                }
            }
            if (player.hasPermission("judgeweapon.make")) {
                if (args.length == 1) {
                    commands.add("make");
                } else if (args.length > 1 && args[0].equalsIgnoreCase("make")) {
                    if (args.length == 2) {
                        commands.add("name");
                    }
                }
            }
            if (player.hasPermission("judgeweapon.link")) {
                if (args.length == 1) {
                    commands.add("link");
                } else if (args.length > 1 && args[0].equalsIgnoreCase("link")) {
                    if (args.length == 2) {
                        final Set<String> set = Objects.requireNonNull(plugin.data.get.getConfigurationSection("")).getKeys(false);
                        set.remove("players");
                        if (!set.isEmpty()) {
                            commands.addAll(set);
                        }
                    } else if (args.length == 3) {
                        commands.addAll(getPlayers(args[2]));
                    }
                }
            }
            if (player.hasPermission("judgeweapon.unlink")) {
                if (args.length == 1) {
                    commands.add("unlink");
                } else if (args.length > 1 && args[0].equalsIgnoreCase("unlink")) {
                    if (args.length == 2) {
                        commands.addAll(getPlayers(args[1]));
                    }
                }
            }
            if (player.hasPermission("judgeweapon.message")) {
                if (args.length == 1) {
                    commands.add("message");
                } else if (args.length > 1 && args[0].equalsIgnoreCase("message")) {
                    if (args.length == 2) {
                        commands.addAll(getPlayers(args[1]));
                    } else if (args.length == 3) {
                        commands.add("text");
                    }
                }
            }
            if (player.hasPermission("judgeweapon.effect")) {
                if (args.length == 1) {
                    commands.add("effect");
                } else if (args.length > 1 && args[0].equalsIgnoreCase("effect")) {
                    if (args.length == 2) {
                        commands.add("give");
                        commands.add("clear");
                    } else if (args.length == 3) {
                        commands.addAll(getPlayers(args[2]));
                    } else if (args[1].equalsIgnoreCase("give")) {
                        if (args.length == 4) {
                            for (PotionEffectType potionEffectType : PotionEffectType.values()) {
                                commands.add(potionEffectType.getName().toLowerCase());
                            }
                        } else if (args.length == 5) {
                            commands.add("seconds");
                        } else if (args.length == 6) {
                            commands.add("amplifier");
                        } else if (args.length == 7) {
                            commands.add("ambient");
                        } else if (args.length == 8) {
                            commands.add("particles");
                        }
                    }
                }
            }
            StringUtil.copyPartialMatches(args[args.length - 1], commands, list);
            Collections.sort(list);
            return list;
        }
        return null;
    }

    private List<String> getPlayers(String startsWith) {
        final List<String> list = new ArrayList<>();
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            if (onlinePlayer.isOnline()) {
                final String name = onlinePlayer.getName();
                if (name.startsWith(startsWith)) {
                    list.add(name);
                }
            }
        }
        return list;
    }
}