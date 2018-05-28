package at.ltd.adds.utils;

import java.util.ArrayList;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import at.ltd.Main;
import at.ltd.events.custom.EventConfigReload;

public class ReloadHandler implements Listener {

	private static ArrayList<Runnable> RELOAD_HANDLER = new ArrayList<>();
	private static Object lock = new Object();

	public static void init() {
		Main.registerListener(new ReloadHandler());
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void on(EventConfigReload e) {
		synchronized (lock) {
			for (Runnable run : (ArrayList<Runnable>) RELOAD_HANDLER.clone()) {
				try {
					run.run();
				} catch (Exception e2) {
					System.out.println("[LTD] Error on config reload handler.");
					e2.printStackTrace();
				}
			}
		}
	}

	public static void addReloadHandler(Runnable run) {
		synchronized (lock) {
			RELOAD_HANDLER.add(run);
		}
	}

	public static void removeReloadHandler(Runnable run) {
		synchronized (lock) {
			RELOAD_HANDLER.remove(run);
		}
	}

}
