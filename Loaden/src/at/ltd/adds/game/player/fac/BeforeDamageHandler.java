package at.ltd.adds.game.player.fac;

import at.ltd.adds.game.player.GamePlayer;

public interface BeforeDamageHandler {

	public void handle(GamePlayer player, boolean dead);

}
