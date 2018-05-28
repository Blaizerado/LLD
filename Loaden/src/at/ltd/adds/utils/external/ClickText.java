package at.ltd.adds.utils.external;

import java.util.UUID;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ClickText {

	public static void sendClickText(Player p, String text) {
		UUID id = UUIDFetcher.getUUID(p);

		TextComponent txt = new TextComponent(text);

		txt.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/atcPerform_" + id));

		p.spigot().sendMessage(txt);
	}

	public static void sendHoverClickText(Player p, String clickText, String hovertext) {
		UUID id = UUIDFetcher.getUUID(p);

		TextComponent txt = new TextComponent(clickText);

		txt.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/atcPerform_" + id));
		txt.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hovertext).create()));

		p.spigot().sendMessage(txt);
	}

}
