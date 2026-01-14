package JoaoDevFramework.classes;

import JoaoDevFramework.annotations.*;
import JoaoDevFramework.entities.Handler;
import JoaoDevFramework.entities.ParameterBind;
import JoaoDevFramework.entities.HttpMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class Starter {

        private final Map<String, List<Handler>> appMap = new HashMap<>();


        public Starter(Set<Object> controllersObjects){

            for(Object controllerObject : controllersObjects){

                List<Handler> controllerHandlerList = new ArrayList<>();

                //Controller
                Class<?> controllerClass = controllerObject.getClass();

                if(!controllerClass.isAnnotationPresent(Controller.class)) continue;

                Controller controller = controllerClass.getAnnotation(Controller.class);

                //Map key
                String path = controller.path().replaceFirst("/", "");

                //Handler objects
                Method[] methods = controllerClass.getDeclaredMethods();

                for (Method method:methods){

                    Handler handler = new Handler(controllerObject, method);

                    //Processando anotações de metodo
                    StringBuilder annotationPathBuilder = new StringBuilder();
                    if (method.isAnnotationPresent(GetMapping.class)){

                        GetMapping get = method.getAnnotation(GetMapping.class);
                        annotationPathBuilder.append(get.path());
                        handler.setHttpMethod(HttpMethod.GET);

                    } else if (method.isAnnotationPresent(PostMapping.class)) {

                        PostMapping postMapping = method.getAnnotation(PostMapping.class);
                        annotationPathBuilder.append(postMapping.path());
                        handler.setHttpMethod(HttpMethod.POST);

                    }

                        String annotationPath = annotationPathBuilder.toString();

                    if(annotationPath.startsWith("/")){
                        annotationPath = annotationPath.replaceFirst("/", "");
                    }

                    Map<Integer, String> pathMap = new HashMap<>();
                    List<String> paths = new ArrayList<>(List.of(annotationPath.split("/")));

                    for (int i = 0; i< paths.size(); i ++){

                        Integer pathPosition = i;
                        String subPath = paths.get(pathPosition);

                        if(!subPath.isEmpty()){
                            pathMap.put(pathPosition, subPath);
                        }

                    }

                    handler.setPathMap(pathMap);

                    Parameter[] parameters = method.getParameters();

                    List<ParameterBind> parameterList = bindParameters(parameters);

                    handler.setParameterList(parameterList);

                    controllerHandlerList.add(handler);


                }

                appMap.put(path, controllerHandlerList);

            }

        }


    private List<ParameterBind> bindParameters(Parameter[] parameters){

        List<ParameterBind> parameterList = new ArrayList<>();

        for (int parameterPos = 0; parameterPos < parameters.length; parameterPos ++){
            Parameter parameter = parameters[parameterPos];
            Annotation[] annotations = parameter.getAnnotations();

            //Pegando apenas primeira anotação, refatorar depois
            if(annotations.length > 0){
                Annotation annotation = annotations[0];
                ParameterBind parameterBind = new ParameterBind(parameter.getType(), parameterPos, annotation);
                parameterList.add(parameterBind);
            }


        }

        return parameterList;
    }

    @Override
    public String toString() {
        return "Starter{" +
                "appMap=" + appMap.toString() +
                '}';
    }

    public Map<String, List<Handler>> getAppMap() {
        return appMap;
    }
}
