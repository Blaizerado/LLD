package at.ltd.adds.game.player.character;

public enum CharacterType {

	SOLDIER("Soldat"), SNIPER("Sniper"), MEDIC("Sanitäter"), NOT_SELECTED("NO"), ALL("Alle");

	private String val;

	CharacterType(String val) {
		this.val = val;
	}

	public String getDisplayName() {
		return val;
	}

	public CharacterType fromDisplayName(String name) {
		for (CharacterType type : values()) {
			if (type.getDisplayName().equals(name)) {
				return type;
			}
		}
		return null;
	}

}
