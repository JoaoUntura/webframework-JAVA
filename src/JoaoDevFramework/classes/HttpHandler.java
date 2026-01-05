package JoaoDevFramework.classes;

import JoaoDevFramework.annotations.Body;
import JoaoDevFramework.annotations.Request;
import JoaoDevFramework.entities.HttpMethod;
import JoaoDevFramework.entities.HttpRequest;
import JoaoDevFramework.entities.ObjectMethodBind;
import JoaoDevFramework.entities.ParameterBind;

import java.lang.reflect.Method;
import java.util.*;

public class HttpHandler {

    private Map<String, ObjectMethodBind> appMap;


    private final JsonSerializer jsonSerializer = new JsonSerializer();

    public HttpHandler(Map<String, ObjectMethodBind> appMap) {
        this.appMap = appMap;
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

            Object response = handler.invoke(object, parametersInjected.toArray());
            return Optional.ofNullable(response);

        }catch (Exception e){
            System.out.println(e);
        }

        return Optional.empty();


    }
}
