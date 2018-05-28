package at.ltd.command.NormalCommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.fasterxml.jackson.databind.AnnotationIntrospector.ReferenceProperty.Type;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.game.player.character.Character;
import at.ltd.adds.game.player.character.CharacterType;
import at.ltd.adds.game.player.character.SkillData;
import at.ltd.adds.sql.SQLPlayer;
import at.ltd.adds.utils.ItemUtils;
import at.ltd.adds.utils.config.Config;
import at.ltd.adds.utils.config.ConfigAble;
import at.ltd.adds.utils.inventory.InventoryHandler;
import at.ltd.adds.utils.inventory.ItemClickHandler;
import at.ltd.adds.utils.visual.InventoryAnimation;
import at.ltd.adds.utils.visual.InventoryAnimation.AnimationType;

public class CommandSkillGroup implements CommandExecuter {

	@ConfigAble(key = "", value = "§c")
	public static String prefix;

	@ConfigAble(key = "", value = "§cSkillGroup")
	public static String inv_name;

	@ConfigAble(key = "", value = "5")
	public static int change_cost;

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		Inventory inv = Bukkit.createInventory(p, 9, inv_name);
		InventoryAnimation animation = new InventoryAnimation();
		InventoryHandler.addProtection(inv);
		SkillData skilldata = Character.getSkillData(p);
		CharacterType current = skilldata.getType();
		Config cfg = Config.getInstance("SkillGroupSelection");

		int i = 0;
		for (CharacterType ct : CharacterType.values()) {
			if (ct != CharacterType.ALL && ct != CharacterType.NOT_SELECTED) {
				String cost = "" + change_cost;
				if (ct == current) {
					cost = "0";
				}
				ItemStack is = ItemUtils.generateItemStack(Material.MAGMA_CREAM, prefix + ct.getDisplayName(),
						convertLore(cfg.getString(ct.name()).replace("[COST]", cost)));
				animation.put(i, is);
				i++;
				InventoryHandler.addClickHandler(inv, is, new ItemClickHandler() {

					@Override
					public void on(Player p, ItemStack is, Inventory inv) {
						p.closeInventory();
						select(p, ct, skilldata);
					}
				});
			}
		}
		ItemStack close = ItemUtils.generateItemStack(Material.REDSTONE_BLOCK, "§cClose");
		InventoryHandler.addClickHandler(inv, close, new ItemClickHandler() {

			@Override
			public void on(Player p, ItemStack is, Inventory inv) {
				p.closeInventory();
			}
		});
		animation.put(8, close);

		animation.put(7, ItemUtils.generateItemStack(Material.NETHER_STAR, "§6Fertigkeitspunkte",
				"§6Deine Punkte: §a" + Main.getSQLPlayer(p).getSkillPoints()));

		p.openInventory(inv);
		animation.animate(inv, AnimationType.LEFT_TO_RIGHT, false, 1, null);

	}

	public static void select(Player p, CharacterType wantedtype, SkillData sd) {
		SQLPlayer sqlplayer = Main.getSQLPlayer(p);
		int skillpoints = sqlplayer.getSkillPoints();
		CharacterType currenttype = sd.getType();
		if (currenttype.equals(wantedtype)) {
			Cf.rsS(Cf.SKILL_GROUP_SELECT_ALREADY, p, "[CHARACTER_TYPE]", currenttype.getDisplayName());
			return;
		}
		if (skillpoints < change_cost) {
			Cf.rsS(Cf.SKILL_GROUP_SELECT_NO_POINTS, p, "[CHARACTER_TYPE]", wantedtype.getDisplayName());
			return;
		}
		skillpoints -= change_cost;
		sqlplayer.setSkillPoints(skillpoints);
		sd.setType(wantedtype);
		Cf.rsS(Cf.SKILL_GROUP_SELECT, p, "[CHARACTER_TYPE]", wantedtype.getDisplayName());

	}

	public static List<String> convertLore(String lore) {
		ArrayList<String> ar = new ArrayList<>();
		for (String str : lore.split("<newline>")) {
			ar.add(Cf.rs(str));
		}
		return ar;
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
		Config cfg = Config.getInstance("SkillGroupSelection");
		Config.loadMyClass("SkillGroupSelection", this).setReloadAble(true).setReloadHandler(() -> {
			Config.loadMyClass("SkillGroupSelection", this);
		});

		for (CharacterType ct : CharacterType.values()) {
			if (ct != CharacterType.ALL && ct != CharacterType.NOT_SELECTED) {
				if (!cfg.contains(ct.name())) {
					cfg.putRaw(ct.name(),
							"[TYPE:STRING]Kosten: [COST] Fertigkeitspunkte<newline>Features:<newline>Kann tolle sachen machen.");
				}
			}
		}
		cfg.save();
	}

}
