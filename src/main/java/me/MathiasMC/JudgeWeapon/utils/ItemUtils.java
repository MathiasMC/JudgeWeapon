package me.MathiasMC.JudgeWeapon.utils;

import me.MathiasMC.JudgeWeapon.JudgeWeapon;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ItemUtils {

    private final JudgeWeapon plugin;

    private Player player;

    private ItemStack itemStack;

    private ItemMeta itemMeta;

    public ItemUtils(final JudgeWeapon plugin, final Player player, final Material material) {
        this.plugin = plugin;
        this.player = player;
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemMeta() {
        itemStack.setItemMeta(itemMeta);
    }

    public void name(final String name) {
        if (name.length() > 0) {
            itemMeta.setDisplayName(plugin.replacePlaceholders(player, ChatColor.translateAlternateColorCodes('&', name)));
        }
    }

    public void lores(final List<String> lores) {
        if (!lores.isEmpty()) {
            final List<String> list = new ArrayList<>();
            for (String lore : lores) {
                list.add(plugin.replacePlaceholders(player, ChatColor.translateAlternateColorCodes('&', lore)));
            }
            itemMeta.setLore(list);
        }
    }

    public boolean enchants(final List<String> enchants) {
        for (String enchant : enchants) {
            final String[] split = enchant.split(":");
            if (split.length != 2) {
                return false;
            }
            try {
                itemMeta.addEnchant(Objects.requireNonNull(Enchantment.getByKey(NamespacedKey.minecraft(split[0]))), Integer.parseInt(split[1]), true);
           } catch (NullPointerException e) {
                return false;
            }
        }
        return true;
    }

    public boolean attributes(final List<String> attributes) {
        for (String attribute : attributes) {
            final String[] split = attribute.split(":");
            if (split.length != 3) {
                return false;
            }
            try {
                final AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "", Double.parseDouble(split[1]), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.valueOf(split[2].toUpperCase()));
                itemMeta.addAttributeModifier(Attribute.valueOf(split[0].toUpperCase()), modifier);
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return true;
    }

    public boolean hide_flags(final List<String> flags) {
        for (String flag : flags) {
            try {
                itemMeta.addItemFlags(ItemFlag.valueOf(flag.toUpperCase()));
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return true;
    }
}
