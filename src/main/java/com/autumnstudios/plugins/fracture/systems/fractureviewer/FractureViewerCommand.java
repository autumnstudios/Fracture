package com.autumnstudios.plugins.fracture.systems.fractureviewer;

import com.autumnstudios.plugins.fracture.Fracture;
import com.autumnstudios.plugins.record.api.chat.QuickChat;
import com.autumnstudios.plugins.record.api.sound.QuickPlay;
import com.autumnstudios.plugins.record.api.sound.QuickSound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FractureViewerCommand implements CommandExecutor {

    Fracture main;

    public FractureViewerCommand(Fracture main) {
        this.main = main;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (!player.hasPermission("fracture.fractureviewer")) {
                QuickPlay.play(QuickSound.NO, player);
                player.sendMessage(QuickChat.colorMSG("&c&lYou do not have permission to use this"));
                QuickChat.sendActionBar(player, "&c&lYou do not have permission to use this");
                return true;
            }
            QuickPlay.play(QuickSound.YES, player);
            FractureViewer fractureViewer = new FractureViewer(main, player);
            fractureViewer.showGUI();
        }
        return false;
    }
}
