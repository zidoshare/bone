package site.zido.bone.mvc;

import java.lang.reflect.Method;

/**
 * site.zido.bone.mvc
 *
 * @author zido
 */
public class RequestAction {
    private Class<?> controllerClass;
    private Method actionMethod;

    public RequestAction(Class<?> controllerClass, Method actionMethod) {
        this.controllerClass = controllerClass;
        this.actionMethod = actionMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public void setControllerClass(Class<?> controllerClass) {
        this.controllerClass = controllerClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }

    public void setActionMethod(Method actionMethod) {
        this.actionMethod = actionMethod;
    }
}
