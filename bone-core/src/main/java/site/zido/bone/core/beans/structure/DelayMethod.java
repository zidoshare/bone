package site.zido.bone.core.beans.structure;

import site.zido.bone.core.utils.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * site.zido.core.beans.structure
 *
 * @author zido
 */
public class DelayMethod {

    private DefProperty[] properties;

    private Method method;
    private Object target;

    public Object execute(Object... params) {
        return ReflectionUtils.execute(method, target, params);
    }


    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public DefProperty[] getProperties() {
        return properties;
    }

    public void setProperties(DefProperty[] properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return method.getName();
    }
}
