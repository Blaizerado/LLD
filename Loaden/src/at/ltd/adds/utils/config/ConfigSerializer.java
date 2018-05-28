package at.ltd.adds.utils.config;

public interface ConfigSerializer {

	public Object deserialize(String data);

	public String serialize(Object data);

}
