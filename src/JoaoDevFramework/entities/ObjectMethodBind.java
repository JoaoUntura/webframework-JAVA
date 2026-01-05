package JoaoDevFramework.entities;

import java.lang.reflect.Method;
import java.util.List;

public class ObjectMethodBind {

    private Object object;

    private Method method;

    private List<ParameterBind> parameterMap;



    public ObjectMethodBind(Object object, Method method, List<ParameterBind> parameterMap) {
        this.object = object;
        this.method = method;
        this.parameterMap = parameterMap;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public List<ParameterBind> getParameterList() {
        return parameterMap;
    }

    public void setParameterMap(List<ParameterBind> parameterMap) {
        this.parameterMap = parameterMap;
    }

    @Override
    public String toString() {
        return "ObjectMethodBind{" +
                "object=" + object +
                ", method=" + method +
                ", parameterMap=" + parameterMap +
                '}';
    }
}
