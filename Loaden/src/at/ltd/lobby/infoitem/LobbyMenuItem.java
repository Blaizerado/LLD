package at.ltd.lobby.infoitem;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public interface LobbyMenuItem {
	
	public void onRegister();
	
	public void onClick(Player p);
	
	public String getName();
	
	public String getLore();
	
	public Material getMaterial();

}
