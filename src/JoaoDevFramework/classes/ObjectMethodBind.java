package JoaoDevFramework.classes;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ObjectMethodBind {

    private Object object;

    private Method method;


    public ObjectMethodBind(Object object, Method method) {
        this.object = object;
        this.method = method;
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
}
