package com.autumnstudios.plugins.fracture;

import com.autumnstudios.plugins.fracture.bstats.Metrics;
import com.autumnstudios.plugins.fracture.systems.fractureviewer.FractureDownloadEvents;
import com.autumnstudios.plugins.fracture.systems.fractureviewer.FractureViewerCommand;

import org.bukkit.plugin.java.JavaPlugin;

public final class Fracture extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("fractureviewer").setExecutor(new FractureViewerCommand(this));

        getServer().getPluginManager().registerEvents(new FractureDownloadEvents(), this);
        int pluginId = 18274;
        Metrics metrics = new Metrics(this, pluginId);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
