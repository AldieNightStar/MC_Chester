package haxidenti.chester;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Chester {
    public static Location spawnChests(World world, int radius) {
        for (int i = 0; i < 100; i++) {
            int x = rand(0, radius);
            int y = rand(0, 236);
            int z = rand(0, radius);

            Location location = new Location(world, x, y, z);
            if (isFree(location)) {
                putChest(location, inv -> {
                    getRandomItems(15).forEach(inv::addItem);
                });
                return location;
            }
        }
        return null;
    }

    static int rand(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    static boolean isFree(Location location) {
        return location.getBlock().isEmpty();
    }

    static void putChest(Location location, Consumer<Inventory> invPut) {
        Block block = location.getBlock();
        block.setType(Material.CHEST);
        Chest chest = (Chest) block.getState();
        invPut.accept(chest.getBlockInventory());
    }

    static void sendPlayerMessageSpawnInfo(Player player, Location location) {
        Colored colored = new Colored(ChatColor.AQUA);
        player.sendMessage(ChatColor.AQUA +
                String.format("Chest spawned at: %s %s %s %s",
                        colored.text(location.getWorld().getName(), ChatColor.LIGHT_PURPLE),
                        colored.text("X", ChatColor.RED) + location.getBlockX(),
                        colored.text("Y", ChatColor.RED) + location.getBlockY(),
                        colored.text("Z", ChatColor.RED) + location.getBlockZ()
                )
        );
    }

    static List<ItemStack> getRandomItems(int count) {
        Material[] materials = Material.values();
        if (count < 3) count = 3;
        count = rand(2, count);
        List<ItemStack> list = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            int index = rand(0, materials.length);
            ItemStack item = new ItemStack(materials[index], 1);
            if (rand(0, 5) == 0) {
                randomEnchant(item);
            }
            list.add(item);
        }


        // Chance: 0 to 10:     add books
        if (rand(0, 10) == 0) {
            int booksCount = rand(1, 10);
            for (int i = 0; i < booksCount; i++) {
                ItemStack item = new ItemStack(Material.ENCHANTED_BOOK, 1);
                randomEnchant(item);
                list.add(item);
            }
        }

        return list;
    }

    static void randomEnchant(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return;
        }

        Enchantment[] enchantments = Enchantment.values();

        int r = rand(0, 5);

        for (int i = 0; i < r; i++) {
            meta.addEnchant(enchantments[rand(0, enchantments.length)], rand(1, 3), true);
        }

        itemStack.setItemMeta(meta);
    }

    static class Colored {
        public ChatColor standardColor;

        Colored(ChatColor c) {
            standardColor = c;
        }

        public String text(String s, ChatColor c) {
            return c + s + standardColor;
        }
    }
}
