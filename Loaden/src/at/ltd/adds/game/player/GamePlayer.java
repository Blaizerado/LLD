package at.ltd.adds.game.player;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.game.player.fac.GamePlayerLogic;
import at.ltd.adds.game.player.fac.GunGamePlayer;
import at.ltd.adds.sql.SQLPlayer;
import at.ltd.adds.sql.sqlutils.SQLCollection;
import at.ltd.adds.utils.LocUtils;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.command.NormalCommands.CommandBuildMode;
import at.ltd.gungame.GunGame;
import at.ltd.gungame.events.custom.EventGunGameCoinsAsync;
import at.ltd.gungame.events.custom.EventGunGameSpawn;
import at.ltd.gungame.events.custom.EventJoinGame;
import at.ltd.gungame.events.custom.EventLeaveGame;
import at.ltd.gungame.guns.utils.GunInterface;
import at.ltd.gungame.level.GLevel;
import at.ltd.gungame.level.GLevelUtils;
import at.ltd.gungame.maps.maputils.GameMap;
import at.ltd.lobby.LobbyUtils;

public class GamePlayer extends GamePlayerMethods implements GamePlayerLogic, GunGamePlayer {

	public GamePlayer(Player player) {
		this.player = player;
		GamePlayerMethods.addGamePlayer(player, this);
	}

	/*
	 * GENEREL
	 */
	protected volatile Player player;
	protected volatile GameMap currentGameMap = null;
	protected volatile boolean inGame = false;

	protected volatile GunInterface lastGunHit;
	protected volatile String lastDamageWeaponName;
	protected volatile GamePlayer lastDamager;

	protected volatile double health = 20.00D;
	protected volatile boolean dead = false;
	protected volatile int kills = 0;
	protected volatile int deaths = 0;
	protected volatile HashMap<Object, Object> DATA = new HashMap<>();

	/*
	 * GUN-GAME-DATA
	 */
	protected volatile int gungamelevel = 0;

	/*
	 * CONQEST-DATA
	 */

	/*
	 * TEAM-DEATHMATCH-DATA
	 */

	/*
	 * GENEREL FUNCTIONS
	 */
	public double getHealth() {
		return health;
	}

	public void setHealth(double health) {
		this.health = health;
	}

	public boolean isDead() {
		return dead;
	}

	public Player getPlayer() {
		synchronized (this) {
			return player;
		}
	}

	public boolean isInRound() {
		synchronized (this) {
			return inGame;
		}
	}

	public void setIsInRound(boolean value) {
		synchronized (this) {
			this.inGame = value;
		}
	}

	public GameMap getCurrentGameMap() {
		synchronized (this) {
			return currentGameMap;
		}
	}

	public void setGameMap(GameMap gamemap) {
		synchronized (this) {
			this.currentGameMap = gamemap;
		}
	}

	public int getKills() {
		synchronized (this) {
			return kills;
		}
	}

	public int getDeaths() {
		synchronized (this) {
			return deaths;
		}
	}

	public void setDeaths(int deaths) {
		synchronized (this) {
			this.deaths = deaths;
		}
	}

	public void setKills(int kills) {
		synchronized (this) {
			this.kills = kills;
		}
	}

	public void sendMessage(String s) {
		synchronized (this) {
			player.sendMessage(s);
		}
	}

	public GunInterface getLastGunHit() {
		synchronized (this) {
			return lastGunHit;
		}
	}

	public String getLastDamageWeaponName() {
		synchronized (this) {
			return lastDamageWeaponName;
		}
	}

	public void setLastDamageWeaponName(String lastDamageWeaponName) {
		synchronized (this) {
			this.lastDamageWeaponName = lastDamageWeaponName;
		}
	}

	public GamePlayer getLastDamager() {
		synchronized (this) {
			return lastDamager;
		}
	}

	public void setLastDamager(GamePlayer lastDamager) {
		synchronized (this) {
			this.lastDamager = lastDamager;
		}
	}

	public void setLastGunHit(GunInterface lastGunHit) {
		synchronized (this) {
			if (lastGunHit != null) {
				this.lastDamageWeaponName = lastGunHit.getName();
			}
			this.lastGunHit = lastGunHit;
		}
	}

	public GamePlayer getGamePlayer() {
		return this;
	}

	public SQLPlayer getSQLPlayer() {
		return SQLCollection.getPlayer(player);
	}

	public void resetHealth() {
		health = 20.00D;
		player.setHealth(health);
	}

	// DATA

	public boolean containsDataKey(Object key) {
		synchronized (DATA) {
			return DATA.containsKey(key);
		}
	}

	public void putData(Object key, Object value) {
		synchronized (DATA) {
			DATA.put(key, value);
		}
	}

	public void removeData(Object key) {
		synchronized (DATA) {
			DATA.remove(key);
		}
	}
	
	public Object getData(Object value) {
		synchronized (DATA) {
			return DATA.get(value);
		}
	}

	public HashMap<Object, Object> cloneDataMap() {
		synchronized (DATA) {
			return (HashMap<Object, Object>) DATA.clone();
		}
	}

	/*
	 * GUNGAME-FUNCTIONS
	 * 
	 */
	public int gotGunGameKill(Player target) {
		synchronized (this) {

			if (GamePlayer.getGamePlayer(target).getGunGameLevel() != 0) {
				kills++;
				gungamelevel = gungamelevel + 1;
			}

			if (player.getName().equals(target.getName())) {
				deaths++;
				Cf.rsS(Cf.ROUND_KILL_SELF, player);
				return gungamelevel;
			}

			final GLevel gl = GLevelUtils.getGLevel(gungamelevel);
			if (gl.getBroadcast() != null) {
				String br = Cf.rs(gl.getBroadcast(), player, "[PREFIX]", Main.getPrefix());
				GunGame.getRoundManager().getGameRound()
						.broadcastMessage(Main.getPrefix() + ChatColor.translateAlternateColorCodes('&', br));
			}

			player.setLevel(gungamelevel);

			if (player.getHealth() < 16) {
				player.setHealth(player.getHealth() + 4);
			} else {
				player.setHealth(20);
			}

			GLevelUtils.setInventory(player, gungamelevel);
			Cf.rsS(Cf.ROUND_KILL_COINS, player, "[COINSKILL]", gl.getCoins(), "[KILLED]", target.getName());
			Main.getTransaction().add("KILL", gl.getCoins(), player);
			AsyncThreadWorkers.submitWork(new Runnable() {

				@Override
				public void run() {
					EventGunGameCoinsAsync event = new EventGunGameCoinsAsync(player, gl.getCoins());
					Bukkit.getPluginManager().callEvent(event);
				}
			});

			return gungamelevel;
		}

	}

	public int gotKilledGunGame(int lvlsback, Player killer) {
		synchronized (this) {
			gungamelevel = gungamelevel - lvlsback;
			if (gungamelevel < 1) {
				gungamelevel = 1;
			}
			deaths++;

			if (killer != null) {
				if (!killer.getName().equals(player.getName())) {
					if (lastDamageWeaponName != null) {
						Cf.rsS(Cf.ROUND_GOT_KILLED_1, player, "[KILLER]", killer.getName(), "[WEAPON]",
								ChatColor.stripColor(lastDamageWeaponName));
					} else {
						Cf.rsS(Cf.ROUND_GOT_KILLED_2, player, "[KILLER]", killer.getName());
					}
				} else {
					if (gungamelevel != 1) {
						gungamelevel--;
					}
				}
			} else if (lastDamageWeaponName != null) {
				Cf.rsS(Cf.ROUND_GOT_KILLED_1, player, "[WEAPON]", ChatColor.stripColor(lastDamageWeaponName));
			}

			lastDamager = null;
			lastDamageWeaponName = null;
			lastGunHit = null;

			player.setLevel(gungamelevel);
			return gungamelevel;
		}

	}

	public boolean exitGunGame(boolean gameend) {
		synchronized (this) {
			if (!isInRound()) {
				return false;
			}

			EventLeaveGame elg = new EventLeaveGame(player, this);
			Bukkit.getPluginManager().callEvent(elg);
			if (elg.isCancelled()) {
				return false;
			}

			GunGame.getRoundManager().getGameRound().removePlayer(this);

			inGame = false;
			currentGameMap = null;

			GLevelUtils.removeItems(player);
			player.setLevel(0);
			player.setFoodLevel(20);
			resetHealth();
			dead = false;

			GunGame.getRoundManager().getGameRound().removePlayer(this);
			if (!gameend) {
				GunGame.getRoundManager().getGameRound().broadcastMessage(Cf.rs(Cf.ROUND_LEAVE_ALL, player));
				Cf.rsS(Cf.ROUND_LEAVE, player);
			}

			LobbyUtils.tpPlayerToLobby(player);

			return true;
		}

	}

	public boolean joinGunGame(boolean hasvoted) {
		synchronized (this) {
			GameMap gm = null;
			if (GunGame.getRoundManager() != null) {
				if (GunGame.getRoundManager().getGameMap() != null) {
					gm = GunGame.getRoundManager().getGameMap();
				}
			}

			EventJoinGame ejg = new EventJoinGame(player, this);
			Bukkit.getPluginManager().callEvent(ejg);
			if (ejg.isCancelled()) {
				return false;
			}

			kills = 0;
			deaths = 0;
			gungamelevel = 0;
			if (!hasvoted) {
				gungamelevel = 1;
			}
			inGame = true;
			currentGameMap = gm;
			dead = false;

			resetHealth();

			GunGame.getRoundManager().getGameRound().addPlayer(this);

			Location loc = LocUtils.getBestLocationWithoutPlayer(currentGameMap.getSpawns());
			EventGunGameSpawn event = new EventGunGameSpawn(loc, player, this);
			Bukkit.getPluginManager().callEvent(event);
			player.teleport(event.getSpawnLocation());
			player.setGameMode(GameMode.ADVENTURE);

			GLevelUtils.setInventory(player, gungamelevel);

			if (CommandBuildMode.canBuild(player)) {
				CommandBuildMode.players.remove(player.getUniqueId().toString());
			}

			if (!hasvoted) {
				GunGame.getRoundManager().getGameRound().broadcastMessage(Cf.rs(Cf.ROUND_JOIN, player));
			}

			return true;
		}

	}

	public void resetGunGameLevel() {
		synchronized (this) {
			gungamelevel = 0;
		}
	}

	public int getGunGameLevel() {
		synchronized (this) {
			return gungamelevel;
		}
	}

	public void setGunGameLevel(int gglevel) {
		synchronized (this) {
			this.gungamelevel = gglevel;
		}
	}

	public String toString() {
		synchronized (this) {
			return "Name: '" + player.getName() + "', IsInRound: '" + inGame + "'";
		}
	}

	/*
	 * Player Logic
	 */

	private static HashMap<LogicField, Boolean> LOGIC = new HashMap<>();
	static {
		LOGIC.put(LogicField.BUILD, false);
		LOGIC.put(LogicField.BUY, true);
		LOGIC.put(LogicField.JOIN_GAME, true);
		LOGIC.put(LogicField.MOVE, true);
		LOGIC.put(LogicField.SHOOT, false);
		LOGIC.put(LogicField.TALK, true);
		LOGIC.put(LogicField.USE_CUSTOM_WEAPONS, false);
	}

	@Override
	public void setLogicValue(LogicField field, boolean value) {
		synchronized (LOGIC) {
			LOGIC.remove(field);
			LOGIC.put(field, value);
		}
	}

	@Override
	public boolean canBuild() {
		synchronized (LOGIC) {
			return LOGIC.get(LogicField.BUILD);
		}
	}

	@Override
	public boolean canShoot() {
		synchronized (LOGIC) {
			return LOGIC.get(LogicField.SHOOT);
		}
	}

	@Override
	public boolean canBuy() {
		synchronized (LOGIC) {
			return LOGIC.get(LogicField.BUY);

		}
	}

	@Override
	public boolean canMove() {
		synchronized (LOGIC) {
			return LOGIC.get(LogicField.MOVE);

		}
	}

	@Override
	public boolean canJoinGame() {
		synchronized (LOGIC) {
			return LOGIC.get(LogicField.JOIN_GAME);

		}
	}

	@Override
	public boolean canTalk() {
		synchronized (LOGIC) {
			return LOGIC.get(LogicField.TALK);

		}
	}

	@Override
	public boolean canUseCustomWeapons() {
		synchronized (LOGIC) {
			return LOGIC.get(LogicField.USE_CUSTOM_WEAPONS);

		}
	}

}
