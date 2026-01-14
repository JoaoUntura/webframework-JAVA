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

    public Object serialize(Class<?> targetClass, String json) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {


        Map<String, Class<?>> fieldsNameTypeMap = new HashMap<>();

        for(Field field:targetClass.getDeclaredFields()){

            fieldsNameTypeMap.put(field.getName(), field.getType());

        }

        //Conversao de json para HashMap
        Map<String, String> jsonMap = mapJson(json);


        //Check se o campo do Json corresponde a algum campo do objeto
        Map<String, Object> typedJsonMap = new HashMap<>();
        for (String key: jsonMap.keySet()){
            String value = jsonMap.get(key);
            Class<?> fieldType = fieldsNameTypeMap.get(key);
            if(fieldType != null){

                Object convertedField = objectToType(fieldType, value);

                typedJsonMap.put(key,convertedField);
            }
        }


        //Injecao dos valores via metodos Set
        Method[] methods = targetClass.getDeclaredMethods();
        Object object = targetClass.getConstructor().newInstance();

        for (Method method:methods){
            String methodName = method.getName();
            if (methodName.contains("set")){

                //setName -> name
                //setFirstName -> firstname
                String fieldName = methodName.replaceFirst("set", "").toLowerCase();

                Object value = typedJsonMap.get(fieldName);

                method.invoke(object, value);

            }

        }

        return object;
    }

    public Map<String, String> mapJson(String json) throws RuntimeException{

        Map<String, String> jsonMap = new HashMap<>();

        try{

            char[] characters = json.toCharArray();
            boolean start = false;

            StringBuilder buffer = new StringBuilder();

            String lastKey = null;

            boolean readingString = false;
            boolean isValue = false;
            for (char c : characters) {
                if (c == '{' && !start) {
                    start = true;
                    continue;
                }

                //Transicao entre key-values
                if ((c == ',' || c == '}') && isValue) {

                    String value = buffer.toString();
                    buffer.delete(0, buffer.length());

                    jsonMap.put(lastKey,value);
                    isValue = false;
                    lastKey = null;
                    continue;

                }

                if (c == '"' && !readingString) {
                    readingString = true;
                    continue;
                }

                if (Character.isWhitespace(c) && !readingString) continue;

                //Transicao de key pra value
                if (c == ':'){

                    String key = buffer.toString();

                    buffer.delete(0, buffer.length());

                    jsonMap.put(key, null);
                    lastKey = key;
                    isValue = true;
                    continue;

                }

                if (c == '"' && readingString) {
                    readingString = false;
                    continue;
                }

                buffer.append(c);

            }


        }catch (Exception e){
            System.out.println(e);
            throw new RuntimeException("Invalid Json Body");
        }

        return jsonMap;

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
