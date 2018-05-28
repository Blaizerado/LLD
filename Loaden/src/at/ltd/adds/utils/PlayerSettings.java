package at.ltd.adds.utils;

public class PlayerSettings {
	
	public enum Settings{
		
		NO_POKE(1);
		
		private int val;

		Settings(int val) {
			this.val = val;
		}

		public int getID() {
			return val;
		}
		
	}
	
	public static void init() {
		
	}
	

}
