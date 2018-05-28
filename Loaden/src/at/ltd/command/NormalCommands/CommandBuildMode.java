package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.material.Cake;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.superuser.SuperUser;
import at.ltd.adds.utils.threading.AsyncAble;
import at.ltd.events.custom.EventPlayerQuitAsync;

public class CommandBuildMode implements CommandExecuter, Listener {
	public static ArrayList<String> players = new ArrayList<>();

	@AsyncAble
	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {

		if (SuperUser.isSuperUser(p.getUniqueId().toString())) {
			if (args.size() == 2) {
				Player targer = Bukkit.getPlayer(args.get(1));
				if (targer == null) {
					p.sendMessage(Main.getPrefix() + "Spieler nicht gefunden!");
					return;
				}
				String uuid = targer.getUniqueId().toString();
				if (canBuild(targer)) {
					targer.sendMessage(Main.getPrefix() + "Du wurdest aus dem BuildMode entfernt!");
					p.sendMessage(Main.getPrefix() + "Er kann nun nicht mehr Bauen.");
					synchronized (players) {
						players.remove(uuid);
					}
				} else {
					synchronized (players) {
						players.add(uuid);
					}

					targer.setGameMode(GameMode.CREATIVE);
					targer.sendMessage(Main.getPrefix() + "Du kannst nun Bauen!");
					p.sendMessage(Main.getPrefix() + "Er kann nun Bauen.");
				}
			} else {
				String uuid = p.getUniqueId().toString();
				if (canBuild(p)) {
					p.sendMessage(Main.getPrefix() + "Du wurdest entfernt!");
					synchronized (players) {
						players.remove(uuid);
					}
				} else {
					synchronized (players) {
						players.add(uuid);
					}
					p.setGameMode(GameMode.CREATIVE);
					p.sendMessage(Main.getPrefix() + "Du kannst nun Bauen!");
				}
			}
		} else {
			Cf.rsS(Cf.SUPERUSER_NORIGHTS, p);
		}

	}

	@Override
	public void onRegister(Plugin plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(EventPlayerQuitAsync e) {
		Player p = e.getPlayer();
		synchronized (players) {
			if (players.contains(p.getUniqueId().toString())) {
				players.remove(p.getUniqueId().toString());
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if (!canBuild(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (!canBuild(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(BlockDamageEvent e) {
		Player p = e.getPlayer();
		if (!canBuild(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void getRightPage(PlayerInteractEntityEvent e) {
		if (e.getRightClicked() instanceof ItemFrame) {
			if (!canBuild(e.getPlayer())) {
				e.setCancelled(true);
			}

		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(PlayerInteractAtEntityEvent e) {
		if (e.getRightClicked() instanceof ArmorStand) {
			if (!canBuild(e.getPlayer())) {
				e.setCancelled(true);
			}
		}

	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(PlayerBucketFillEvent e) {
		if (!canBuild(e.getPlayer())) {
			e.setCancelled(true);
		}
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(PlayerBucketEmptyEvent e) {
		if (!canBuild(e.getPlayer())) {
			e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onFlower(PlayerInteractEvent e) {
		if (e.getClickedBlock() == null) {
			return;
		}
		if (e.getClickedBlock().getType() == Material.FLOWER_POT) {
			if (!canBuild(e.getPlayer())) {
				e.setCancelled(true);
			}
		}
		if (e.getClickedBlock().getType() == Material.FLOWER_POT_ITEM) {
			if (!canBuild(e.getPlayer())) {
				e.setCancelled(true);
			}
		}

	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(VehicleDestroyEvent e) {
		if (e.getAttacker() instanceof Player) {
			if (!canBuild(((Player) e).getPlayer())) {
				e.setCancelled(true);
			}
		} else {
			e.setCancelled(true);
		}

	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(VehicleDamageEvent e) {
		if (e.getAttacker() instanceof Player) {
			if (!canBuild((Player) e.getAttacker())) {
				e.setCancelled(true);
			}
		} else {
			e.setCancelled(true);
		}

	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(InventoryOpenEvent e) {
		if (!canBuild((Player) e.getPlayer())) {
			if (e.getInventory().getType() == InventoryType.WORKBENCH) {
				e.setCancelled(true);
				Cf.rsS(Cf.CRAFT, (Player) e.getPlayer());
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(PlayerInteractEntityEvent e) {
		if (e.getRightClicked() instanceof Cake) {
			if (!canBuild(e.getPlayer())) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(CreatureSpawnEvent e) {
		if (e.getSpawnReason() != SpawnReason.EGG) {
			if (e.getSpawnReason() != SpawnReason.CUSTOM) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onExplode(EntityExplodeEvent e) {
		if (e.getEntityType() == EntityType.CREEPER) {
			e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onDestroyByEntity(HangingBreakByEntityEvent e) {
		if ((e.getRemover() instanceof Player)) {
			final Player p = (Player) e.getRemover();
			if (e.getEntity().getType() == EntityType.ITEM_FRAME || e.getEntity().getType() == EntityType.PAINTING) {
				if (!canBuild(p)) {
					e.setCancelled(true);
				}
			}
		}

		if ((e.getRemover() instanceof Snowball)) {
			e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void OnPlaceByEntity(HangingPlaceEvent e) {
		final Player p = e.getPlayer();
		if (e.getEntity().getType() == EntityType.ITEM_FRAME || e.getEntity().getType() == EntityType.PAINTING) {
			if (!canBuild(p)) {
				e.setCancelled(true);
			}

		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void ItemRemoval(EntityDamageByEntityEvent e) {
		if ((e.getDamager() instanceof Player)) {
			Player p = (Player) e.getDamager();
			if (e.getEntity().getType() == EntityType.ITEM_FRAME) {
				if (!canBuild(p)) {
					e.setCancelled(true);
				}
			}
		}
		if (((e.getDamager() instanceof Projectile)) && (e.getEntity().getType() == EntityType.ITEM_FRAME)) {
			Projectile p = (Projectile) e.getDamager();
			Player player = (Player) p.getShooter();
			if (!canBuild(player)) {
				e.setCancelled(true);
			}
		}
	}

	public static Boolean canBuild(Player p) {
		synchronized (players) {
			return players.contains(p.getUniqueId().toString());
		}
	}

}
