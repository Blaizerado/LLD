package at.ltd.command.NormalCommands;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.game.player.character.Character;
import at.ltd.adds.game.player.character.CharacterType;
import at.ltd.adds.game.player.character.SkillData;
import at.ltd.adds.game.player.character.SkillGroup;
import at.ltd.adds.game.player.character.skillgroups.SkillGroupNames;
import at.ltd.adds.sql.SQLPlayer;
import at.ltd.adds.ts3.TeamSpeak;
import at.ltd.adds.utils.threading.AsyncAble;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.adds.utils.visual.InventoryAnimation;
import at.ltd.adds.utils.visual.InventoryAnimation.AnimationType;
import at.ltd.gungame.guns.utils.Gun;
import at.ltd.gungame.guns.utils.GunRegister;
import at.ltd.gungame.guns.utils.GunUtils;
import at.ltd.gungame.ranks.GRankPerm;
import at.ltd.gungame.utils.airdrop.Airdrop;
import at.ltd.lobby.LobbyEffects;
import at.ltd.lobby.LobbyUtils;

public class CommandTest implements CommandExecuter {

	@AsyncAble
	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (!GRankPerm.isHighMember(p)) {
			Cf.rsS(Cf.NO_RIGHTS, p);
			return;
		}

		if (args.size() < 1) {
			p.sendMessage(Main.getPrefix() + "/test <id>");
			return;
		}
		int id = Integer.valueOf(args.get(1));

		if (1 == id) {
			Gun g = GunUtils.createGun(GunRegister.getGun(7), p, -1);
			p.getInventory().addItem(g.getItemStack());
		}

		if (2 == id) {
			ItemStack is = p.getInventory().getItemInHand();
			p.sendMessage(GunUtils.isGun(is) + "");
		}
		if (3 == id) {
			ItemStack is = p.getInventory().getItemInHand();
			p.sendMessage(GunUtils.getGunMemory(is).getPlayer().getName() + "");
		}
		if (4 == id) {
			ItemStack is = p.getInventory().getItemInHand();
			p.sendMessage("" + GunUtils.searchGunByItem(is));
		}
		if (5 == id) {
			Airdrop airdrop = new Airdrop(p.getLocation(), null);
			airdrop.start();
		}
		if (6 == id) {
			p.sendMessage(Main.getPrefix() + LobbyUtils.isInLobby(p));
		}

		if (7 == id) {
			Main.getConnectionHandler().getConnection().getRawConnection().close();
		}

		if (8 == id) {
			p.sendMessage(args.size() + " " + args.toString());
		}
		if (9 == id) {
			LobbyEffects.playEffect();
		}
		if (10 == id) {
			for (int i = 0; i < 100; i++) {
				AsyncThreadWorkers.submitWork(() -> {
					String data = (String) AsyncThreadWorkers.submitSyncDataWork(() -> {
						return "geil";
					});
					System.out.println(data);
				});

			}
		}
		if (11 == id) {
			AsyncThreadWorkers.submitWork(() -> {
				String data = (String) AsyncThreadWorkers.submitSyncDataWork(() -> {
					try {
						Thread.sleep(ThreadLocalRandom.current().nextLong(30, 100));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					return "geil";
				});
				System.out.println(data);
			});
		}
		if (12 == id) {
			InventoryAnimation animation = new InventoryAnimation();
			Inventory inv = Bukkit.getServer().createInventory(p, 36);
			for (int i = 0; i < 40; i++) {
				Material mat = randomEnum(Material.class);
				if (mat == Material.AIR) {
					mat = Material.STONE;
				} else {
					mat = Material.COBBLESTONE;
				}
				animation.put(i, new ItemStack(mat));
			}
			p.openInventory(inv);
			animation.animate(inv, AnimationType.LEFT_TO_RIGHT, true, 2, () -> {

			});
		}

		if (13 == id) {
			InventoryAnimation animation = new InventoryAnimation();
			Inventory inv = Bukkit.getServer().createInventory(p, 36);
			for (int i = 0; i < 40; i++) {
				Material mat = randomEnum(Material.class);
				if (mat == Material.AIR) {
					mat = Material.STONE;
				} else {
					mat = Material.COBBLESTONE;
				}
				animation.put(i, new ItemStack(mat));
			}
			p.openInventory(inv);
			animation.animate(inv, AnimationType.RIGHT_TO_LEFT, true, 2, () -> {

			});
		}

		if (14 == id) {
			InventoryAnimation animation = new InventoryAnimation();
			Inventory inv = Bukkit.getServer().createInventory(p, 36);
			for (int i = 0; i < 40; i++) {
				Material mat = randomEnum(Material.class);
				if (mat == Material.AIR) {
					mat = Material.STONE;
				} else {
					mat = Material.COBBLESTONE;
				}
				animation.put(i, new ItemStack(mat));
			}
			p.openInventory(inv);
			animation.animate(inv, AnimationType.UP_TO_DOWN, true, 2, () -> {

			});
		}

		if (15 == id) {
			InventoryAnimation animation = new InventoryAnimation();
			Inventory inv = Bukkit.getServer().createInventory(p, 36);
			for (int i = 0; i < 40; i++) {
				Material mat = randomEnum(Material.class);
				if (mat == Material.AIR) {
					mat = Material.STONE;
				} else {
					mat = Material.COBBLESTONE;
				}
				animation.put(i, new ItemStack(mat));
			}
			p.openInventory(inv);
			animation.animate(inv, AnimationType.DOWN_TO_UP, true, 2, () -> {

			});
		}

		if (16 == id) {
			SkillData.getData(p).setType(CharacterType.SNIPER);
			p.sendMessage("Sniper");
		}

		if (17 == id) {
			for (SkillGroupNames name : SkillGroupNames.values()) {
				SkillGroup sg = Character.getSkillGroup(name.toString());
				sg.levelUp(p);
				p.sendMessage("Name: " + sg.getFrontEndName() + " Level: " + sg.getPlayerLevel(p));
			}
		}
		if (18 == id) {
			for (SkillGroupNames name : SkillGroupNames.values()) {
				SkillGroup sg = Character.getSkillGroup(name.toString());
				sg.levelDown(p);
				p.sendMessage("Name: " + sg.getFrontEndName() + " Level: " + sg.getPlayerLevel(p));
			}
		}

		if (19 == id) {
			for (SkillGroupNames name : SkillGroupNames.values()) {
				SkillGroup sg = Character.getSkillGroup(name.toString());
				p.sendMessage("Name: " + sg.getFrontEndName() + " Level: " + sg.getPlayerLevel(p));
			}
		}
		if (20 == id) {
			SQLPlayer player = Main.getSQLPlayer(p);
			player.setSkillPoints(player.getSkillPoints() + 1);
			p.sendMessage("+1");
		}
		if(21 == id) {
			p.sendMessage("Online & Veri: " + TeamSpeak.isOnlineAndVeri(p));
		}

	}

	private static final SecureRandom random = new SecureRandom();

	public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
		int x = random.nextInt(clazz.getEnumConstants().length);
		return clazz.getEnumConstants()[x];
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
