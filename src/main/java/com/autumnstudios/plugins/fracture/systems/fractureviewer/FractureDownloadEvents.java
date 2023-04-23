package com.autumnstudios.plugins.fracture.systems.fractureviewer;

import com.autumnstudios.plugins.record.api.chat.QuickChat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.File;
import java.net.URL;

public class FractureDownloadEvents implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if (FractureRuntimeStorage.runtimeStorageCreation.containsKey(player)) {
            e.setCancelled(true);
            player.sendMessage(QuickChat.colorMSG("&aCreated folder!"));
            File folder = new File(FractureRuntimeStorage.runtimeStorageCreation.get(player) + "/" + e.getMessage() + "/");
            if (!folder.exists()) {
                folder.mkdir();
            }
            FractureRuntimeStorage.runtimeStorageCreation.remove(player);
            return;
        }
        if (FractureRuntimeStorage.runtimeStorage.containsKey(player)) {
            e.setCancelled(true);
            FracDownloaderPair pair = FractureRuntimeStorage.runtimeStorage.get(player);
            String initPath = pair.initPath;
            if (pair.url == null) {
                URL url;
                try {
                    url = new URL(e.getMessage());
                } catch (Exception ex) {
                    player.sendMessage(QuickChat.colorMSG("&c&lNot a proper URL!"));
                    return;
                }
                pair.url = url;
                player.sendMessage(QuickChat.colorMSG("&aURL set! Now enter the file to be saved at " + initPath));
            } else {
                pair.file = new File(initPath + "/" + e.getMessage());
                FractureRuntimeStorage.runtimeStorage.remove(player, pair);
                player.sendMessage(QuickChat.colorMSG("&aSaving file at &f" + initPath + "/" + e.getMessage()));
                try {
                    pair.downloader.sendRequest(pair.url, player, pair.file);
                } catch (Exception exx) {
                    player.sendMessage(QuickChat.colorMSG("&c&lFAILED! &r&f") + exx.getCause());
                }
            }

        }
    }
}
