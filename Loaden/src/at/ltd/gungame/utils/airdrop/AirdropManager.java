package at.ltd.gungame.utils.airdrop;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.utils.serializer.ItemSerializer;
import at.ltd.gungame.events.custom.EventGameTick;
import at.ltd.gungame.guns.utils.GunRegister;
import at.ltd.gungame.guns.utils.bullet.BulletUtils;
import at.ltd.gungame.guns.utils.rocket.RocketUtils;
import at.ltd.gungame.maps.MapManager;

public class AirdropManager implements Listener {

	public static ArrayList<AirdropBlock> blocks = new ArrayList<>();

	public static AirdropKit KIT_L96;
	public static AirdropKit KIT_M4A4;
	public static AirdropKit KIT_M79_THUMPER;
	public static AirdropKit KIT_M72_LAW;
	public static AirdropKit KIT_MG42;

	public static void init() {
		registerBlocks();
		registerKits();
		Bukkit.getServer().getPluginManager().registerEvents(new AirdropManager(), Main.getPlugin());
	}

	public static void registerKits() {

		KIT_L96 = new AirdropKit();
		KIT_L96.addGun(GunRegister.getGun(20));
		KIT_L96.addItem(BulletUtils.createBullet(64));

		KIT_M4A4 = new AirdropKit();
		KIT_M4A4.addGun(GunRegister.getGun(7));
		KIT_M4A4.addGun(GunRegister.getGun(28));
		KIT_M4A4.addItem(BulletUtils.createBullet(64));
		KIT_M4A4.addItem(BulletUtils.createBullet(64));
		KIT_M4A4.addItem(BulletUtils.createBullet(64));
		KIT_M4A4.addItem(BulletUtils.createBullet(64));

		KIT_M79_THUMPER = new AirdropKit();
		KIT_M79_THUMPER.addGun(GunRegister.getGun(34));
		KIT_M79_THUMPER.addGun(GunRegister.getGun(28));
		KIT_M79_THUMPER.addItem(BulletUtils.createBullet(64));
		KIT_M79_THUMPER.addItem(BulletUtils.createBullet(64));
		KIT_M79_THUMPER.addItem(RocketUtils.createRocket(5));

		KIT_M72_LAW = new AirdropKit();
		KIT_M72_LAW.addGun(GunRegister.getGun(35));
		KIT_M72_LAW.addGun(GunRegister.getGun(28));
		KIT_M72_LAW.addItem(BulletUtils.createBullet(64));
		KIT_M72_LAW.addItem(BulletUtils.createBullet(64));
		KIT_M72_LAW.addItem(RocketUtils.createRocket(5));

		KIT_MG42 = new AirdropKit();
		KIT_MG42.addGun(GunRegister.getGun(30));
		KIT_MG42.addItem(BulletUtils.createBullet(64));
		KIT_MG42.addItem(BulletUtils.createBullet(64));
		KIT_MG42.addItem(BulletUtils.createBullet(64));
		KIT_MG42.addItem(BulletUtils.createBullet(64));

	}

	public static void registerBlocks() {
		// CHEST AND WIRE
		add(0, 0, 0, "[V=002:CHEST/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]");
		add(0, 1, 0, "[V=002:IRON_FENCE/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]");
		add(0, 2, 0, "[V=002:IRON_FENCE/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]");
		add(0, 3, 0, "[V=002:IRON_FENCE/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]");

		// WOOL TOP
		add(0, 4, 0, "[V=002:WOOL/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]");
		add(1, 4, 0, "[V=002:WOOL/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]");
		add(-1, 4, 0, "[V=002:WOOL/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]");
		add(0, 4, 1, "[V=002:WOOL/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]");
		add(0, 4, -1, "[V=002:WOOL/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]");

		// GLASS
		add(1, 4, 1, "[V=002:STAINED_GLASS_PANE/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]");
		add(1, 4, -1, "[V=002:STAINED_GLASS_PANE/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]");
		add(-1, 4, 1, "[V=002:STAINED_GLASS_PANE/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]");
		add(-1, 4, -1, "[V=002:STAINED_GLASS_PANE/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]");

		// WOOL BOTTOM
		add(1, 3, 1, "[V=002:WOOL/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]");
		add(1, 3, -1, "[V=002:WOOL/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]");
		add(-1, 3, 1, "[V=002:WOOL/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]");
		add(-1, 3, -1, "[V=002:WOOL/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]");

	}

	public static void add(int x, int y, int z, String item) {
		blocks.add(new AirdropBlock(x, y, z, ItemSerializer.deserialize(item)));
	}

	public static void spawnBlock(AirdropBlock ab, Location startlocation, int y) {
		Location loc = startlocation.clone();
		loc = loc.add(ab.xoffset, ab.yoffset - y, ab.zoffset);
		placeItemStack(ab.block, loc);
	}

	public static void removeBlock(AirdropBlock ab, Location startlocation, int y) {
		Location loc = startlocation.clone();
		loc = loc.add(ab.xoffset, ab.yoffset - y, ab.zoffset);
		loc.getWorld().getBlockAt(loc).setType(Material.AIR);
	}

	public static void placeItemStack(ItemStack item, Location loc) {
		Block b = loc.getWorld().getBlockAt(loc);
		b.setType(item.getType());
		b.setData((byte) item.getDurability());
	}

	@EventHandler
	public void on(EventGameTick e) {
		if (!MapManager.airdrop_objects.containsKey(e.getGameRound().getGameMap().getMapName())) {
			return;
		}

		AirdropSpawn as = (AirdropSpawn) MapManager.airdrop_objects.get(e.getGameRound().getGameMap().getMapName());

		int tick = e.getTick();
		if (tick == 60 * 2) {
			ArrayList<Location> locs = as.getAirdropSpawns();
			Location loc = locs.get((new Random()).nextInt(locs.size()));
			Airdrop a = new Airdrop(loc, null);
			a.start();
			e.getGameRound().broadcastMessage(Cf.rs(Cf.AIRDROP));
		}

		if (tick == 60 * 4) {
			ArrayList<Location> locs = as.getAirdropSpawns();
			Location loc = locs.get((new Random()).nextInt(locs.size()));
			Airdrop a = new Airdrop(loc, null);
			a.start();
			e.getGameRound().broadcastMessage(Cf.rs(Cf.AIRDROP));
		}

		if (tick == 60 * 6) {
			ArrayList<Location> locs = as.getAirdropSpawns();
			Location loc = locs.get((new Random()).nextInt(locs.size()));
			Airdrop a = new Airdrop(loc, null);
			a.start();
			e.getGameRound().broadcastMessage(Cf.rs(Cf.AIRDROP));
		}
		if (tick == 60 * 8) {
			ArrayList<Location> locs = as.getAirdropSpawns();
			Location loc = locs.get((new Random()).nextInt(locs.size()));
			Airdrop a = new Airdrop(loc, null);
			a.start();
			e.getGameRound().broadcastMessage(Cf.rs(Cf.AIRDROP));
		}
	}

}
