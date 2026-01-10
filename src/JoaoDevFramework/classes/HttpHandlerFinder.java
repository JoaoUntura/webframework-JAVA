package JoaoDevFramework.classes;

import JoaoDevFramework.annotations.Body;
import JoaoDevFramework.annotations.PathVariable;
import JoaoDevFramework.annotations.Request;
import JoaoDevFramework.entities.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class HttpHandlerFinder {

    private final Map<String, List<Handler>> appMap;


    private final JsonSerializer jsonSerializer = new JsonSerializer();

    public HttpHandlerFinder(Map<String, List<Handler>> appMap) {
        this.appMap = appMap;
    }

    private record PathFinderResponse(Handler handler, Map<String, String> pathVariables){};

    public Response handlerFinder(HttpRequest httpRequest) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {

        String httpRequestPath = httpRequest.getPath();

        if(httpRequestPath == null) return Response.status(HttpStatus.NOT_FOUND);

        List<String> httpRequestSubPaths = List.of(httpRequestPath.split("/"));

        String routeKey = httpRequestSubPaths.get(1);

        HttpMethod requestHttpMethod = httpRequest.getHttpMethod();

        List<Handler> handlerList = appMap.get(routeKey);

        if(handlerList == null) return Response.status(HttpStatus.NOT_FOUND);

        //Path finding aqui
        httpRequestSubPaths = httpRequestSubPaths.subList(2, httpRequestSubPaths.size());

       PathFinderResponse pathFinderResponse = pathFinder(handlerList, requestHttpMethod, httpRequestSubPaths);
       Handler handler = pathFinderResponse.handler;
       Map<String, String> pathVariables = pathFinderResponse.pathVariables;

        if(handler == null) return Response.status(HttpStatus.NOT_FOUND);

        Method methodHandler = handler.getMethodHandler();
        Object object = handler.getControllerObject();
        List<ParameterBind> parameterBindList = handler.getParameterList();

        List<Object> parametersInjected = new ArrayList<>();

        for(ParameterBind parameterBind:parameterBindList){

            //Usar atributo .getPosition para injeção mais precisa refatorar
            if(parameterBind.getAnnotationObject() instanceof Body){

                Class<?> parameterType = parameterBind.getType();

                String body = httpRequest.getBody();

                if(body == null || body.isEmpty()) continue;

                Object serializedBody = jsonSerializer.serialize(parameterType, httpRequest.getBody());

                parametersInjected.add(serializedBody);
                continue;

            }

            if (parameterBind.getAnnotationObject() instanceof Request){
                parametersInjected.add(httpRequest);
                continue;
            }

            if(parameterBind.getAnnotationObject() instanceof PathVariable pathVariable){

                String key = pathVariable.name();

                String value = pathVariables.get(key);
                Object valueCasted = jsonSerializer.objectToType(parameterBind.getType(), value);
                parametersInjected.add(valueCasted);
                continue;
            }

            parametersInjected.add(null);

        }

        Response response = (Response) methodHandler.invoke(object, parametersInjected.toArray());

        if(response == null){
            response = Response.status(HttpStatus.OK);
        }

        return response;

    }

    private PathFinderResponse pathFinder(List<Handler> handlerList, HttpMethod requestHttpMethod, List<String> httpRequestSubPaths){
        Handler handler = null;
        Map<String, String> pathVariables = new HashMap<>();
        for (Handler controllerHandler:handlerList){

            HttpMethod httpMethod = controllerHandler.getHttpMethod();

            if(!requestHttpMethod.equals(httpMethod)) continue;

            Map<Integer, String> pathMap = controllerHandler.getPathMap();

            if(httpRequestSubPaths.size() == pathMap.size()){
                boolean isHandler = true;
                for(int i = 0; i < pathMap.size(); i ++){

                    String handlerPath = pathMap.get(i);
                    String requestPath = httpRequestSubPaths.get(i);

                    if(handlerPath.startsWith("{") && handlerPath.endsWith("}")){
                        pathVariables.put(handlerPath.replace("{", "").replace("}", ""), requestPath);
                    }else{

                        if(!requestPath.equals(handlerPath)) {
                            isHandler = false;
                            break;
                        }

                    }

                }
                if(isHandler) handler = controllerHandler;

            }

        }
        return new PathFinderResponse(handler, pathVariables);
    }
}
