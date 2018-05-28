package at.ltd.gungame.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import at.ltd.adds.Cf;
import at.ltd.adds.sql.SQLPlayer;
import at.ltd.adds.sql.sqlutils.SQLCollection;
import at.ltd.adds.utils.visual.Particles;
import at.ltd.gungame.ranks.GRank;
import at.ltd.gungame.ranks.GRankUtils;

public class GunGameBroadcastEvent implements Listener {

	@EventHandler
	public void on(PlayerDeathEvent e) {
		final Player p = e.getEntity();
		e.setDeathMessage(null);
		SQLCollection.getPlayer(p).setDeaths(SQLCollection.getPlayer(p).getDeaths() + 1);
		
		if (p.getKiller() instanceof Player) {
			final Player killer = p.getKiller();
			SQLPlayer killerSQL = SQLCollection.getPlayer(killer);
			Integer kills = killerSQL.getKills();
			GRank old = GRankUtils.getRank(kills);
			kills++;
			GRank now = GRankUtils.getRank(kills);
			if (!old.getName().equals(now.getName())) {
				Particles.pat(p.getLocation());
				Bukkit.broadcastMessage(Cf.rs(Cf.NEW_LEVEL, killer, "[RANG]", now.getName()));
			}
			killerSQL.setKills(kills);
		}
	}

}
