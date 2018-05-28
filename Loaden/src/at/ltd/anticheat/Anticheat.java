package at.ltd.anticheat;

import java.util.ArrayList;

import org.bukkit.event.Listener;

import at.ltd.Main;
import at.ltd.anticheat.standalone.AC_KILLAURA;
import at.ltd.anticheat.standalone.AC_SPINNER;

public class Anticheat {

	private static ArrayList<AnitCheatModule> MODULES = new ArrayList<>();

	public static void init() {
		register(new AC_SPINNER());
		register(new AC_KILLAURA());
	}

	public static void register(AnitCheatModule acm) {
		boolean listener = acm.register();
		if (listener) {
			Main.registerListener((Listener) acm);
		}
		MODULES.add(acm);
		System.out.println("[LTD-ANTICHEAT] Registrated anitcheat module: " + acm.getClass().getSimpleName());
	}

}
