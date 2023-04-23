package com.autumnstudios.plugins.fracture.systems.fractureviewer;

import org.bukkit.entity.Player;

import java.io.File;
import java.net.URL;

public class FracDownloaderPair {

    public URL url;
    public Player player;
    public FractureDownload downloader;
    public File file;
    public String initPath;

    public FracDownloaderPair(Player player, FractureDownload downloader, String initPath) {
        this.player = player;
        this.downloader = downloader;
        this.initPath = initPath;
    }

}
