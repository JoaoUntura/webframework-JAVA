package JoaoDevFramework.classes;

import JoaoDevFramework.annotations.Body;
import JoaoDevFramework.annotations.Request;
import JoaoDevFramework.entities.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class HttpHandler {

    private Map<String, ObjectMethodBind> appMap;


    private final JsonSerializer jsonSerializer = new JsonSerializer();

    public HttpHandler(Map<String, ObjectMethodBind> appMap) {
        this.appMap = appMap;
    }

    public Response handlerFinder(HttpRequest httpRequest) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {

        String path = httpRequest.getPath();
        HttpMethod method = httpRequest.getHttpMethod();

        String routeKey =  path + "-" + method;

        ObjectMethodBind objectMethodBind = appMap.get(routeKey);

        if(objectMethodBind == null) return Response.status(HttpStatus.NOT_FOUND);

        Method handler = objectMethodBind.getMethod();
        Object object = objectMethodBind.getObject();
        List<ParameterBind> parameterBindList = objectMethodBind.getParameterList();

        List<Object> parametersInjected = new ArrayList<>();

        for(ParameterBind parameterBind:parameterBindList){

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

            parametersInjected.add(null);

        }

        Response response = (Response) handler.invoke(object, parametersInjected.toArray());

        if(response == null){
            response = Response.status(HttpStatus.OK);
        }


        return response;

    }
}
