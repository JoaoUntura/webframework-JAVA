package JoaoDevFramework.entities;

public class ParameterBind {

    private Class<?> type;
    private Integer position;
    private Object annotationObject;

    public ParameterBind(Class<?> type, Integer position, Object annotationObject) {
        this.type = type;
        this.position = position;
        this.annotationObject = annotationObject;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
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
