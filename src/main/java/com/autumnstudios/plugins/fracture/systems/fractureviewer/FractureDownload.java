package com.autumnstudios.plugins.fracture.systems.fractureviewer;

import com.autumnstudios.plugins.fracture.Fracture;
import com.autumnstudios.plugins.record.api.chat.QuickChat;
import com.autumnstudios.plugins.record.api.recordbar.RecordBar;
import com.autumnstudios.plugins.record.api.recordbar.RecordBarDisplayType;
import com.autumnstudios.plugins.record.api.recordbar.RecordBarType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class FractureDownload extends Thread {

    Fracture main;

    public FractureDownload(Fracture main) {
        this.main = main;
    }

    private void generatePBar(double current, RecordBar bar, String fileName, double total, Player requestSender) {


        bar.setTotal(total);

        double dCPre = current / 1048576;
        int dC = (int) Math.round(dCPre);

        bar.setCurrent(dC);
        float cR = (float) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1073741824;
        float mR = (float) Runtime.getRuntime().maxMemory() / 1073741824;
        bar.display("&aDownloading &f" + fileName + "&8| %bar%&r&bMB &8| &f" + dC + "&7/" + "&a" + total + "&bMB &r&8| &f" + cR + "&7/&f" + mR + "&cGB RAM.");

    }




    public void sendRequest(URL url, Player requestSender, File outputFile) throws Exception {


        if (outputFile.exists()) {
            requestSender.sendMessage(QuickChat.colorMSG("&e&lWARNING!&r&e This will replace the file!"));
        }


        RecordBar bar = new RecordBar(0, 0, requestSender, RecordBarType.ACTION_BAR, RecordBarDisplayType.SEPERATOR_50);


        HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
        int total = httpConnection.getContentLength() / 1048576;
        httpConnection.disconnect();
        try (BufferedInputStream in = new BufferedInputStream(url.openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
            byte dataBuffer[] = new byte[512];
            int bytesRead;
            double bytesActuallyRead = 0;
            int whileIndex = 0;
            Bukkit.getLogger().info(QuickChat.colorMSG("&aStarting downloading &f" + outputFile.getName() + "&a, total bytes of file is &f" + total));
            requestSender.sendMessage(QuickChat.colorMSG("&aStarting download!"));
            while ((bytesRead = in.read(dataBuffer, 0, 512)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
                whileIndex = whileIndex + 1;
                bytesActuallyRead = bytesActuallyRead + 512;
                if (whileIndex == 5000) {
                    generatePBar(bytesActuallyRead, bar, outputFile.getName(), total, requestSender);
                    whileIndex = 1;
                }
            }
            requestSender.sendTitle(QuickChat.colorMSG("&a&lFINISHED"), "");
        } catch (IOException e) {
            // handle exception
        }
}
}
