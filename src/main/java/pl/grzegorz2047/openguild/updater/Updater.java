package pl.grzegorz2047.openguild.updater;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import pl.grzegorz2047.openguild.OpenGuild;
import pl.grzegorz2047.openguild.configuration.GenConf;
import pl.grzegorz2047.openguild.utils.NewVersionChecker;

/**
 * Created by grzeg on 23.08.2017.
 */
public class Updater {

    NewVersionChecker newVersionChecker = new NewVersionChecker();

    public void checkForUpdates() {
        if (!GenConf.updater) {
            OpenGuild.getOGLogger().warning("Updater is disabled.");
        } else {
            if (isAvailable()) {
                OpenGuild.getOGLogger().info(" ");
                OpenGuild.getOGLogger().info(" ==================== UPDATER ==================== ");
                OpenGuild.getOGLogger().info("Update found! Please update your plugin to the newest version!");
                OpenGuild.getOGLogger().info("Download it from https://github.com/grzegorz2047/OpenGuild2047/releases");
                OpenGuild.getOGLogger().info(" ==================== UPDATER ==================== ");
                OpenGuild.getOGLogger().info(" ");
            } else {
                OpenGuild.getOGLogger().info("No updates found! Good job! :D");
            }
        }
    }

    private boolean isAvailable() {
        return !newVersionChecker.getVersions().contains(Bukkit.getPluginManager().getPlugin("OpenGuild").getDescription().getVersion());
    }

    public void notifyOpAboutUpdate(Player player) {
        if (player.isOp() && GenConf.updater && isAvailable()) {
            player.sendMessage(ChatColor.RED + " =============== OpenGuild UPDATER =============== ");
            if (GenConf.lang.equalsIgnoreCase("PL")) {
                player.sendMessage(ChatColor.YELLOW + "Znaleziono aktualizacje! Prosze zaktualizowac Twój plugin do najnowszej wersji!");
                player.sendMessage(ChatColor.YELLOW + "Pobierz go z https://github.com/grzegorz2047/OpenGuild2047/releases");
            } else if (GenConf.lang.equalsIgnoreCase("SV")) {
                player.sendMessage(ChatColor.YELLOW + "Uppdatering hittas! Uppdatera ditt plugin till den senaste version!");
                player.sendMessage(ChatColor.YELLOW + "Ladda ner det från https://github.com/grzegorz2047/OpenGuild2047/releases");
            } else {
                player.sendMessage(ChatColor.YELLOW + "Update found! Please update your plugin to the newest version!");
                player.sendMessage(ChatColor.YELLOW + "Download it from https://github.com/grzegorz2047/OpenGuild2047/releases");
            }
            player.sendMessage(ChatColor.RED + " =============== OpenGuild UPDATER =============== ");
        }
    }
}
