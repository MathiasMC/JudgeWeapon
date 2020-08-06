package me.MathiasMC.JudgeWeapon.commands;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import me.MathiasMC.JudgeWeapon.JudgeWeapon;
import me.MathiasMC.JudgeWeapon.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.regex.Pattern;

public class JudgeWeapon_Command implements CommandExecutor {

    private final JudgeWeapon plugin;

    public JudgeWeapon_Command(final JudgeWeapon plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("judgeweapon")) {
            boolean unknown = true;
            String type;
            if (sender instanceof Player) {
                type = "player";
            } else {
                type = "console";
            }
            if (sender.hasPermission("judgeweapon")) {
                if (args.length == 0) {
                    if (type.equalsIgnoreCase("player")) {
                        for (String message : plugin.language.get.getStringList("command.message")) {
                            plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName()).replace("{version}", plugin.getDescription().getVersion())));
                        }
                    } else {
                        for (String message : plugin.language.get.getStringList("console.command.message")) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("{version}", plugin.getDescription().getVersion())));
                        }
                    }
                } else {
                    if (args[0].equalsIgnoreCase("help")) {
                        unknown = false;
                        if (sender.hasPermission("judgeweapon.help")) {
                            if (type.equalsIgnoreCase("player")) {
                                for (String message : plugin.language.get.getStringList("help.message")) {
                                    plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName())));
                                }
                            } else {
                                for (String message : plugin.language.get.getStringList("console.help.message")) {
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                }
                            }
                        } else {
                            if (type.equalsIgnoreCase("player")) {
                                for (String message : plugin.language.get.getStringList("help.permission")) {
                                    plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName())));
                                }
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("reload")) {
                        unknown = false;
                        if (sender.hasPermission("judgeweapon.reload")) {
                            if (args.length == 2) {
                                if (args[1].equalsIgnoreCase("all")) {
                                    plugin.config.load();
                                    plugin.language.load();
                                    plugin.data.load();
                                    if (type.equalsIgnoreCase("player")) {
                                        for (String message : plugin.language.get.getStringList("reload.all")) {
                                            plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName())));
                                        }
                                    } else {
                                        for (String message : plugin.language.get.getStringList("console.reload.all")) {
                                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                        }
                                    }
                                } else if (args[1].equalsIgnoreCase("config")) {
                                    plugin.config.load();
                                    if (type.equalsIgnoreCase("player")) {
                                        for (String message : plugin.language.get.getStringList("reload.config")) {
                                            plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName())));
                                        }
                                    } else {
                                        for (String message : plugin.language.get.getStringList("console.reload.config")) {
                                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                        }
                                    }
                                } else if (args[1].equalsIgnoreCase("language")) {
                                    plugin.language.load();
                                    if (type.equalsIgnoreCase("player")) {
                                        for (String message : plugin.language.get.getStringList("reload.language")) {
                                            plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName())));
                                        }
                                    } else {
                                        for (String message : plugin.language.get.getStringList("console.reload.language")) {
                                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                        }
                                    }
                                } else if (args[1].equalsIgnoreCase("data")) {
                                    plugin.data.load();
                                    if (type.equalsIgnoreCase("player")) {
                                        for (String message : plugin.language.get.getStringList("reload.data")) {
                                            plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName())));
                                        }
                                    } else {
                                        for (String message : plugin.language.get.getStringList("console.data.language")) {
                                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                        }
                                    }
                                }
                            } else {
                                if (type.equalsIgnoreCase("player")) {
                                    for (String message : plugin.language.get.getStringList("reload.usage")) {
                                        plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName())));
                                    }
                                } else {
                                    for (String message : plugin.language.get.getStringList("console.reload.usage")) {
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                    }
                                }
                            }
                        } else {
                            if (type.equalsIgnoreCase("player")) {
                                for (String message : plugin.language.get.getStringList("reload.permission")) {
                                    plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName())));
                                }
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("make")) {
                        unknown = false;
                        if (sender.hasPermission("judgeweapon.make")) {
                            if (type.equalsIgnoreCase("player")) {
                                if (args.length == 2) {
                                    if (!plugin.data.get.contains(args[1])) {
                                        final Player player = (Player) sender;
                                        final ItemStack itemStack = player.getInventory().getItemInMainHand();
                                        if (!itemStack.getType().equals(Material.AIR)) {
                                            final ItemMeta itemMeta = itemStack.getItemMeta();
                                            final String path = args[1] + ".";
                                            plugin.data.get.set(path + "material", itemStack.getType().toString().toLowerCase());
                                            plugin.data.get.set(path + "name", itemMeta.getDisplayName().replace("§", "&"));
                                            final List<String> lores = new ArrayList<>();
                                            if (itemMeta.getLore() != null) {
                                                for (String lore : itemMeta.getLore()) {
                                                    lores.add(lore.replace("§", "&"));
                                                }
                                            }
                                            plugin.data.get.set(path + "lore", lores);
                                            Map<Enchantment, Integer> enchants = new HashMap<>();
                                            if (itemMeta.hasEnchants()) {
                                                enchants = itemMeta.getEnchants();
                                            }
                                            List<String> enchantList = new ArrayList<>();
                                            for (Enchantment enchantment : enchants.keySet()) {
                                                enchantList.add(enchantment.getKey().getKey().toLowerCase() + ":" + enchants.get(enchantment));
                                            }
                                            plugin.data.get.set(path + "enchantments", enchantList);
                                            Multimap<Attribute, AttributeModifier> modifierMultimap = ArrayListMultimap.create();
                                            if (itemMeta.hasAttributeModifiers()) {
                                                modifierMultimap = itemMeta.getAttributeModifiers();
                                            }
                                            final List<String> attributes = new ArrayList<>();
                                            for (Attribute attribute : modifierMultimap.keySet()) {
                                                Collection<AttributeModifier> attributeModifier = itemMeta.getAttributeModifiers(attribute);
                                                for (AttributeModifier modifier : attributeModifier) {
                                                    attributes.add(attribute.name().toLowerCase() + ":" + modifier.getAmount() + ":" + modifier.getSlot().name().toLowerCase());
                                                }
                                            }
                                            plugin.data.get.set(path + "attributes", attributes);
                                            final Set<ItemFlag> itemFlags = itemMeta.getItemFlags();
                                            List<String> itemflags = new ArrayList<>();
                                            for (ItemFlag itemflag : itemFlags) {
                                                itemflags.add(itemflag.name().toLowerCase());
                                            }
                                            plugin.data.get.set(path + "hide-flags", itemflags);
                                            plugin.data.save();
                                            plugin.data.load();
                                            for (String message : plugin.language.get.getStringList("make.created")) {
                                                plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName()).replace("{name}", args[1])));
                                            }
                                        } else {
                                            for (String message : plugin.language.get.getStringList("make.air")) {
                                                plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName())));
                                            }
                                        }
                                    } else {
                                        for (String message : plugin.language.get.getStringList("make.already")) {
                                            plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName())));
                                        }
                                    }
                                } else {
                                    for (String message : plugin.language.get.getStringList("make.usage")) {
                                        plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName())));
                                    }
                                }
                            } else {
                                for (String message : plugin.language.get.getStringList("console.make.player")) {
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                }
                            }
                        } else {
                            if (type.equalsIgnoreCase("player")) {
                                for (String message : plugin.language.get.getStringList("make.permission")) {
                                    plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName())));
                                }
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("link")) {
                        unknown = false;
                        if (sender.hasPermission("judgeweapon.link")) {
                            if (args.length == 3) {
                                if (plugin.data.get.contains(args[1]) && !args[1].equalsIgnoreCase("linked")) {
                                    final Player target = plugin.getServer().getPlayer(args[2]);
                                    if (target != null) {
                                        final ItemUtils itemStack = new ItemUtils(plugin, target, Material.valueOf(plugin.data.get.getString(args[1] + ".material").toUpperCase()));
                                        final String path = args[1] + ".";
                                        itemStack.name(plugin.data.get.getString(path + "name"));
                                        itemStack.lores(plugin.data.get.getStringList(path + "lore"));
                                        if (itemStack.enchants(plugin.data.get.getStringList(path + "enchantments")) && itemStack.attributes(plugin.data.get.getStringList(path + "attributes")) && itemStack.hide_flags(plugin.data.get.getStringList(path + "hide-flags"))) {
                                            itemStack.attributes(plugin.data.get.getStringList(path + "attributes"));
                                            itemStack.hide_flags(plugin.data.get.getStringList(path + "hide-flags"));
                                            itemStack.setItemMeta();
                                            final ItemMeta itemMeta = itemStack.getItemStack().getItemMeta();
                                            itemMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "judgeweapon_uuid"), PersistentDataType.STRING, target.getUniqueId().toString());
                                            itemStack.getItemStack().setItemMeta(itemMeta);
                                            target.getInventory().addItem(itemStack.getItemStack());
                                            if (type.equalsIgnoreCase("player")) {
                                                for (String message : plugin.language.get.getStringList("link.player")) {
                                                    plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName()).replace("{target}", target.getName())));
                                                }
                                            } else {
                                                for (String message : plugin.language.get.getStringList("console.link.player")) {
                                                    plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{target}", target.getName())));
                                                }
                                            }
                                        } else {
                                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cAn error has occurred."));
                                        }
                                    } else {
                                        if (type.equalsIgnoreCase("player")) {
                                            for (String message : plugin.language.get.getStringList("link.online")) {
                                                plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName())));
                                            }
                                        } else {
                                            for (String message : plugin.language.get.getStringList("console.link.online")) {
                                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                            }
                                        }
                                    }
                                } else {
                                    if (type.equalsIgnoreCase("player")) {
                                        for (String message : plugin.language.get.getStringList("link.found")) {
                                            plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName())));
                                        }
                                    } else {
                                        for (String message : plugin.language.get.getStringList("console.link.found")) {
                                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                        }
                                    }
                                }
                            } else {
                                if (type.equalsIgnoreCase("player")) {
                                    for (String message : plugin.language.get.getStringList("link.usage")) {
                                        plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName())));
                                    }
                                } else {
                                    for (String message : plugin.language.get.getStringList("console.link.usage")) {
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                    }
                                }
                            }
                        } else {
                            if (type.equalsIgnoreCase("player")) {
                                for (String message : plugin.language.get.getStringList("link.permission")) {
                                    plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName())));
                                }
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("unlink")) {
                        unknown = false;
                        if (sender.hasPermission("judgeweapon.unlink")) {
                            if (args.length == 2) {
                                final Player target = plugin.getServer().getPlayer(args[1]);
                                if (target != null) {
                                    plugin.damageMap.remove(target);
                                    if (plugin.data.get.contains("players")) {
                                        final List<String> list = plugin.data.get.getStringList("players");
                                        final String uuid = target.getUniqueId().toString();
                                        if (!list.isEmpty() && list.contains(uuid)) {
                                            list.remove(uuid);
                                            plugin.data.get.set("players", list);
                                            plugin.data.save();
                                        }
                                    }
                                    if (type.equalsIgnoreCase("player")) {
                                        for (String message : plugin.language.get.getStringList("unlink.player")) {
                                            plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName()).replace("{target}", target.getName())));
                                        }
                                    } else {
                                        for (String message : plugin.language.get.getStringList("console.unlink.player")) {
                                            plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{target}", target.getName())));
                                        }
                                    }

                                } else {
                                    if (type.equalsIgnoreCase("player")) {
                                        for (String message : plugin.language.get.getStringList("unlink.online")) {
                                            plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName())));
                                        }
                                    } else {
                                        for (String message : plugin.language.get.getStringList("console.unlink.online")) {
                                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                        }
                                    }
                                }
                            } else {
                                if (type.equalsIgnoreCase("player")) {
                                    for (String message : plugin.language.get.getStringList("unlink.usage")) {
                                        plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName())));
                                    }
                                } else {
                                    for (String message : plugin.language.get.getStringList("console.unlink.usage")) {
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                    }
                                }
                            }
                        } else {
                            if (type.equalsIgnoreCase("player")) {
                                for (String message : plugin.language.get.getStringList("unlink.permission")) {
                                    plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName())));
                                }
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("message")) {
                        unknown = false;
                        if (sender.hasPermission("judgeweapon.message")) {
                            if (args.length <= 2) {
                                if (type.equalsIgnoreCase("player")) {
                                    for (String message : plugin.language.get.getStringList("message.usage")) {
                                        plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName())));
                                    }
                                } else {
                                    for (String message : plugin.language.get.getStringList("console.message.usage")) {
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                    }
                                }
                            } else {
                                final Player target = plugin.getServer().getPlayer(args[1]);
                                if (target != null) {
                                    final StringBuilder sb = new StringBuilder();
                                    for (int i = 2; i < args.length; i++) {
                                        sb.append(args[i]).append(" ");
                                    }
                                    final String text = sb.toString().trim();
                                    if (!text.contains("\\n")) {
                                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.replacePlaceholders(target, text)));
                                    } else {
                                        for (String message : text.split(Pattern.quote("\\n"))) {
                                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.replacePlaceholders(target, message)));
                                        }
                                    }
                                } else {
                                    if (type.equalsIgnoreCase("player")) {
                                        for (String message : plugin.language.get.getStringList("message.online")) {
                                            plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName())));
                                        }
                                    } else {
                                        for (String message : plugin.language.get.getStringList("console.message.online")) {
                                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                        }
                                    }
                                }
                            }
                        } else {
                            if (type.equalsIgnoreCase("player")) {
                                for (String message : plugin.language.get.getStringList("message.permission")) {
                                    plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName())));
                                }
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("effect")) {
                        unknown = false;
                        if (sender.hasPermission("judgeweapon.effect")) {
                            if (args.length > 1 && args[1].equalsIgnoreCase("give") || args[1].equalsIgnoreCase("clear")) {
                                final Player target = plugin.getServer().getPlayer(args[2]);
                                if (target != null) {
                                    if (args[1].equalsIgnoreCase("give")) {
                                        if (args.length == 8) {
                                            try {
                                                if (plugin.isInt(args[4])) {
                                                    if (plugin.isInt(args[5])) {
                                                        target.addPotionEffect(new PotionEffect(Objects.requireNonNull(PotionEffectType.getByName(args[3])), Integer.parseInt(args[4]) * 20, Integer.parseInt(args[5]), Boolean.parseBoolean(args[6]), Boolean.parseBoolean(args[7])));
                                                        if (type.equalsIgnoreCase("player")) {
                                                            for (String message : plugin.language.get.getStringList("effect.give.player")) {
                                                                plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName()).replace("{effect}", args[3]).replace("{target}", target.getName())));
                                                            }
                                                        } else {
                                                            for (String message : plugin.language.get.getStringList("console.effect.give.player")) {
                                                                plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", target.getName()).replace("{effect}", args[3])));
                                                            }
                                                        }
                                                    } else {
                                                        if (type.equalsIgnoreCase("player")) {
                                                            for (String message : plugin.language.get.getStringList("effect.give.amplifier")) {
                                                                plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName())));
                                                            }
                                                        } else {
                                                            for (String message : plugin.language.get.getStringList("console.effect.give.amplifier")) {
                                                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    if (type.equalsIgnoreCase("player")) {
                                                        for (String message : plugin.language.get.getStringList("effect.give.seconds")) {
                                                            plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName())));
                                                        }
                                                    } else {
                                                        for (String message : plugin.language.get.getStringList("console.effect.give.seconds")) {
                                                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                                        }
                                                    }
                                                }
                                            } catch (NullPointerException e) {
                                                if (type.equalsIgnoreCase("player")) {
                                                    for (String message : plugin.language.get.getStringList("effect.give.found")) {
                                                        plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName())));
                                                    }
                                                } else {
                                                    for (String message : plugin.language.get.getStringList("console.effect.give.found")) {
                                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                                    }
                                                }
                                            }
                                        } else {
                                            if (type.equalsIgnoreCase("player")) {
                                                for (String message : plugin.language.get.getStringList("effect.give.usage")) {
                                                    plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName())));
                                                }
                                            } else {
                                                for (String message : plugin.language.get.getStringList("console.effect.give.usage")) {
                                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                                }
                                            }
                                        }
                                    } else if (args[1].equalsIgnoreCase("clear")) {
                                        if (args.length == 3) {
                                            for (PotionEffect potionEffect : target.getActivePotionEffects()) {
                                                target.removePotionEffect(potionEffect.getType());
                                            }
                                            if (type.equalsIgnoreCase("player")) {
                                                for (String message : plugin.language.get.getStringList("effect.clear.player")) {
                                                    plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName()).replace("{target}", target.getName())));
                                                }
                                            } else {
                                                for (String message : plugin.language.get.getStringList("console.effect.clear.player")) {
                                                    plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", target.getName())));
                                                }
                                            }
                                        } else {
                                            if (type.equalsIgnoreCase("player")) {
                                                for (String message : plugin.language.get.getStringList("effect.clear.usage")) {
                                                    plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName())));
                                                }
                                            } else {
                                                for (String message : plugin.language.get.getStringList("console.effect.clear.usage")) {
                                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (type.equalsIgnoreCase("player")) {
                                        for (String message : plugin.language.get.getStringList("effect.online")) {
                                            plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName())));
                                        }
                                    } else {
                                        for (String message : plugin.language.get.getStringList("console.effect.online")) {
                                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                        }
                                    }
                                }
                            } else {
                                if (type.equalsIgnoreCase("player")) {
                                    for (String message : plugin.language.get.getStringList("effect.usage")) {
                                        plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName())));
                                    }
                                } else {
                                    for (String message : plugin.language.get.getStringList("console.effect.usage")) {
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                    }
                                }
                            }
                        } else {
                            if (type.equalsIgnoreCase("player")) {
                                for (String message : plugin.language.get.getStringList("effect.permission")) {
                                    plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName())));
                                }
                            }
                        }
                    }
                    if (unknown) {
                        if (type.equalsIgnoreCase("player")) {
                            for (String message : plugin.language.get.getStringList("command.unknown")) {
                                plugin.getServer().dispatchCommand(plugin.consoleSender, ChatColor.translateAlternateColorCodes('&', message.replace("{player}", sender.getName()).replace("{command}", args[0])));
                            }
                        } else {
                            for (String message : plugin.language.get.getStringList("console.command.unknown")) {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("{command}", args[0])));
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}