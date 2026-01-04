package JoaoDevFramework;

import JoaoDevFramework.annotations.*;
import JoaoDevFramework.classes.ObjectMethodBind;
import JoaoDevFramework.classes.ParameterBind;
import entities.HttpMethod;
import entities.HttpRequest;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.*;

public class Starter {

        private Map<String, ObjectMethodBind> appMap = new HashMap<>();
        private final Set<Class<?>> frameworkAnnotations = new HashSet<>(Set.of(Body.class, Request.class));



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

        public Optional<Object> handlerFinder(HttpRequest httpRequest)  {

            String path = httpRequest.getPath();
            HttpMethod method = httpRequest.getHttpMethod();

            String routeKey =  path + "-" + method;

            try{
                ObjectMethodBind objectMethodBind = appMap.get(routeKey);

                if(objectMethodBind == null) {
                    System.out.println("Handler nao encontrado");
                    return Optional.empty();
                }

                Method handler = objectMethodBind.getMethod();
                Object object = objectMethodBind.getObject();
                List<ParameterBind> parameterBindList = objectMethodBind.getParameterList();

                List<Object> parametersInjected = new ArrayList<>();

                for(ParameterBind parameterBind:parameterBindList){

                    if(parameterBind.getAnnotationObject() instanceof Body){

                        Type parameterType = parameterBind.getType();
                        Body bodyAnnotatio  = (Body) parameterBind.getAnnotationObject();

                        parametersInjected.add(httpRequest.getBody());
                        continue;

                    }

                    if (parameterBind.getAnnotationObject() instanceof Request){
                        Type parameterType = parameterBind.getType();


                        parametersInjected.add(httpRequest);
                        continue;
                    }

                    parametersInjected.add(null);

                }

                Object response = handler.invoke(object, parametersInjected.toArray());
                return Optional.ofNullable(response);

            }catch (Exception e){
                System.out.println(e);
            }

            return Optional.empty();


        }

    @Override
    public String toString() {
        return "Starter{" +
                "appMap=" + appMap.toString() +
                '}';
    }
}
