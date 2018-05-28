package at.ltd.gungame.guns.utils.enums;

public enum GunType {
	
	ASSAULT_RIFLE("Assault Rifle"),
	DMR("DMR"),
	SNIPER("Sniper"),
	PISTOL("Pistol"),
	PDW("PDW"),
	MG("MG"),
	SHOTGUN("Shotgun"),
	ROCKET_LAUNCHER("Rocket Launcher");
	
	private final String name;

	private GunType(String s) {
		name = s;
	}

	public boolean equalsName(String otherName) {
		return name.equals(otherName);
	}

	public String toString() {
		return this.name;
	}
	
    public String getName() {
        return name;
    }
}