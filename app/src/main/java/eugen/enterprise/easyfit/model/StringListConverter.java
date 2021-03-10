package eugen.enterprise.easyfit.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class StringListConverter {

    private static Gson gson = new Gson();

    @TypeConverter
    public static List<String> stringToList(String string) {
        if (string == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<String>>() {}.getType();

        return gson.fromJson(string, listType);
    }

    @TypeConverter
    public static String listToString(List<String> list) {
        return gson.toJson(list);
    }
}
