package at.ltd.adds.utils.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class ConfigUtils {

	public static final HashMap<String, ConfigSerializer> TYPE_CODER = new HashMap<>();

	static {
		ConfigCoderRegister.register();
	}

	public static String convertList(List<Object> object, Class<?> type) {
		ConfigSerializer ser = TYPE_CODER.get(type.getSimpleName());
		ArrayList<String> data = new ArrayList<>();
		for (Object obj : object) {
			data.add(ser.serialize(obj));
		}
		return "[TYPE:LIST]" + StringUtils.join(data, ";") + ">>" + type.getSimpleName();
	}

	public static List<Object> convertList(String value) {
		String type = value.split(">>")[1];
		String[] data = getData(value.split(">>")[0]).split(";");
		ConfigSerializer decoder = TYPE_CODER.get(type);
		List<Object> LIST = new ArrayList<>();
		for (String da : data) {
			LIST.add(decoder.deserialize(da));
		}
		return LIST;
	}

	public static String getType(String data) {
		String result = data.substring(data.indexOf("[") + 1, data.indexOf("]"));
		return result.split(":", 2)[1];
	}

	public static String getData(String data) {
		String result = data.substring(data.indexOf("[") + 1, data.indexOf("]"));
		return data.replace("[" + result + "]", "");
	}

	public static Object convertToObject(String data) {
		ConfigSerializer ser = TYPE_CODER.get(getType(data));
		return ser.deserialize(getData(data));
	}

	public static String convertToString(Object data) {
		ConfigSerializer ser = TYPE_CODER.get(data.getClass().getSimpleName());
		return "[TYPE:" + data.getClass().getSimpleName().toUpperCase() + "]" + ser.serialize(data);
	}

}
