package at.ltd.adds.game.player.fac;

import org.bukkit.entity.Player;

import at.ltd.adds.game.player.GamePlayer;

public interface GamePlayerLogic {

	public enum LogicField {
		BUILD, SHOOT, BUY, MOVE, JOIN_GAME, TALK, USE_CUSTOM_WEAPONS
	}

	public void setLogicValue(LogicField field, boolean value);
	public Player getPlayer();
	public GamePlayer getGamePlayer();
	
	
	public boolean canBuild();
	public boolean canShoot();
	public boolean canBuy();
	public boolean canMove();
	public boolean canJoinGame();
	public boolean canTalk();
	public boolean canUseCustomWeapons();
	

}
