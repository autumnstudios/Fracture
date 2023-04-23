package com.autumnstudios.plugins.fracture.systems.fractureviewer;

import com.autumnstudios.plugins.fracture.Fracture;
import com.autumnstudios.plugins.record.api.chat.QuickChat;
import com.autumnstudios.plugins.record.api.recordbar.RecordBar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FractureViewer {

    Fracture main;
    Player player;
    File mainDirectory;

    ItemStack border;
    ItemStack download;
    ItemStack create;
    ItemStack back;

    public FractureViewer(Fracture main, Player player) {
        mainDirectory = main.getDataFolder().getParentFile();
        this.main = main;
        this.player = player;

        main.getServer().getPluginManager().registerEvents(new FractureGUIEvents(this, main), main);

        border = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);

        download = new ItemStack(Material.REDSTONE);
        ItemMeta downloadMeta = download.getItemMeta();
        downloadMeta.setDisplayName(ChatColor.GREEN + "Add a file! (Download)");
        downloadMeta.addEnchant(Enchantment.LOYALTY, 1, true);
        download.setItemMeta(downloadMeta);

        create = new ItemStack(Material.COMPARATOR);
        ItemMeta createMeta = download.getItemMeta();
        createMeta.setDisplayName(ChatColor.GREEN + "Create a folder");
        createMeta.addEnchant(Enchantment.LOYALTY, 1, true);
        create.setItemMeta(createMeta);

        back = new ItemStack(Material.PAPER);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(ChatColor.GREEN + "BACK");
        backMeta.addEnchant(Enchantment.LOYALTY, 1, true);
        back.setItemMeta(backMeta);
    }


    public void showGUI() {
        generateGUI("default", false, null);
    }

    public void showGUI(String extend) {
        generateGUI(extend, false, null);
    }

    public void showGUI(File file, String extend) {
        generateGUI(extend, false, file);
    }
    public void showGUI(File file, boolean deleting, String extend) {
        generateGUI(extend, true, new File(extend));
    }
    private void generateGUI(String path, boolean deleting, File file) {
        File directory = mainDirectory;
        String title = ChatColor.GREEN + "Fracture Viewer";
        if (!path.equals("default")) {
            directory = new File(path);

        }
        title = title + ChatColor.GRAY + " (" + directory.getName() + ")";
        Inventory inv = Bukkit.createInventory(null, 54, title);
        List<String> downloadlore = new ArrayList<>();
        try {
            downloadlore.add(directory.getCanonicalPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        inv.setItem(0, back);
        inv.setItem(1, border);
        inv.setItem(2, border);
        inv.setItem(3, border);

        ItemStack downloadModified = download;
        ItemMeta downloadMMeta = downloadModified.getItemMeta();
        downloadMMeta.setLore(downloadlore);
        downloadModified.setItemMeta(downloadMMeta);
        inv.setItem(4, downloadModified);

        ItemStack createModified = create;
        ItemMeta createMMeta = createModified.getItemMeta();
        createMMeta.setLore(downloadlore);
        createModified.setItemMeta(createMMeta);
        inv.setItem(5, createModified);


        inv.setItem(6, border);
        inv.setItem(7, border);
        inv.setItem(8, border);
        File[] files = directory.listFiles();
        try {
            for (File f : files) {
                if (f.isDirectory()) {
                    ItemStack itemD = generateI(Material.SPRUCE_SIGN, QuickChat.colorMSG("&b" + f.getName() + " &7(Directory)"), true, f.getCanonicalPath());
                    inv.addItem(itemD);
                } else {
                    if (f.getName().endsWith(".jar")) {
                        ItemStack itemJ = generateI(Material.DRAGON_EGG, QuickChat.colorMSG("&c" + f.getName() + " &7(Plugin)"), false, f.getCanonicalPath());
                        inv.addItem(itemJ);
                    } else {
                        if (deleting) {
                            if (f.getName().equals(file.getName())) {
                                ItemStack itemD = generateI(Material.REDSTONE_BLOCK, QuickChat.colorMSG("&a" + f.getName() + " &7(File) &8| &cClick again to delete"), true, f.getCanonicalPath());
                                inv.addItem(itemD);

                            }
                        } else {
                            if (f.getName().endsWith(".sk")) {
                                ItemStack itemSk = generateI(Material.REDSTONE_TORCH, QuickChat.colorMSG("&a" + f.getName() + " &7(File)"), true, f.getCanonicalPath());
                                inv.addItem(itemSk);

                            } else {
                                ItemStack itemS = generateI(Material.CHEST, QuickChat.colorMSG("&a" + f.getName() + " &7(File)"), false, f.getCanonicalPath());
                                inv.addItem(itemS);
                            }
                        }



                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        player.openInventory(inv);
    }



    public ItemStack generateI(Material mat, String name, boolean glowing, String lore) {
        ItemStack ie = new ItemStack(mat);
        ItemMeta meta = ie.getItemMeta();
        List<String> lores = new ArrayList<>();
        lores.add(lore);
        if (name.contains("File") || name.contains("SK")) {
            lores.add("");
            lores.add(QuickChat.colorMSG("&aRight click to read file &8&l|&r &cLeft click to delete"));
        }

        meta.setLore(lores);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        if (glowing) {meta.addEnchant(Enchantment.LOYALTY, 1, true);}
        ie.setItemMeta(meta);
        return ie;

    }


}
