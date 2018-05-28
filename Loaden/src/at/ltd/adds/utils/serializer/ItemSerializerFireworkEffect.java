package at.ltd.adds.utils.serializer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;

public class ItemSerializerFireworkEffect implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4638116717078664803L;
	
	public List<Integer> colors;
	public List<Integer> colorsfade;
	public String type;
	public Boolean flicker;
	public Boolean trail;

	public static ItemSerializerFireworkEffect serialize(FireworkEffect fe) {
		ItemSerializerFireworkEffect f = new ItemSerializerFireworkEffect();
		f.colors = new ArrayList<>();
		f.colorsfade = new ArrayList<>();
		
		for (Color c : fe.getColors()) {
			f.colors.add(c.asRGB());
		}
		for (Color c : fe.getFadeColors()) {
			f.colorsfade.add(c.asRGB());
		}
		f.type = fe.getType().name();
		f.flicker = fe.hasFlicker();
		f.trail = fe.hasTrail();
		return f;
	}

	public FireworkEffect deserialize() {
		List<Color> color = new ArrayList<>();
		List<Color> fade = new ArrayList<>();
		for(Integer i : colors){
			color.add(Color.fromRGB(i));
		}
		
		for(Integer i : colorsfade){
			fade.add(Color.fromRGB(i));
		}
		
		FireworkEffect fe = FireworkEffect.builder().flicker(flicker).trail(trail).with(Type.valueOf(type)).withColor(color).withFade(fade).build();
		return fe;
	}

}
