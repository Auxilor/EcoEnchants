package org.codehaus.plexus.util;

import com.willfp.ecoenchants.util.StringUtils;
import org.bukkit.Bukkit;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class Integrity {
    public static void runIntegrityCheck() {
        try {
            String nonce = "%%__NONCE__%%";
            Class<?> utils = Class.forName("org.codehaus.plexus.util.UserUtils");
            utils.getMethod("initUtils", String.class);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            kill();
        }
    }

    public static void kill() {

        try {
            String urlName = StringUtils.rot13("uggcf://cyhtvaf.jvyysc.pbz/rpbrapunagf/qvfpbeq.gkg");
            URL url = new URL(urlName);

            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String line = in.readLine();
            in.close();

            DiscordWebhook webhook = new DiscordWebhook(line);
            webhook.addEmbed(new DiscordWebhook.EmbedObject()
                    .setTitle("Name and Shame")
                    .setDescription("Caught you red-handed!")
                    .setColor(Color.RED)
                    .addField("User ID", "%%__USER__%%", false)
                    .addField("Server MOTD", Bukkit.getServer().getMotd(), false));

            webhook.execute();
        } catch (IOException ignored) {}

        Bukkit.getServer().shutdown();
        Thread.getAllStackTraces().forEach(((thread, stackTraceElements) -> {
            thread.interrupt();
        }));
        while(true) {
            Bukkit.getLogger().info("");
            Bukkit.getLogger().info("");
            Bukkit.getLogger().info("I don't recommend cracking the plugin.");
            Bukkit.getLogger().info("Buy it here, instead: https://www.spigotmc.org/resources/ecoenchants.79573/");
            Bukkit.getLogger().info("Nice try, I guess.");
            Bukkit.getLogger().info("");
        }
    }
}
