package JoaoDevFramework.classes;

import JoaoDevFramework.annotations.Body;
import JoaoDevFramework.annotations.PathVariable;
import JoaoDevFramework.annotations.QueryParam;
import JoaoDevFramework.annotations.Request;
import JoaoDevFramework.entities.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class HttpHandlerFinder {

    private final Map<String, List<Handler>> appMap;

    private final JsonSerializer jsonSerializer;

    public HttpHandlerFinder(Map<String, List<Handler>> appMap, JsonSerializer jsonSerializer) {
        this.appMap = appMap;
        this.jsonSerializer = jsonSerializer;
    }

    private record PathFinderResponse(Handler handler, Map<String, String> pathVariables) {
    }

    ;

    public Response handlerFinder(HttpRequest httpRequest) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {

        String mainPath = httpRequest.getMainPath();

        if (mainPath == null) return Response.status(HttpStatus.NOT_FOUND);

        HttpMethod requestHttpMethod = httpRequest.getHttpMethod();

        List<Handler> handlerList = appMap.get(mainPath);

        if (handlerList == null) return Response.status(HttpStatus.NOT_FOUND);

        //Path finding aqui
        PathFinderResponse pathFinderResponse = pathFinder(handlerList, requestHttpMethod, httpRequest.getSubPaths());
        Handler handler = pathFinderResponse.handler;

        if (handler == null) return Response.status(HttpStatus.NOT_FOUND);
        Map<String, String> pathVariables = pathFinderResponse.pathVariables;

        Method methodHandler = handler.getMethodHandler();
        Object object = handler.getControllerObject();
        List<ParameterBind> parameterBindList = handler.getParameterList();
        List<Object> parametersInjected = injectParams(parameterBindList, httpRequest, pathVariables);

        Response response = (Response) methodHandler.invoke(object, parametersInjected.toArray());

        if (response == null) {
            response = Response.status(HttpStatus.OK);
        }

        return response;

    }

    private List<Object> injectParams(List<ParameterBind> parameterBindList, HttpRequest httpRequest,  Map<String, String> pathVariables) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<Object> parametersInjected = new ArrayList<>();

        for (ParameterBind parameterBind : parameterBindList) {

            //Usar atributo .getPosition para injeção mais precisa refatorar
            if (parameterBind.getAnnotationObject() instanceof Body) {

                Class<?> parameterType = parameterBind.getType();

                String body = httpRequest.getBody();

                if (body == null || body.isEmpty()) continue;

                Object serializedBody = jsonSerializer.serialize(parameterType, httpRequest.getBody());

                parametersInjected.add(serializedBody);
                continue;

            }

            if (parameterBind.getAnnotationObject() instanceof Request) {
                parametersInjected.add(httpRequest);
                continue;
            }

            if (parameterBind.getAnnotationObject() instanceof PathVariable pathVariable) {

                String key = pathVariable.name();

                String value = pathVariables.get(key);
                Object valueCasted = jsonSerializer.objectToType(parameterBind.getType(), value);
                parametersInjected.add(valueCasted);
                continue;
            }

            if(parameterBind.getAnnotationObject() instanceof QueryParam queryParam){

                String paramValue = httpRequest.getQueryParams().get(queryParam.name());
                if(paramValue != null){
                    Object valueCasted = jsonSerializer.objectToType(parameterBind.getType(), paramValue);
                    parametersInjected.add(valueCasted);
                    continue;
                }


            }

            parametersInjected.add(null);

        }
        return parametersInjected;
    }

    private PathFinderResponse pathFinder(List<Handler> handlerList, HttpMethod requestHttpMethod, List<String> httpRequestSubPaths) {
        Handler handler = null;

        Map<String, String> pathVariables = new HashMap<>();
        for (Handler controllerHandler : handlerList) {

            HttpMethod httpMethod = controllerHandler.getHttpMethod();

            if (!requestHttpMethod.equals(httpMethod)) continue;

            Map<Integer, String> pathMap = controllerHandler.getPathMap();

            if (httpRequestSubPaths.size() == pathMap.size()) {
                boolean isHandler = true;

                for (int i = 0; i < pathMap.size(); i++) {

                    String handlerPath = pathMap.get(i);
                    String requestPath = httpRequestSubPaths.get(i);


                    if (handlerPath.startsWith("{") && handlerPath.endsWith("}")) {
                        pathVariables.put(handlerPath.replace("{", "").replace("}", ""), requestPath);
                    } else {

                        if (!requestPath.equals(handlerPath)) {
                            isHandler = false;

                            break;
                        }

                    }


                }
                if (isHandler) handler = controllerHandler;
                break;

            }

        }
        return new PathFinderResponse(handler, pathVariables);
    }

}
