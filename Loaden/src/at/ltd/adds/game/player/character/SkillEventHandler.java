package at.ltd.adds.game.player.character;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.bansystem.events.custom.EventPlayerPreJoinAsync;
import at.ltd.adds.sql.SQLPlayer;
import at.ltd.events.custom.EventPlayerQuitAsync;
import at.ltd.gungame.events.custom.EventGunGameDeath;
import at.ltd.gungame.events.custom.EventGunGameWinner;
import at.ltd.gungame.ranks.GRank;
import at.ltd.gungame.ranks.GRankUtils;

public class SkillEventHandler implements Listener {

	@EventHandler
	public void on(EventGunGameWinner e) {
		Player p = e.getPlayer();
		SQLPlayer sqlP = Main.getSQLPlayer(p);
		sqlP.setSkillPoints(sqlP.getSkillPoints() + 1);
		Cf.rsS(Cf.WINNING_ROUND, p);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void on(EventGunGameDeath e) {
		Player p = e.getKiller();
		SQLPlayer sqlP = Main.getSQLPlayer(p);
		int now = sqlP.getKills() + 1;
		int future = sqlP.getKills() + 2;
		if (!GRankUtils.getRank(now).equals(GRankUtils.getRank(future))) {
			GRank furank = GRankUtils.getRank(future);
			String msg = Cf.rs(Cf.NEW_LEVEL, p, "[RANG]", furank.getName() + " " + furank.getSymbol(), "[NAME]",
					p.getName());
			Main.broadcastMessageRound(msg);
			Cf.rsS(Cf.RANK_UP_SKILL_POINT, p);
			sqlP.setSkillPoints(sqlP.getSkillPoints() + 1);
		}

	}

	@EventHandler()
	public void on(EventPlayerPreJoinAsync e) {
		new SkillData(e.getUUID().toString());
	}

	@EventHandler
	public void on(EventPlayerQuitAsync e) {
		SkillData.removeData(e.getPlayer());
	}

}
