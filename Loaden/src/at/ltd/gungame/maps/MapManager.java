package at.ltd.gungame.maps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import at.ltd.Main;
import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.game.player.GamePlayerDamage;
import at.ltd.adds.game.player.fac.BeforeDamageHandler;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.events.custom.EventPlayerMoveBlockEventAsync;
import at.ltd.gungame.events.custom.EventGameRoundStop;
import at.ltd.gungame.events.custom.EventGunGameDamage.DamageType;
import at.ltd.gungame.maps.deathmatch.GAME_01;
import at.ltd.gungame.maps.deathmatch.GAME_02;
import at.ltd.gungame.maps.deathmatch.GAME_03;
import at.ltd.gungame.maps.deathmatch.GAME_04;
import at.ltd.gungame.maps.deathmatch.GAME_05;
import at.ltd.gungame.maps.deathmatch.GAME_06;
import at.ltd.gungame.maps.deathmatch.GAME_07;
import at.ltd.gungame.maps.deathmatch.GAME_08;
import at.ltd.gungame.maps.deathmatch.GAME_09;
import at.ltd.gungame.maps.deathmatch.GAME_10;
import at.ltd.gungame.maps.deathmatch.GAME_11;
import at.ltd.gungame.maps.deathmatch.GAME_12;
import at.ltd.gungame.maps.deathmatch.GAME_13;
import at.ltd.gungame.maps.deathmatch.GAME_14;
import at.ltd.gungame.maps.deathmatch.GAME_15;
import at.ltd.gungame.maps.deathmatch.GAME_16;
import at.ltd.gungame.maps.deathmatch.GAME_17;
import at.ltd.gungame.maps.deathmatch.GAME_18;
import at.ltd.gungame.maps.maputils.DeathBorder;
import at.ltd.gungame.maps.maputils.GameMap;
import at.ltd.gungame.utils.airdrop.AirdropSpawn;

public class MapManager implements Listener {

	public static ArrayList<GameMap> MAPS = new ArrayList<>();
	public static HashMap<String, AirdropSpawn> airdrop_objects = new HashMap<>();
	public static HashMap<GameMap, DeathBorder> BORDER = new HashMap<>();

	public static void registerMap(Object gm) {
		if (!MAPS.contains(gm)) {
			MAPS.add((GameMap) gm);
			((GameMap) gm).onRegister();
			if (gm instanceof AirdropSpawn) {
				airdrop_objects.put(((GameMap) gm).getMapName(), (AirdropSpawn) gm);
			}
			if (gm instanceof DeathBorder) {
				BORDER.put((GameMap) gm, (DeathBorder) gm);
			}
		}
	}

	public static ArrayList<GameMap> getMaps() {
		return MAPS;
	}

	public static void setMaps(ArrayList<GameMap> maps) {
		MapManager.MAPS = maps;
	}

	public static GameMap getMapByName(String s) {
		GameMap gm = null;
		for (GameMap gms : MAPS) {
			if (gms.getMapName().contains(s)) {
				gm = gms;
			}
		}
		return gm;
	}

	public static void registerAllMaps() {
		Main.registerListener(new MapManager());
		MapManager.registerMap(new GAME_01());
		MapManager.registerMap(new GAME_02());
		MapManager.registerMap(new GAME_03());
		MapManager.registerMap(new GAME_04());
		MapManager.registerMap(new GAME_05());
		MapManager.registerMap(new GAME_06());
		MapManager.registerMap(new GAME_07());
		MapManager.registerMap(new GAME_08());
		MapManager.registerMap(new GAME_09());
		MapManager.registerMap(new GAME_10());
		MapManager.registerMap(new GAME_11());
		MapManager.registerMap(new GAME_12());
		MapManager.registerMap(new GAME_13());
		MapManager.registerMap(new GAME_14());
		MapManager.registerMap(new GAME_15());
		MapManager.registerMap(new GAME_16());
		MapManager.registerMap(new GAME_17());
		MapManager.registerMap(new GAME_18());
	}

	@EventHandler
	public void on(EventPlayerMoveBlockEventAsync e) {
		GamePlayer ggp = GamePlayer.getGamePlayer(e.getPlayer());
		if (ggp.isInRound()) {
			synchronized (BORDER) {
				if (BORDER.containsKey(ggp.getCurrentGameMap())) {
					DeathBorder db = (DeathBorder) ggp.getCurrentGameMap();
					int y = db.getY();
					double cury = AsyncThreadWorkers.getPlayerLocation(e.getPlayer()).getY();
					if (cury < y) {
						AsyncThreadWorkers.submitSyncWork(() -> {
							GamePlayerDamage.damagePlayer(e.getPlayer(), null, 20.00, DamageType.VOID, new BeforeDamageHandler() {

								@Override
								public void handle(GamePlayer player, boolean dead) {
								}
							}, true);
						});
					}
				}
			}
		}
	}

	@EventHandler
	public void on(EventGameRoundStop e) {
		Collections.shuffle(MAPS);
	}

}
