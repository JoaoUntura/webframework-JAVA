package JoaoDevFramework.classes;

import java.lang.reflect.Type;

public class ParameterBind {

    private Type type;
    private Integer position;
    private Object annotationObject;

    public ParameterBind(Type type, Integer position, Object annotationObject) {
        this.type = type;
        this.position = position;
        this.annotationObject = annotationObject;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Object getAnnotationObject() {
        return annotationObject;
    }

    public void setAnnotationObject(Object annotationObject) {
        this.annotationObject = annotationObject;
    }

    @Override
    public String toString() {
        return "ParameterBind{" +
                "type=" + type +
                ", position=" + position +
                ", annotationObject=" + annotationObject +
                '}';
    }
}
