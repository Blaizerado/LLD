package at.ltd.gungame.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import at.ltd.Main;
import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.sql.sqlutils.SQLCollection;
import at.ltd.gungame.ranks.GRank;
import at.ltd.gungame.ranks.GRankPerm;
import at.ltd.gungame.ranks.GRankPermission;
import at.ltd.gungame.ranks.GRankUtils;

public class ChatEvent implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onChat(AsyncPlayerChatEvent e) {
		if (e.isCancelled()) {
			return;
		}
		Player p = e.getPlayer();
		if (e.getMessage().contains("%")) {
			p.sendMessage(Main.getPrefix() + "Das Zeichen: '%' ist nicht erlaubt! (A known java bug.)");
			e.setCancelled(true);
			return;
		}
		if (e.getMessage().contains("[STATUSBAR]")) {
			p.sendMessage(Main.getPrefix() + "[STATUSBAR] ist nicht erlaubt!");
			e.setCancelled(true);
			return;
		}
		try {
			Integer kills = SQLCollection.getPlayer(p).getKills();
			Integer ggLevel = GamePlayer.getGamePlayers().get(p).getGunGameLevel();
			GRank gk = GRankUtils.getRank(kills);
			if (GRankPerm.isTeamMember(e.getPlayer())) {
				e.setFormat(GRankUtils.getFormat(GRankPermission.get(GRankPerm.getRank(p)), gk, p.getName(), GRankPerm.getRank(p), ggLevel, ChatColor.translateAlternateColorCodes('&', e.getMessage()), p));
			} else {
				e.setFormat(GRankUtils.getFormat(GRankPermission.get(GRankPerm.getRank(p)), gk, p.getName(), GRankPerm.getRank(p), ggLevel, e.getMessage(), p));
			}

		} catch (Exception e2) {
			e.setCancelled(true);
			p.sendMessage(Main.getPrefix() + "Ein fehler trat auf.");
			e2.printStackTrace();
		}

	}

}
