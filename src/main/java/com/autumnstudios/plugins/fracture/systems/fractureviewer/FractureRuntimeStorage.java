package com.autumnstudios.plugins.fracture.systems.fractureviewer;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class FractureRuntimeStorage {
    public static HashMap<Player, FracDownloaderPair> runtimeStorage = new HashMap<Player, FracDownloaderPair>();

    public static HashMap<Player, String> runtimeStorageCreation = new HashMap<Player, String>();


}
