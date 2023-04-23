package com.autumnstudios.plugins.fracture.systems.fractureviewer;

import com.autumnstudios.plugins.fracture.Fracture;
import com.autumnstudios.plugins.record.api.chat.QuickChat;
import com.autumnstudios.plugins.record.api.sound.QuickPlay;
import com.autumnstudios.plugins.record.api.sound.QuickSound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.EventListener;

public class FractureGUIEvents implements Listener {

    FractureViewer viewer;
    Fracture main;

    public FractureGUIEvents(FractureViewer viewer, Fracture main) {
        this.viewer = viewer;
        this.main = main;
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {

        Inventory inv = event.getInventory();
        InventoryView view = event.getView();
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (view.getTitle().contains(QuickChat.colorMSG("&aFracture Viewer"))) {
            event.setCancelled(true);
            if (clickedItem.getItemMeta().getDisplayName().equals(viewer.back.getItemMeta().getDisplayName())) {
                player.closeInventory();
                viewer.showGUI();
                return;
            }
            String pathPreCheck = clickedItem.getItemMeta().getLore().get(0);
            pathPreCheck = pathPreCheck.replaceAll("&r", "");
            pathPreCheck = pathPreCheck.replaceAll("&f", "");
            File fileCheck = new File(pathPreCheck);

            if (clickedItem.getItemMeta().getDisplayName().equals(viewer.download.getItemMeta().getDisplayName())) {
                FractureDownload downloader = new FractureDownload(main);
                player.closeInventory();
                String path = clickedItem.getItemMeta().getLore().get(0);
                FracDownloaderPair downloaderPair = new FracDownloaderPair(player, downloader, path);
                FractureRuntimeStorage.runtimeStorage.put(player, downloaderPair);
                player.sendMessage(QuickChat.colorMSG("&aEnter the URL to download from!"));
                QuickPlay.play(QuickSound.CLICK, player);
                return;
            }
            if (clickedItem.getItemMeta().getDisplayName().equals(viewer.create.getItemMeta().getDisplayName())) {
                player.closeInventory();
                String path = clickedItem.getItemMeta().getLore().get(0);
                FractureRuntimeStorage.runtimeStorageCreation.put(player, path);
                player.sendMessage(QuickChat.colorMSG("&aEnter the name for the folder!"));
                QuickPlay.play(QuickSound.CLICK, player);
                return;
            }

            if (clickedItem.getItemMeta().getDisplayName().contains("(File)")) {
                if (event.isRightClick()) {
                    player.closeInventory();
                    String path = clickedItem.getItemMeta().getLore().get(0);
                    path = path.replaceAll("&r", "");
                    File fileToRead = new File(path);
                    readFile(fileToRead, player);
                    return;
                } else if (event.isLeftClick()) {

                    String path = clickedItem.getItemMeta().getLore().get(0);
                    path = path.replaceAll("&r", "");
                    File fileToDelete = new File(path);

                    if (fileToDelete.isDirectory()) {
                        if (!(fileToDelete.listFiles() == null)) {
                            player.sendMessage(QuickChat.colorMSG("&c&lYOU CANNOT DELETE FOLDERS WITH FILES INSIDE"));
                            return;
                        }
                    }

                    if (fileToDelete.getName().endsWith(".yml")) {
                        player.sendMessage(QuickChat.colorMSG("&c&lYOU CANNOT DELETE CONFIGURATION FILES"));
                        return;
                    }
                    if (fileToDelete.getName().endsWith(".jar")) {
                        player.sendMessage(QuickChat.colorMSG("&c&lYOU CANNOT DELETE PLUGINS"));
                        return;
                    }
                    if (fileToDelete.getName().endsWith(".sqlite")) {
                        player.sendMessage(QuickChat.colorMSG("&c&lYOU CANNOT DELETE DATABASES"));
                        return;
                    }
                    if (fileToDelete.getName().endsWith(".csv")) {
                        player.sendMessage(QuickChat.colorMSG("&c&lYOU CANNOT DELETE DATABASES"));
                        return;
                    }

                    if (clickedItem.getItemMeta().getDisplayName().contains("Click again to delete")) {
                        boolean deleted = fileToDelete.delete();
                        ItemStack stack = viewer.generateI(Material.BEDROCK, QuickChat.colorMSG("&cDeleted File"), true, "");
                        inv.setItem(event.getSlot(), stack);
                        if (deleted) {
                            player.sendMessage(QuickChat.colorMSG("&cDeleted!"));
                        } else {
                            player.sendMessage(QuickChat.colorMSG("&c&lCOULD NOT DELETE"));
                        }
                        return;
                    } else {
                        try {
                            ItemStack stack = viewer.generateI(Material.REDSTONE_BLOCK, QuickChat.colorMSG("&a" + fileToDelete.getName() + " &7(File) &8| &cClick again to delete"), true, fileToDelete.getCanonicalPath());
                            inv.setItem(event.getSlot(), stack);
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
                return;
            }
            player.closeInventory();
            if (clickedItem.getItemMeta().getDisplayName().contains("(Plugin)")) {
                return;
            }
            if (fileCheck.isDirectory()) {

                String path = clickedItem.getItemMeta().getLore().get(0);
                path = path.replaceAll("&r", "");
                path = path.replaceAll("&f", "");


                viewer.showGUI(path);
                return;
            }

        }
    }

    private void readFile(File file, Player player) {
        try {
            player.sendMessage(QuickChat.colorMSG("&d&lReading File " + file.getName()));
            FileInputStream fstream = new FileInputStream(file);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null)   {
                // Print the content on the console
                player.sendMessage(QuickChat.colorMSG("&a" + strLine));
            }

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
