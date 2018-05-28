package at.ltd.adds.utils.visual;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Title extends JavaPlugin implements Listener {

	public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String message) {
		sendTitle(player, fadeIn, stay, fadeOut, message, "");
	}

	public static void sendSubtitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String message) {
		sendTitle(player, fadeIn, stay, fadeOut, "", message);
	}

	public static void sendFullTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
		sendTitle(player, fadeIn, stay, fadeOut, title, subtitle);
	}

	public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
		sendFT(player, fadeIn, stay, fadeOut, title, subtitle);
	}

	public static void sendTabTitle(Player player, String header, String footer)
  {
		sendFT(player, 15, 4 * 20, 15, header, footer);
  }
	public static void sendFT(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
		player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
	}


}
