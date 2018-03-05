package site.zido.bone.core.exception.beans;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Bean实例化异常
 *
 * @author zido
 */
public class BeanInstantiationException extends FatalBeansException {
    private Class<?> beanClass;

    private Constructor<?> constructor;

    private Method constructingMethod;

    public BeanInstantiationException(Class<?> beanClass, String msg) {
        this(beanClass, msg, null);
    }

    public BeanInstantiationException(Class<?> beanClass, String msg, Throwable cause) {
        super("实例化失败 [" + beanClass.getName() + "]: " + msg, cause);
        this.beanClass = beanClass;
    }

    public BeanInstantiationException(Constructor<?> constructor, String msg, Throwable cause) {
        super("实例化失败 [" + constructor.getDeclaringClass().getName() + "]: " + msg, cause);
        this.beanClass = constructor.getDeclaringClass();
        this.constructor = constructor;
    }

    public BeanInstantiationException(Method constructingMethod, String msg, Throwable cause) {
        super("实例化失败 [" + constructingMethod.getReturnType().getName() + "]: " + msg, cause);
        this.beanClass = constructingMethod.getReturnType();
        this.constructingMethod = constructingMethod;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public Constructor<?> getConstructor() {
        return constructor;
    }

    public void setConstructor(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    public Method getConstructingMethod() {
        return constructingMethod;
    }

    public void setConstructingMethod(Method constructingMethod) {
        this.constructingMethod = constructingMethod;
    }
}
