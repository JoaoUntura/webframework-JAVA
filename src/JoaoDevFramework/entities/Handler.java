package JoaoDevFramework.entities;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class Handler {



    private Map<Integer, String> pathMap;

    private HttpMethod httpMethod;

    private Object controllerObject;

    private Method methodHandler;

    private List<ParameterBind> parameterList;


    public Handler(Object controllerObject, Method methodHandler){
        this.controllerObject = controllerObject;
        this.methodHandler = methodHandler;
    }

    public Map<Integer, String> getPathMap() {
        return pathMap;
    }

    public void setPathMap(Map<Integer, String> pathMap) {
        this.pathMap = pathMap;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Object getControllerObject() {
        return controllerObject;
    }

    public void setControllerObject(Object controllerObject) {
        this.controllerObject = controllerObject;
    }

    public Method getMethodHandler() {
        return methodHandler;
    }

    public void setMethodHandler(Method methodHandler) {
        this.methodHandler = methodHandler;
    }

    public List<ParameterBind> getParameterList() {
        return parameterList;
    }

    public void setParameterList(List<ParameterBind> parameterList) {
        this.parameterList = parameterList;
    }

    @Override
    public String toString() {
        return "Handler{" +
                "pathMap=" + pathMap +
                ", httpMethod=" + httpMethod +
                ", controllerObject=" + controllerObject +
                ", methodHandler=" + methodHandler +
                ", parameterList=" + parameterList +
                '}';
    }
}
