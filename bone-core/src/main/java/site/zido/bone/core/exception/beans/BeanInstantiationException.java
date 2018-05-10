package site.zido.bone.core.exception.beans;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Bean实例化异常
 *
 * @author zido
 * @date 2018 /05/10
 */
public class BeanInstantiationException extends FatalBeansException {
    /**
     * The Bean class.
     */
    private Class<?> beanClass;

    /**
     * The Constructor.
     */
    private Constructor<?> constructor;

    /**
     * The Constructing method.
     */
    private Method constructingMethod;

    /**
     * Instantiates a new Bean instantiation exception.
     *
     * @param beanClass the bean class
     * @param msg       the msg
     */
    public BeanInstantiationException(Class<?> beanClass, String msg) {
        this(beanClass, msg, null);
    }

    /**
     * Instantiates a new Bean instantiation exception.
     *
     * @param beanClass the bean class
     * @param msg       the msg
     * @param cause     the cause
     */
    public BeanInstantiationException(Class<?> beanClass, String msg, Throwable cause) {
        super("实例化失败 [" + beanClass.getName() + "]: " + msg, cause);
        this.beanClass = beanClass;
    }

    /**
     * Instantiates a new Bean instantiation exception.
     *
     * @param constructor the constructor
     * @param msg         the msg
     * @param cause       the cause
     */
    public BeanInstantiationException(Constructor<?> constructor, String msg, Throwable cause) {
        super("实例化失败 [" + constructor.getDeclaringClass().getName() + "]: " + msg, cause);
        this.beanClass = constructor.getDeclaringClass();
        this.constructor = constructor;
    }

    /**
     * Instantiates a new Bean instantiation exception.
     *
     * @param constructingMethod the constructing method
     * @param msg                the msg
     * @param cause              the cause
     */
    public BeanInstantiationException(Method constructingMethod, String msg, Throwable cause) {
        super("实例化失败 [" + constructingMethod.getReturnType().getName() + "]: " + msg, cause);
        this.beanClass = constructingMethod.getReturnType();
        this.constructingMethod = constructingMethod;
    }

    /**
     * Gets bean class.
     *
     * @return the bean class
     */
    public Class<?> getBeanClass() {
        return beanClass;
    }

    /**
     * Sets bean class.
     *
     * @param beanClass the bean class
     */
    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    /**
     * Gets constructor.
     *
     * @return the constructor
     */
    public Constructor<?> getConstructor() {
        return constructor;
    }

    /**
     * Sets constructor.
     *
     * @param constructor the constructor
     */
    public void setConstructor(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    /**
     * Gets constructing method.
     *
     * @return the constructing method
     */
    public Method getConstructingMethod() {
        return constructingMethod;
    }

    /**
     * Sets constructing method.
     *
     * @param constructingMethod the constructing method
     */
    public void setConstructingMethod(Method constructingMethod) {
        this.constructingMethod = constructingMethod;
    }
}
