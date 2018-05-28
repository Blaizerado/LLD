package at.ltd.adds.utils.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigAble {

	String key();
	/**
	 * If List ->
	 * 		1;2;3;4;5;6>>INTEGER
	 * 		hoi;ciao;toll;joo>>STRING
	 * 		etc.
	 * @return
	 */
	String value();
}
