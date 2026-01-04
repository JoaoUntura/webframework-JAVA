package JoaoDevFramework;

import JoaoDevFramework.annotations.Body;
import JoaoDevFramework.annotations.Controller;
import JoaoDevFramework.annotations.GetMapping;
import JoaoDevFramework.annotations.PostMapping;
import JoaoDevFramework.classes.ObjectMethodBind;
import entities.HttpMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class Starter {

        private Map<String, ObjectMethodBind> appMap = new HashMap<>();


        public Starter(Set<Object> controllers){

            for(Object object : controllers){

                Class<?>  controllerClass = object.getClass();

                if(!controllerClass.isAnnotationPresent(Controller.class)) continue;

                Controller controller = controllerClass.getAnnotation(Controller.class);

                String path = controller.path();

                Method[] methods = controllerClass.getDeclaredMethods();

                for (Method method:methods){

                    if(method.isAnnotationPresent(GetMapping.class)){

                        String routeKey = path + "-" + HttpMethod.GET;
                        appMap.put(routeKey, new ObjectMethodBind(object, method));

                    } else if (method.isAnnotationPresent(PostMapping.class)) {

                        String routeKey = path + "-" + HttpMethod.POST;

                        appMap.put(routeKey, new ObjectMethodBind(object, method));

                    }


                }

            }

        }

        public void handlerFinder(String path, HttpMethod method, String requestBody)  {
            String routeKey =  path + "-" + method;

            try{
                ObjectMethodBind objectMethodBind = appMap.get(routeKey);

                if(objectMethodBind == null) {
                    System.out.println("Handler nao encontrado");
                    return;
                }

                Method handler = objectMethodBind.getMethod();
                Object object = objectMethodBind.getObject();

                if(requestBody == null){
                    handler.invoke(object);
                    return;
                }

                Parameter[] parameters = handler.getParameters();

                for (Parameter parameter:parameters){

                    if(parameter.isAnnotationPresent(Body.class)){

                        handler.invoke(object, requestBody);
                        return;

                    }

                }


            }catch (Exception e){
                System.out.println(e);
            }


        }

    @Override
    public String toString() {
        return "Starter{" +
                "appMap=" + appMap.toString() +
                '}';
    }
}
