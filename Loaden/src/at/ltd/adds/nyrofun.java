package at.ltd.adds;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import at.ltd.adds.utils.serializer.ItemSerializer;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;

public class nyrofun {

	public static void init() {
		ItemStack skull = ItemSerializer.deserialize("[V=002:SKULL_ITEM/³1/³3/³/³/³/³/³/³/³/³/³/³/³/³/³/³]");
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setOwner("NyroForce");
		skull.setItemMeta(meta);
		AsyncThreadWorkers.submitSchedulerWorkTick(() -> {
			for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
				if (p.getName().equals("NyroForce")) {
					AsyncThreadWorkers.submitSyncWork(() -> {
						ItemStack is = p.getInventory().getHelmet();
						if (is == null || is.getType() == Material.AIR) {
							p.getInventory().setHelmet(skull);
						} else {
							p.getInventory().setHelmet(new ItemStack(Material.AIR));
						}
					});
				}
			}
		}, 52, 8);
	}

}
