package at.ltd.lobby.ranking;

import java.sql.ResultSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

import at.ltd.Main;
import at.ltd.adds.sql.SQLQuery;
import at.ltd.adds.sql.sqlutils.SQLCollection;
import at.ltd.adds.utils.LocUtils;
import at.ltd.adds.utils.threading.AsyncWorker;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;

public class RankingLastVoted implements Listener {

	public static Location head_1;
	public static Location head_2;
	public static Location head_3;
	public static Location sign_1;
	public static Location sign_2;
	public static Location sign_3;

	public static void start() {
		Bukkit.getServer().getPluginManager().registerEvents(new RankingLastVoted(), Main.getPlugin());
		head_1 = LocUtils.locationByString("60.0*134.0*-30.0*0.0*0.0*GG_Lobby");
		head_2 = LocUtils.locationByString("59.0*134.0*-30.0*0.0*0.0*GG_Lobby");
		head_3 = LocUtils.locationByString("61.0*134.0*-30.0*0.0*0.0*GG_Lobby");
		sign_1 = LocUtils.locationByString("60.0*133.0*-30.0*0.0*0.0*GG_Lobby");
		sign_2 = LocUtils.locationByString("59.0*133.0*-30.0*0.0*0.0*GG_Lobby");
		sign_3 = LocUtils.locationByString("61.0*133.0*-30.0*0.0*0.0*GG_Lobby");
		new AsyncWorker(new Runnable() {

			@Override
			public void run() {
				update();
			}
		}, true);

	}

	@EventHandler
	public void on(VotifierEvent e) {
		Vote v = e.getVote();
		if (Bukkit.getPlayer(v.getUsername()) != null) {
			Player p = Bukkit.getPlayer(v.getUsername());
			new AsyncWorker(new Runnable() {

				@Override
				public void run() {
					add(p);

				}
			}, true);
		}

	}

	public static synchronized void add(Player p) {
		if (!AsyncWorker.isAsyncWorker()) {
			throw new IllegalAccessError("Only with AsyncWorker!");
		}
		try {
			SQLQuery query = new SQLQuery(SQLCollection.getConnectionHandler());
			ResultSet rs = query.querySQL("SELECT MAX(ID) AS ID FROM VOTES;");
			rs.next();
			int max = rs.getInt("ID");
			max++;
			query.updateSQL("INSERT INTO `VOTES` (`UUID`, `ID`) VALUES ('" + p.getUniqueId().toString() + "', '" + max + "')");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static synchronized void update() {
		if (!AsyncWorker.isAsyncWorker()) {
			throw new IllegalAccessError("Only with AsyncWorker!");
		}
		try {
			SQLQuery query = new SQLQuery(SQLCollection.getConnectionHandler());
			ResultSet rs = query.querySQL("SELECT * FROM `VOTES` ORDER BY `VOTES`.`ID` DESC LIMIT 3");
			String uuid_1 = null;
			String uuid_2 = null;
			String uuid_3 = null;
			if (rs.next()) {
				uuid_1 = rs.getString("UUID");
			} else {
				return;
			}

			if (rs.next()) {
				uuid_2 = rs.getString("UUID");
			} else {
				return;
			}

			if (rs.next()) {
				uuid_3 = rs.getString("UUID");
			} else {
				return;
			}
			String name_1 = getName(uuid_1);
			String name_2 = getName(uuid_2);
			String name_3 = getName(uuid_3);

			AsyncThreadWorkers.submitSyncWork(new Runnable() {

				@Override
				public void run() {
					setHead(name_1, head_1);
					setHead(name_2, head_2);
					setHead(name_3, head_3);
					setSign(name_1, 1, sign_1);
					setSign(name_2, 2, sign_2);
					setSign(name_3, 3, sign_3);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String getName(String uuid) {
		try {
			SQLQuery query = new SQLQuery(SQLCollection.getConnectionHandler());
			ResultSet rs = query.querySQL("SELECT * FROM `MC` WHERE `UUID` LIKE '" + uuid + "'");
			rs.next();
			return rs.getString("NAME");
		} catch (Exception e) {
		}
		return uuid;

	}

	/*
	 * 0x2: On a wall, facing north 0x3: On a wall, facing south 0x4: On a wall,
	 * facing east 0x5: On a wall, facing west
	 */
	public static void setHead(String name, Location loc) {
		loc.getBlock().setType(Material.AIR);
		loc.getBlock().setType(Material.SKULL);
		loc.getBlock().setTypeIdAndData(Material.SKULL.getId(), (byte) 3, true);
		Skull s = (Skull) loc.getBlock().getState();
		s.setRotation(BlockFace.EAST);
		s.setSkullType(SkullType.PLAYER);
		s.setOwner(name);
		s.update();
	}

	public static void setSign(String name, Integer platz, Location loc) {
		loc.getBlock().setType(Material.AIR);
		loc.getBlock().setTypeIdAndData(Material.WALL_SIGN.getId(), (byte) 3, true);
		Sign s = (Sign) loc.getBlock().getState();
		s.setLine(0, "§3#§b" + platz);
		s.setLine(1, "");
		s.setLine(2, "§4" + name);
		s.update();
	}

}
