package Utils;
import com.google.gson.Gson;

import model.Film;

public class JsonUtils {
	
	public static <T> String serializeJson(T t) {
		Gson gson = new Gson();
		String json = gson.toJson(t);
		return json;
	}

	
	public static Film deserializeJson(String jsonInput){
		Gson gson = new Gson();
		Film toReturn = gson.fromJson(jsonInput, Film.class);
		return toReturn;
	}
}

