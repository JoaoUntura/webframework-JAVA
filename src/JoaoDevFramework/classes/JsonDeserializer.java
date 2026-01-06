package JoaoDevFramework.classes;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonDeserializer {
    Logger logger = Logger.getGlobal();

    public String deserializeObject(Object object)  {


        StringBuilder jsonString = new StringBuilder("{");
        Field[] fields = object.getClass().getDeclaredFields();

        try {
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                String key = field.getName();
                Object value = field.get(object);


                if (value instanceof String) {
                    jsonString.append(String.format(" \"%s\":\"%s\",", key, value));
                } else {
                    jsonString.append(String.format(" \"%s\":%s,", key, value));
                }

                if (i == (fields.length - 1)) {
                    jsonString.deleteCharAt(jsonString.lastIndexOf(","));
                    jsonString.append("}");
                }


            }
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }

        return jsonString.toString();

    }

}
