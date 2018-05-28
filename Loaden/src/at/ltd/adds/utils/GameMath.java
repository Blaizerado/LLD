package at.ltd.adds.utils;

import java.math.BigDecimal;

public class GameMath {

	public static float getKD(int kills, int deaths) {
		if(kills == 0){
			return (float) 0;
		}
		if(deaths == 0){
			return (float) kills;
		}
		float ki = kills;
		float de = deaths;
		float kd = ki / de;
		return kd;
	}

	public static float round(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}

	public static int getReward(int kills, int deaths, int gglevel, float multi) {
		float gg = gglevel;
		float ki = kills;
		float plus = ki + (gg * 5);
		float end = 0;
		
		if (0 > deaths || deaths == 0) {
			if (multi == 0) {
				end = plus * ki;
			} else {
				end = plus * ki * multi;
			}
			return (int) round(end, 0);
		}

		if (gglevel == 0 && kills == 0) {
			return 0;
		}

		Float kd = getKD(kills, deaths);
		if (multi == 0) {
			end = plus * kd;
		} else {
			end = plus * kd * multi;
		}
		return (int) round(end, 0);
	}

	public static int getMultiPlus(int kills, int deaths, int gglevel, Float multi) {
		int first = getReward(kills, deaths, gglevel, 0);
		int sec = getReward(kills, deaths, gglevel, multi);
		return sec - first;
	}


}
