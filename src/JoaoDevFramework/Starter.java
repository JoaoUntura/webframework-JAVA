package JoaoDevFramework;

import JoaoDevFramework.annotations.*;
import JoaoDevFramework.entities.ObjectMethodBind;
import JoaoDevFramework.entities.ParameterBind;
import JoaoDevFramework.entities.HttpMethod;

import java.lang.annotation.Annotation;
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

                    String routeKey = null;

                    if(method.isAnnotationPresent(GetMapping.class)){

                        GetMapping get = method.getAnnotation(GetMapping.class);

                        routeKey = path + get.path() + "-" + HttpMethod.GET;

                    } else if (method.isAnnotationPresent(PostMapping.class)) {

                        routeKey = path + "-" + HttpMethod.POST;

                    }

                    Parameter[] parameters = method.getParameters();

                    List<ParameterBind> parameterList = new ArrayList<>();

                    //Somente parametros anotados entram na lista
                    for (int i = 0; i < parameters.length; i ++){
                        Parameter parameter = parameters[i];
                        Annotation[] annotations = parameter.getAnnotations();

                        for(Annotation annotation:annotations){

                            ParameterBind parameterBind = new ParameterBind(parameter.getType(), i, annotation);
                            parameterList.add(parameterBind);
                            break;

                        }
                    }


                    appMap.put(routeKey, new ObjectMethodBind(object, method, parameterList));

                }

            }

        }



    @Override
    public String toString() {
        return "Starter{" +
                "appMap=" + appMap.toString() +
                '}';
    }

    public Map<String, ObjectMethodBind> getAppMap() {
        return appMap;
    }
}
