package Utils;
import java.util.ArrayList;

import model.Film;
import model.FilmList;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TextUtils {
	
	public static String serializeTextFilmList(FilmList inputList) {
	
		ArrayList<Film> films = inputList.getList();
		int filmCount = inputList.getNumFilms();
		Field[] fields = Film.class.getDeclaredFields();
		
		StringBuilder sBuilder = new StringBuilder();
		
		/*
		 * create a string with known regular expression in order to separate out
		 */
		
		sBuilder.append(filmCount + "£&");
		
		
		int i = 0;
		for(Film f : films) {
			int j = 0;
			if(i > 0) {sBuilder.append("£%");} //£% will separate the films in the list, don't want it on the first film
			
			/*
			 * Use reflection to build string - no changes needed later if film class is changed, provided class method formats (e.g getId with first letter capitalisation)
			 * stay the same and no arguments are needed to be passed in for get methods
			 */
			
			for(Field fld : fields) {
				String key = fld.getName();
				String keyCapitalised = key.substring(0, 1).toUpperCase() + key.substring(1);
				String methodName = "get" + keyCapitalised;
				Method method = null;
				String value = null;
				
				if(j > 0) {sBuilder.append("%&");} //use %& to separate out each key value pair, don't want on the first field
				
				
				//generate method needed
				try {
					method = Film.class.getDeclaredMethod(methodName);
				} catch (NoSuchMethodException | SecurityException e) {System.out.println(e);}
				
				//collect value from method generated
				try {
					value = method.invoke(f).toString();
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {System.out.println(e);}
				
				//append 
				sBuilder.append(key + "&&" + value); //use && to separate keys from values
				j++;
			}
			
			i ++;
		}
		
		return sBuilder.toString();
	}
	
	public static Film desearializeText(String textInput) {
		
	
		String[] values = new String[Film.class.getDeclaredFields().length];
		
		
			String[] pairs = textInput.split("%&"); //only 1 film in this example, dont need %%
			
			int i=0;
			for(String pair : pairs) {
				values[i] = pair.split("&&")[1];
				i++;
			}
			
		Film toReturn = new Film(Integer.parseInt(values[0]),values[1],Integer.parseInt(values[2]), values[3], values[4], values [5]);
		
		return toReturn;
	}
}
