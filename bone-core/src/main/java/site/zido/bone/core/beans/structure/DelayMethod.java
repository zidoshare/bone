package site.zido.bone.core.beans.structure;

import site.zido.bone.core.utils.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * site.zido.core.beans.structure
 *
 * @author zido
 * @date 2018 /05/10
 */
public class DelayMethod {

    /**
     * The Properties.
     */
    private DefProperty[] properties;

    /**
     * The Method.
     */
    private Method method;
    /**
     * The Target.
     */
    private Object target;

    /**
     * Execute object.
     *
     * @param params the params
     * @return the object
     */
    public Object execute(Object... params) {
        return ReflectionUtils.execute(method, target, params);
    }


    /**
     * Gets method.
     *
     * @return the method
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Sets method.
     *
     * @param method the method
     */
    public void setMethod(Method method) {
        this.method = method;
    }

    /**
     * Gets target.
     *
     * @return the target
     */
    public Object getTarget() {
        return target;
    }

    /**
     * Sets target.
     *
     * @param target the target
     */
    public void setTarget(Object target) {
        this.target = target;
    }

    /**
     * Get properties def property [ ].
     *
     * @return the def property [ ]
     */
    public DefProperty[] getProperties() {
        return properties;
    }

    /**
     * Sets properties.
     *
     * @param properties the properties
     */
    public void setProperties(DefProperty[] properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return method.getName();
    }
}
