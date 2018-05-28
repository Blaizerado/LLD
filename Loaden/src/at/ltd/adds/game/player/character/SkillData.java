package at.ltd.adds.game.player.character;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.entity.Player;

import at.ltd.Main;
import at.ltd.adds.Lg;
import at.ltd.adds.game.player.character.skillgroups.SkillGroupNames;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;

public class SkillData {

	private static HashMap<String, SkillData> DATA = new HashMap<>();

	private HashMap<String, Integer> PLAYER_LEVEL_DATA = new HashMap<>();
	private String uuid;
	private CharacterType characterType = null;

	public static SkillData getData(Player p) {
		synchronized (DATA) {
			return DATA.get(p.getUniqueId().toString());
		}
	}
	public static SkillData getData(String uuid) {
		synchronized (DATA) {
			return DATA.get(uuid);
		}
	}

	public static void removeData(Player p) {
		String uuid = p.getUniqueId().toString();
		SkillData sd = getData(uuid);
		AsyncThreadWorkers.submitWork(() -> sd.save());
		synchronized (DATA) {
			DATA.remove(uuid);
		}
	}

	public SkillData(String uuid) {
		this.uuid = uuid;
		AsyncThreadWorkers.submitWork(() -> {
			load();
			synchronized (DATA) {
				DATA.put(uuid, this);
			}
		});
	}

	public int getLevel(String name) {
		synchronized (PLAYER_LEVEL_DATA) {
			Integer level = PLAYER_LEVEL_DATA.get(name);
			if (level == null) {
				level = 0;
			}
			return level;
		}
	}
	
	public int getLevel(SkillGroupNames name) {
		synchronized (PLAYER_LEVEL_DATA) {
			Integer level = PLAYER_LEVEL_DATA.get(name.name());
			if (level == null) {
				level = 0;
			}
			return level;
		}
	}

	public SkillData setLevel(String name, int level) {
		synchronized (PLAYER_LEVEL_DATA) {
			if (PLAYER_LEVEL_DATA.containsKey(name)) {
				PLAYER_LEVEL_DATA.remove(name);
			}
			PLAYER_LEVEL_DATA.put(name, level);
		}
		return this;
	}
	
	public SkillData setLevel(SkillGroupNames name, int level) {
		String strname = name.name();
		synchronized (PLAYER_LEVEL_DATA) {
			if (PLAYER_LEVEL_DATA.containsKey(strname)) {
				PLAYER_LEVEL_DATA.remove(strname);
			}
			PLAYER_LEVEL_DATA.put(strname, level);
		}
		return this;
	}

	public CharacterType getType() {
		return characterType;
	}

	public void setType(CharacterType type) {
		this.characterType = type;
	}

	public synchronized void save() {
		try {
			if (characterType != null) {
				if (characterType != CharacterType.NOT_SELECTED) {
					Main.getSQLQuery().updateSQL("REPLACE INTO SKILL (`UUID`, `DATA`) VALUES ('" + uuid + "', '" + characterType.toString() + "/" + convertHashMap(PLAYER_LEVEL_DATA) + "');");

				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public synchronized void load() {
		try {
			ResultSet rs = Main.getSQLQuery().querySQL("SELECT * FROM `SKILL` WHERE `UUID` LIKE '" + uuid + "'");
			if (rs.next()) {
				String data = rs.getString("DATA");
				if (data.contains("/")) {
					String[] datasplit = data.split("/");
					characterType = CharacterType.valueOf(datasplit[0]);
					synchronized (PLAYER_LEVEL_DATA) {
						if(datasplit.length != 1) {
							PLAYER_LEVEL_DATA = getHashMap(datasplit[1]);
						}
					}
				} else {
					Lg.lgError("SKILL NO DATA UUID: " + uuid);
				}
			} else {
				characterType = CharacterType.NOT_SELECTED;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static String convertHashMap(HashMap<String, Integer> hm) {
		String data = hm.toString();
		data = data.replace("{", "");
		data = data.replace("}", "");
		data = data.replace(" ", "");
		return data;
	}

	private static HashMap<String, Integer> getHashMap(String data) {
		HashMap<String, Integer> hm = new HashMap<>();
		if (data.equals("")) {
			return hm;
		}
		for (String split : data.split(",")) {
			String[] sp = split.split("=");
			hm.put(sp[0], Integer.valueOf(sp[1]));
		}
		return hm;
	}

}
