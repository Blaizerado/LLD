package at.ltd.gungame.round.vote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.utils.LocUtils;
import at.ltd.adds.utils.JsonMSG.FancyMessage;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.adds.utils.visual.InventoryAnimation;
import at.ltd.events.custom.EventTeleport;
import at.ltd.gungame.GunGame;
import at.ltd.gungame.events.custom.EventGameRoundStart;
import at.ltd.gungame.events.custom.EventJoinGame;
import at.ltd.gungame.events.custom.EventVote;
import at.ltd.gungame.events.custom.EventVoteStart;
import at.ltd.gungame.events.custom.EventVoteTick;
import at.ltd.gungame.maps.MapManager;
import at.ltd.gungame.maps.maputils.GameMap;
import at.ltd.gungame.round.RoundManager;
import at.ltd.lobby.LobbyUtils;

public class RoundVote implements Listener {

	public static boolean isVoting = false;
	public static boolean isWaitVotingTime = false;
	public static HashMap<GameMap, Integer> votes = new HashMap<>();
	public static HashMap<Player, GameMap> voters = new HashMap<>();
	public static HashMap<String, GameMap> playerUUIDs = new HashMap<>();
	public static String InvName = "§4§lMap-Vote";
	public static ArrayList<GamePlayer> PlayersLastRound = new ArrayList<>();
	public static int minToWait = 1;
	public static int countdownId;
	public static int secLeft;
	public static int votewaiter;

	private static RoundManager rm;
	private static Runnable runnableFinish;
	public static void vote(RoundManager r, Runnable finish) {
		rm = r;
		runnableFinish = finish;
		secLeft = (60 * minToWait);
		isWaitVotingTime = true;
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {

			@Override
			public void run() {
				isWaitVotingTime = false;
				isVoting = true;
				for (GameMap gm : MapManager.getMaps()) {
					votes.put(gm, 0);
				}
				for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
					new FancyMessage(Cf.rs(Cf.ROUND_VOTE_1, p)).command("/mapvote").send(p);
				}
				for (GamePlayer gg : PlayersLastRound) {
					RoundVote.vote(gg.getPlayer());
				}
				EventVoteStart evs = new EventVoteStart();
				Bukkit.getPluginManager().callEvent(evs);
				votewaiter = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {

					@Override
					public void run() {
						if (voters.size() != 2) {
							for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
								new FancyMessage(Cf.rs(Cf.ROUND_VOTE_WAIT)).command("/mapvote").send(p);
							}
						}
					}
				}, 20 * 10, 20 * 45);

			}
		}, 30 * 20);

	}

	private static void startFinalVoting(RoundManager r, Runnable finish) {
		Bukkit.getScheduler().cancelTask(votewaiter);
		countdown();
		Bukkit.broadcastMessage(Cf.rs(Cf.ROUND_VOTE_WAIT_COMPLETE));

		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {

			@Override
			public void run() {
				Bukkit.getScheduler().cancelTask(countdownId);
				Integer MaxVotes = 0;
				GameMap HighestMap = null;
				for (GameMap gm : votes.keySet()) {
					Integer vote = votes.get(gm);
					if (MaxVotes < vote) {
						HighestMap = gm;
						MaxVotes = vote;
					}
				}

				if (HighestMap == null) {
					HighestMap = MapManager.getMaps().get(0);
				}
				HighestMap.onLoad();
				isVoting = false;

				for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
					if (!voters.keySet().contains(p)) {
						new FancyMessage(Cf.rs(Cf.ROUND_VOTE_2, p, "[MAP]", HighestMap.getMapName())).command("/join").send(p);
					}
				}
				GunGame.setRoundStatus(true);
				r.setGameMap(HighestMap);
				GunGame.getRoundManager().setGameRoundStartTime(System.currentTimeMillis());
				for (Player p : voters.keySet()) {
					if (p.isOnline()) {
						GamePlayer ggp = GamePlayer.getGamePlayer(p);
						if (ggp != null) {
							ggp.joinGunGame(true);
						} else {
							try {
								p.kickPlayer("Try to reconnect.");
							} catch (Exception e) {
								System.out.println("[LTD] Catched");
								e.printStackTrace();
							}
						}
					}
				}
				for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
					GamePlayer.getGamePlayer(p).resetGunGameLevel();
				}
				finish.run();
				voters.clear();
				votes.clear();
				synchronized (playerUUIDs) {
					playerUUIDs.clear();
				}

			}
		}, (1200 * minToWait) + 1);
	}

	private static void countdown() {
		secLeft = (60 * minToWait);
		countdownId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {

			@Override
			public void run() {
				secLeft--;
				EventVoteTick evt = new EventVoteTick(secLeft);
				Bukkit.getPluginManager().callEvent(evt);
			}
		}, 20, 20);
	}

	public static void vote(Player p) {
		InventoryAnimation animation = new InventoryAnimation();
		Inventory inv = Bukkit.createInventory(null, 27, InvName);
		int slot = 0;
		for (GameMap gm : MapManager.getMaps()) {
			List<String> lore = new ArrayList<>();
			lore.add("§7Map§8:§6 " + gm.getMapName());
			ItemStack is = getItem(gm.getIcon(), gm.getMapName(), lore);
			animation.put(slot, is);
			slot++;
		}
		p.openInventory(inv);
		animation.animateRandomly(inv, null);
	}

	public static HashMap<Player, ArrayList<ItemStack>> itemssave = new HashMap<>();
	public static HashMap<Player, GameMap> gamemapdata = new HashMap<>();

	@EventHandler
	public void on(InventoryClickEvent e) {
		if (!(e.getWhoClicked() instanceof Player)) {
			return;
		}
		if (e.getInventory().getName().equals(InvName)) {
			e.setCancelled(true);
			if (e.getCurrentItem() != null) {
				if (e.getCurrentItem().hasItemMeta()) {
					if (e.getCurrentItem().getItemMeta().hasDisplayName()) {
						Player p = (Player) e.getWhoClicked();
						if (isVoting) {
							GameMap gm = MapManager.getMapByName(e.getCurrentItem().getItemMeta().getDisplayName());
							setInvAndManage(p, gm);
							p.closeInventory();
						} else {
							p.closeInventory();
							Cf.rsS(Cf.ROUND_VOTE_4, p, "[MAP]", GunGame.getRoundManager().getGameRound().getGameMap().getMapName());
						}

					}
				}

			}
		}
	}

	public static void setInvAndManage(Player p, GameMap gm) {
		Location loc = LocUtils.getLocWithoutPlayers(gm.getSpawns(), 10);
		EventTeleport et = new EventTeleport(p, loc, "MAPSELECT");
		Bukkit.getPluginManager().callEvent(et);

		if (!et.isCancelled()) {
			p.setAllowFlight(true);
			p.setFlying(true);
			p.teleport(loc);
		} else {
			p.sendMessage(Main.getPrefix() + "Error");
		}

		ArrayList<ItemStack> items = new ArrayList<>();
		for (int i = 0; i <= 8; i++) {
			items.add(p.getInventory().getItem(i));
			p.getInventory().setItem(i, new ItemStack(Material.AIR));
		}
		itemssave.put(p, items);
		gamemapdata.put(p, gm);
		p.getInventory().setItem(0, getIs(Material.BOOK_AND_QUILL, "§aJa", "§7Map: " + gm.getMapName()));
		p.getInventory().setItem(1, getIs(Material.BOOK_AND_QUILL, "§cNein", null));

	}

	public static void setHisInvAndManage(Player p, boolean tp) {
		int i = 0;
		for (ItemStack is : itemssave.get(p)) {
			p.getInventory().setItem(i, is);
			i++;
		}
		itemssave.remove(p);
		gamemapdata.remove(p);
		p.setAllowFlight(false);
		p.setFlying(false);
		if (tp) {
			LobbyUtils.tpPlayerToLobby(p);
		}
	}

	@EventHandler
	public void on(PlayerInteractEvent e) {
		if (e.getAction() == Action.LEFT_CLICK_AIR) {
			return;
		}
		if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
			return;
		}
		if (e.getAction() == Action.PHYSICAL) {
			return;
		}

		Player p = e.getPlayer();
		if (!itemssave.containsKey(p)) {
			return;
		}

		ItemStack is = p.getInventory().getItemInMainHand();
		if (is.hasItemMeta()) {
			if (is.getItemMeta().hasDisplayName()) {
				if (is.getItemMeta().getDisplayName().equals("§cNein")) {
					setHisInvAndManage(p, true);
				}
				if (is.getItemMeta().getDisplayName().equals("§aJa")) {
					String mapname = is.getItemMeta().getLore().get(0).replace("§7Map: ", "");
					GameMap gm = MapManager.getMapByName(mapname);
					setHisInvAndManage(p, true);
					manageVote(p, gm);
				}
			}
		}
		e.setCancelled(true);

	}

	@EventHandler
	public static void on(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (!itemssave.containsKey(p)) {
			return;
		}
		e.setCancelled(true);
	}

	@EventHandler
	public void on(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (!itemssave.containsKey(p)) {
			return;
		}
		setHisInvAndManage(p, true);
	}

	@EventHandler
	public void on(EventTeleport e) {
		Player p = e.getPlayer();
		if (!itemssave.containsKey(p)) {
			return;
		}
		setHisInvAndManage(p, false);
	}

	@EventHandler
	public void on(EventJoinGame e) {
		if (itemssave.containsKey(e.getPlayer())) {
			Player p = e.getPlayer();
			setHisInvAndManage(p, false);
		}
	}

	@EventHandler
	public void on1(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			if (itemssave.containsKey(e.getWhoClicked())) {
				e.setCancelled(true);
			}
		}

	}

	@EventHandler
	public void on(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (itemssave.containsKey(p)) {
			if (!e.getTo().getWorld().getName().equals(gamemapdata.get(p).getSpawns().get(1).getWorld().getName())) {
				Cf.rsS(Cf.MAPS_RUN_AWAY, p);
				setHisInvAndManage(p, true);
			}
		}
	}

	@EventHandler
	public void on(EventGameRoundStart e) {
		ArrayList<Player> gamer = e.getPlayers();
		ArrayList<Player> ar = new ArrayList<>();
		for (Player p : itemssave.keySet()) {
			ar.add(p);
		}
		for (Player p : ar) {
			if (gamer.contains(p)) {
				setHisInvAndManage(p, false);
			} else {
				setHisInvAndManage(p, true);
			}

		}

	}

	public static ItemStack getIs(Material material, String name, String... lore) {
		ItemStack is = new ItemStack(material);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		if (lore != null) {
			im.setLore(Arrays.asList(lore));
		}
		is.setItemMeta(im);
		return is;

	}

	public static void manageVote(Player p, GameMap gm) {

		EventVote ev = new EventVote(p, gm);
		Bukkit.getPluginManager().callEvent(ev);
		if (ev.isCancelled()) {
			p.closeInventory();
			return;
		}
		voters.put(p, gm);
		synchronized (playerUUIDs) {
			playerUUIDs.put(p.getUniqueId().toString(), gm);
		}
		Integer vote = votes.get(gm);
		vote++;
		votes.replace(gm, vote);
		p.closeInventory();
		Cf.rsS(Cf.ROUND_VOTE_3, p, "[MAP]", gm.getMapName());
		if (voters.size() == 2) {
			startFinalVoting(rm, runnableFinish);
		}
	}

	public static ItemStack getItem(ItemStack m, String name, List<String> lor) {
		ItemStack is = m.clone();
		ItemMeta im = is.getItemMeta();
		if (lor != null) {
			im.setLore(lor);
		}
		if (name != null) {
			im.setDisplayName(name);
		}
		is.setItemMeta(im);
		return is;

	}

	public static Boolean isVoting() {
		return isVoting;
	}

	public static void setIsVoting(Boolean isVoting) {
		RoundVote.isVoting = isVoting;
	}

	public static HashMap<GameMap, Integer> getVotes() {
		return votes;
	}

	public static void setVotes(HashMap<GameMap, Integer> votes) {
		RoundVote.votes = votes;
	}

	public static HashMap<Player, GameMap> getVoters() {
		return voters;
	}

	public static void setVoters(HashMap<Player, GameMap> voters) {
		RoundVote.voters = voters;
	}

	public static String getInvName() {
		return InvName;
	}

	public static void setInvName(String invName) {
		InvName = invName;
	}

	public static ArrayList<GamePlayer> getPlayersLastRound() {
		return PlayersLastRound;
	}

	public static void setPlayersLastRound(ArrayList<GamePlayer> playersLastRound) {
		PlayersLastRound = playersLastRound;
	}

	public static Boolean isWaitVotingTime() {
		return isWaitVotingTime;
	}

}
