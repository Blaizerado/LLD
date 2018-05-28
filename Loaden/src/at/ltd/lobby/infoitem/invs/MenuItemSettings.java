package at.ltd.lobby.infoitem.invs;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import at.ltd.adds.utils.config.Config;
import at.ltd.adds.utils.config.ConfigAble;
import at.ltd.command.CommandManager;
import at.ltd.lobby.infoitem.LobbyMenuItem;

public class MenuItemSettings implements LobbyMenuItem{

	@ConfigAble(key = "", value = "Settings")
	public String name;
	
	@ConfigAble(key = "", value = "An und abschaltung von Features<newline>und Nachrichten.")
	public String lore;
	
	
	@Override
	public void onRegister() {
		Config.loadMyClass(this.getClass().getSimpleName(), this).setReloadAble(true).setReloadHandler(() ->{
			Config.loadMyClass(this.getClass().getSimpleName(), this);
		});
	}
	
	@Override
	public void onClick(Player p) {
		try {
			CommandManager.getCommandExecuter("settings").run(p, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getLore() {
		return lore;
	}

	@Override
	public Material getMaterial() {
		return Material.COMPASS;
	}



}
