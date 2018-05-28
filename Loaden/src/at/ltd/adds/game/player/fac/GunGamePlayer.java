package at.ltd.adds.game.player.fac;

import org.bukkit.entity.Player;

import at.ltd.adds.game.player.GamePlayer;

public interface GunGamePlayer {

	public boolean joinGunGame(boolean hasvotet);
	public boolean exitGunGame(boolean roundend);
	public int getGunGameLevel();
	public void setGunGameLevel(int i);
	public void resetGunGameLevel();
	public int gotGunGameKill(Player target);
	public Player getPlayer();
	public GamePlayer getGamePlayer();
	
}
