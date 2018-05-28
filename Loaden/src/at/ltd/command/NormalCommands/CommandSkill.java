package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.game.player.character.Character;
import at.ltd.adds.game.player.character.CharacterType;
import at.ltd.adds.game.player.character.SkillData;
import at.ltd.adds.game.player.character.SkillGroup;
import at.ltd.adds.game.player.character.SkillGroupManager;
import at.ltd.adds.game.player.character.skillgroups.SkillGroupNames;
import at.ltd.adds.sql.SQLPlayer;
import at.ltd.adds.utils.ItemUtils;
import at.ltd.adds.utils.serializer.ItemSerializer;
import at.ltd.adds.utils.visual.InventoryAnimation;
import at.ltd.adds.utils.visual.InventoryAnimation.AnimationType;

public class CommandSkill implements CommandExecuter, Listener {

	public static ItemStack[] ITEMS;
	static {
		ITEMS = new ItemStack[15];
		for (int i = 0; i < 15; i++) {
			ITEMS[i] = ItemSerializer
					.deserialize("[V=002:STAINED_GLASS_PANE/³1/³" + i + "/³/³/³/³/³/³/³/³/³/³/³/³/³/³]");
		}
	}

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		openInv(p, true);
	}

	public static void openInv(Player p, boolean first) {
		InventoryAnimation ia = new InventoryAnimation();
		Inventory inv = Bukkit.getServer().createInventory(p, 27, "§6§lSkill Management");
		int i = 0;
		for (SkillGroupNames name : SkillGroupNames.values()) {
			SkillGroup sg = SkillGroupManager.getGroup(name.name());
			ia.put(i, ItemUtils.generateItemStack(ITEMS[i], "§6FERTIGKEIT §8| §7" + sg.getFrontEndName(),
					sg.getInfo(p).split("\\r?\\n")));
			i++;
		}
		ia.put(26, ItemUtils.generateItemStack(Material.REDSTONE_BLOCK, "§cSchließen"));
		ia.put(25, ItemUtils.generateItemStack(Material.NETHER_STAR, "§6Fertigkeitspunkte",
				"§6Deine Punkte: §a" + Main.getSQLPlayer(p).getSkillPoints()));
		p.openInventory(inv);
		ia.animate(inv, AnimationType.LEFT_TO_RIGHT, true, 0, null);
	}

	@EventHandler
	public void on(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (e.getInventory().getName().equals("§6§lSkill Management")) {
			e.setCancelled(true);
			int slot = e.getSlot();
			ItemStack is = e.getCurrentItem();
			if (slot == 26) {
				p.closeInventory();
			}
			if (is != null && is.hasItemMeta()) {
				String itemname = is.getItemMeta().getDisplayName();
				if (itemname.startsWith("§6FERTIGKEIT §8| §7")) {
					if(Character.getSkillData(p).getType() == CharacterType.NOT_SELECTED) {
						Cf.rsS(Cf.SKILL_GROUP_NOT_SELECTED, p);
						return;
					}
					SQLPlayer sql = Main.getSQLPlayer(p);
					int skillpoints = sql.getSkillPoints();
					SkillGroup sg = getSkillGroup(itemname.replace("§6FERTIGKEIT §8| §7", ""));
					int cost = SkillGroupManager.getCost(p, sg);
					if (skillpoints >= cost) {
						sg.levelUp(p);
						sql.setSkillPoints(skillpoints - cost);
						p.closeInventory();
						Cf.rsS(Cf.BUY_SKILL, p);
					} else {
						p.closeInventory();
						Cf.rsS(Cf.NO_SKILL_POINTS, p);
					}
				}
			}

		}
	}

	public static SkillGroup getSkillGroup(String name) {
		for (SkillGroupNames skg : SkillGroupNames.values()) {
			SkillGroup sg = SkillGroupManager.getGroup(skg.name());
			if (sg.getFrontEndName().equals(name)) {
				return sg;
			}
		}
		return null;
	}

	public static void update(Player p, Inventory inv) {
		inv.setItem(25, ItemUtils.generateItemStack(Material.REDSTONE, "§6Fertigkeitspunkte",
				"§6Deine Punkte\n§a" + Main.getSQLPlayer(p).getSkillPoints()));
		p.updateInventory();
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
		Main.registerListener(this);
	}

}
