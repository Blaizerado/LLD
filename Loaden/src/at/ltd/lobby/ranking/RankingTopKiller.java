package at.ltd.lobby.ranking;

import java.sql.ResultSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;

import at.ltd.adds.sql.SQLQuery;
import at.ltd.adds.sql.sqlutils.SQLCollection;
import at.ltd.adds.utils.LocUtils;
import at.ltd.adds.utils.threading.AsyncWorker;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;

public class RankingTopKiller {

	public static Location head_1;
	public static Location head_2;
	public static Location head_3;
	public static Location sign_1;
	public static Location sign_2;
	public static Location sign_3;
	public static void start() {
		head_1 = LocUtils.locationByString("48.0*134.0*-23.0*0.0*0.0*GG_Lobby");
		head_2 = LocUtils.locationByString("48.0*134.0*-22.0*0.0*0.0*GG_Lobby");
		head_3 = LocUtils.locationByString("48.0*134.0*-24.0*0.0*0.0*GG_Lobby");
		sign_1 = LocUtils.locationByString("48.0*133.0*-23.0*0.0*0.0*GG_Lobby");
		sign_2 = LocUtils.locationByString("48.0*133.0*-22.0*0.0*0.0*GG_Lobby");
		sign_3 = LocUtils.locationByString("48.0*133.0*-24.0*0.0*0.0*GG_Lobby");

	}

	public static synchronized void update() {
		if (!AsyncWorker.isAsyncWorker()) {
			throw new IllegalAccessError("Only with AsyncWorker!");
		}
		try {
			SQLQuery query = new SQLQuery(SQLCollection.getConnectionHandler());
			ResultSet rs = query.querySQL("SELECT * FROM `MC` ORDER BY `MC`.`KILLS` DESC LIMIT 3");
			rs.next();
			String name1 = (String) rs.getString("NAME");
			int kills_1 = rs.getInt("KILLS");
			rs.next();
			String name2 = (String) rs.getString("NAME");
			int kills_2 = rs.getInt("KILLS");
			rs.next();
			String name3 = (String) rs.getString("NAME");
			int kills_3 = rs.getInt("KILLS");
			AsyncThreadWorkers.submitSyncWork(new Runnable() {

				@Override
				public void run() {
					setHead(name1, head_1);
					setHead(name2, head_2);
					setHead(name3, head_3);
					setSign(name1, kills_1, 1, sign_1);
					setSign(name2, kills_2, 2, sign_2);
					setSign(name3, kills_3, 3, sign_3);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	

	/*
	 * 0x2: On a wall, facing north 0x3: On a wall, facing south 0x4: On a wall,
	 * facing east 0x5: On a wall, facing west
	 */
	public static void setHead(String skullOwner, Location loc) {
		loc.getBlock().setType(Material.AIR);
		loc.getBlock().setType(Material.SKULL);
		loc.getBlock().setTypeIdAndData(Material.SKULL.getId(), (byte) 5, true);
		Skull s = (Skull) loc.getBlock().getState();
		s.setRotation(BlockFace.EAST);
		s.setSkullType(SkullType.PLAYER);
		s.setOwner(skullOwner);
		s.update();
	}

	public static void setSign(String name, Integer kills, Integer platz, Location loc) {
		loc.getBlock().setType(Material.AIR);
		loc.getBlock().setTypeIdAndData(Material.WALL_SIGN.getId(), (byte) 5, true);
		Sign s = (Sign) loc.getBlock().getState();
		s.setLine(0, "§3#§b" + platz);
		s.setLine(1, "");
		s.setLine(2, "§4" + name);
		s.setLine(3, "§8" + kills + " §6Kills");
		s.update();
	}

}
