package com.mycompany.managers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Simple file-backed notifications manager.
 * Each notification is stored as: username|requestId|itemCode|message
 */
public class NotificationsManager {

    private final String notificationsFile = "data/notifications.txt";

    public NotificationsManager() {
        // ensure file exists
        try {
            File f = new File(notificationsFile);
            File parent = f.getParentFile();
            if (parent != null && !parent.exists()) parent.mkdirs();
            if (!f.exists()) f.createNewFile();
        } catch (IOException e) {
            // ignore creation errors
            e.printStackTrace();
        }
    }

    public void addNotification(String username, String requestId, String itemCode, String message) {
        String line = String.join("|", username, requestId, itemCode, message.replace("\n", " "));
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(notificationsFile, true))) {
            bw.write(line);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> fetchAndClearNotifications(String username) {
        List<String> results = new ArrayList<>();
        List<String> all = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(notificationsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                all.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return results;
        }

        // iterate and collect user's notifications, keep others
        List<String> keep = new ArrayList<>();
        for (String l : all) {
            String[] parts = l.split("\\|");
            if (parts.length >= 4 && parts[0].equals(username)) {
                results.add(parts[3]);
            } else {
                keep.add(l);
            }
        }

        // rewrite file with remaining notifications
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(notificationsFile, false))) {
            for (String k : keep) {
                bw.write(k);
                bw.newLine();
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }
}
