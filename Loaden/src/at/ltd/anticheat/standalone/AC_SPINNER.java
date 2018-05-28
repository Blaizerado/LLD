package at.ltd.anticheat.standalone;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import at.ltd.anticheat.AnitCheatModule;
import at.ltd.events.custom.EventPlayerMoveAsync;

public class AC_SPINNER implements AnitCheatModule, Listener {

	@Override
	public boolean register() {
		return false;
	}

	@EventHandler
	public void on(EventPlayerMoveAsync e) {

	}

}
