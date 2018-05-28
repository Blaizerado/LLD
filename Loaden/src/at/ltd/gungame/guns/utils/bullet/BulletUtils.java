package at.ltd.gungame.guns.utils.bullet;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import at.ltd.Main;
import at.ltd.adds.utils.LocUtils;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.adds.utils.visual.Particles;

public class BulletUtils {

	public static final Material BULLET_MATERIAL = Material.SULPHUR;
	public static final String BULLET_NAME = "§cMunition";

	public static boolean validateItemStack(ItemStack is) {
		if (is.getType() == BULLET_MATERIAL) {
			if (is.hasItemMeta()) {
				if (is.getItemMeta().hasDisplayName()) {
					if (is.getItemMeta().getDisplayName().equals(BULLET_NAME)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static int getBulletCount(PlayerInventory pi) {
		ItemStack[] inv = pi.getContents();
		int cuantity = 0;
		for (int i = 0; i < inv.length; i++) {
			if (inv[i] != null) {
				if (inv[i].getType() == BULLET_MATERIAL) {
					if (validateItemStack(inv[i])) {
						int cant = inv[i].getAmount();
						cuantity = cuantity + cant;
					}
				}
			}
		}
		return cuantity;
	}

	public static void removeBullets(PlayerInventory pi, int ammount) {

		ItemStack[] inv = pi.getContents();
		ItemStack[] remove = new ItemStack[inv.length];

		int removepos = 0;

		for (int i = 0; i < inv.length; i++) {

			ItemStack is = inv[i];

			if (ammount == 0 | ammount < 0) {
				break;
			}

			if (is != null) {
				if (is.getType() == BULLET_MATERIAL) {
					if (validateItemStack(is)) {
						
						int size = is.getAmount();
						ammount = ammount - size;

						if (ammount < 0) {
							int dif = (ammount < 0 ? -ammount : ammount);
							ItemStack newis = is.clone();
							newis.setAmount(size - dif);
							remove[removepos] = newis;
						} else {
							remove[removepos] = is;
						}
						removepos++;
					}
				}
			}
		}
		for (int i = 0; i < remove.length; i++) {
			ItemStack is = remove[i];
			if (is == null) {
				break;
			}
			pi.removeItem(is);
		}
	}

	public static ItemStack createBullet() {
		ItemStack is = new ItemStack(BULLET_MATERIAL);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(BULLET_NAME);
		is.setItemMeta(im);
		return is;
	}

	public static ItemStack createBullet(int size) {
		ItemStack is = new ItemStack(BULLET_MATERIAL);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(BULLET_NAME);
		is.setItemMeta(im);
		is.setAmount(size);
		return is;
	}

	private static void createDestroyer() {

		AsyncThreadWorkers.submitSchedulerWorkTick(() -> {
			for (Bullet bul : Bullet.getBulletsAsList()) {
				if (!LocUtils.isSameWorld(bul.getCurrentBulletLocation(), AsyncThreadWorkers.getPlayerLocation(bul.getShooter()))) {
					bul.removeBullet();
				}
			}
		}, 20, 1);

		Main.addReloadHandler(() -> Bullet.getBulletsAsList().forEach(bul -> bul.removeBullet()));

		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () -> {
			Bukkit.getWorlds().forEach(world -> {
				world.getEntities().forEach(en -> {
					if (en instanceof Snowball) {
						en.remove();
						en.setGravity(true);
					}
				});
			});
		}, 5);
		AsyncThreadWorkers.submitSchedulerWorkTick(() -> {
			ArrayList<Bullet> removebullets = new ArrayList<>();
			long time = System.currentTimeMillis();
			List<Bullet> bullets = Bullet.getBulletsAsList();
			for (Bullet bullet : bullets) {

				if (bullet.removed) {
					continue;
				}

				if ((time - bullet.timeshot) > bullet.getFlytime()) {
					removebullets.add(bullet);
				} else {
					if (!AsyncThreadWorkers.getEntityStatus(bullet.getUUID())) {
						if ((time - bullet.timeshot) > 100) {
							removebullets.add(bullet);
						}
					}
				}
			}

			for (Bullet bullet : removebullets) {
				if (AsyncThreadWorkers.isPlayerOnline(bullet.getShooter())) {
					Player p = bullet.getShooter();
					Location snow = bullet.getCurrentBulletLocation();
					Location player = AsyncThreadWorkers.getPlayerLocation(p);
					if (player.getWorld() == snow.getWorld()) {
						if (snow.distance(player) < 60D) {
							Particles.BULLET_REMOVE(snow, bullet.getShooter());
						}
					}

				}
				bullet.removeBullet();

			}
		}, 1, 1);
	}

	public static void init() {
		createDestroyer();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), () -> {
			synchronized (Bullet.FLYING_BULLETS) {
				for (Bullet bullet : Bullet.FLYING_BULLETS.values()) {
					bullet.setCurrentBulletLocation(bullet.getSnowball().getLocation());
				}
			}
		}, 10, 0);
	}

	public boolean hasArmor(Player p) {
		PlayerInventory inv = p.getInventory();
		if (inv.getHelmet() != null && inv.getHelmet().getType() != Material.AIR) {
			return true;
		}
		if (inv.getChestplate() != null && inv.getHelmet().getType() != Material.AIR) {
			return true;
		}
		if (inv.getLeggings() != null && inv.getHelmet().getType() != Material.AIR) {
			return true;
		}
		if (inv.getBoots() != null && inv.getHelmet().getType() != Material.AIR) {
			return true;
		}
		return false;
	}

}
