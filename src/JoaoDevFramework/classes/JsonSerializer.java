package JoaoDevFramework.classes;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JsonSerializer {
    Map<Class<?>, Function<String, Object>> convertMap = new HashMap<>();



    public JsonSerializer(){
        convertMap.put(String.class, s -> s);
        convertMap.put(Integer.class, Integer::valueOf);
        convertMap.put(Float.class, Float::valueOf);
        convertMap.put(Double.class, Double::valueOf);
        convertMap.put(Boolean.class, Boolean::valueOf);
    }

    public Object serialize(Class<?> targetClass, String content) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        List<String> jsonKeyValues = List.of(content.split(","));

        Map<String, Class<?>> fieldsNameTypeMap = new HashMap<>();

        for(Field field:targetClass.getDeclaredFields()){

            fieldsNameTypeMap.put(field.getName(), field.getType());

        }

        Map<String, Object> jsonFields = new HashMap<>();
        for(String jsonKeyValue:jsonKeyValues){

            List<String> keyValue = Arrays.stream(jsonKeyValue.trim().split(":")).map(s -> {
               return s.trim().replaceAll("[\"{}]", "");
            }).toList();

            String key = keyValue.get(0);

            String value = keyValue.get(1);

            //Check se o campo do Json corresponde a algum campo do objeto
            Class<?> fieldType = fieldsNameTypeMap.get(key);
            if(fieldType != null){

                Object convertedField = objectToType(fieldType, value);

                jsonFields.put(key,convertedField);
            }
        }


        Method[] methods = targetClass.getDeclaredMethods();
        Object object = targetClass.getConstructor().newInstance();

        for (Method method:methods){
            String methodName = method.getName();
            if (methodName.contains("set")){

                //setName -> name
                //setFirstName -> firstname
                String fieldName = methodName.replaceFirst("set", "").toLowerCase();

                Object value = jsonFields.get(fieldName);


                method.invoke(object, value);

            }

        }

        return object;
    }

    public Object objectToType(Class<?> type, String object){




        Function<String, Object> function = convertMap.get(type);

        if(function != null){
           Object covertedValue = function.apply(object);
           return covertedValue;
        }

        return object;

    }

}
