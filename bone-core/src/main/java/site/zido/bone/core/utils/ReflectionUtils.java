package site.zido.bone.core.utils;

import site.zido.bone.core.exception.beans.BeanHaveOneConstructorException;
import site.zido.bone.core.exception.beans.BeanInstantiationException;
import site.zido.bone.logger.Logger;
import site.zido.bone.logger.impl.LogManager;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * site.zido.core.utils
 *
 * @author zido
 */
public class ReflectionUtils {
    private static Logger logger = LogManager.getLogger(ReflectionUtils.class);

    /**
     * 获取类构造器
     *
     * @param classzz 类
     * @return 构造器
     */
    public static <T> Constructor<T> getOnlyConstructor(Class<T> classzz) {
        Constructor<?>[] ctrs = classzz.getDeclaredConstructors();
        if (ctrs.length != 1) {
            throw new BeanHaveOneConstructorException(classzz);
        }
        return (Constructor<T>) ctrs[0];
    }

    /**
     * 实例化类，通过默认无参方法(只能是公有构造方法)
     *
     * @param clazz 类
     * @param <T>   类型
     * @return 实例
     */
    public static <T> T newInstance(Class<T> clazz) {
        Assert.notNull(clazz, "实例化时提供的类不能为空");
        if (clazz.isInterface()) {
            throw new BeanInstantiationException(clazz, "给定的类为接口");
        }
        try {
            return clazz.newInstance();
        } catch (InstantiationException ex) {
            throw new BeanInstantiationException(clazz, "给定的类可能是抽象类", ex);
        } catch (IllegalAccessException ex) {
            throw new BeanInstantiationException(clazz, "给定的类的构造器可能是私有的", ex);
        }
    }

    /**
     * 实例化类，通过默认无参方法(包括私有构造方法)
     *
     * @param clazz 类
     * @param <T>   类型
     * @return 实例
     */
    public static <T> T instantiateClass(Class<T> clazz) {
        Assert.notNull(clazz, "提供的类不能为空");
        if (clazz.isInterface()) {
            throw new BeanInstantiationException(clazz, "给定的类为接口");
        }
        try {
            return instantiateClass(clazz.getDeclaredConstructor());
        } catch (NoSuchMethodException ex) {
            throw new BeanInstantiationException(clazz, "未找到默认构造器", ex);
        }
    }

    public static <T> T instantiateClass(Constructor<T> ctor, Object... params) {
        Assert.notNull(ctor, "构造器不能为空");
        try {
            ReflectionUtils.makeAccessible(ctor);
            return ctor.newInstance(params);
        } catch (InstantiationException ex) {
            throw new BeanInstantiationException(ctor, "提供的类为抽象类、接口?", ex);
        } catch (IllegalAccessException ex) {
            throw new BeanInstantiationException(ctor, "构造器是否有访问权限?", ex);
        } catch (IllegalArgumentException ex) {
            throw new BeanInstantiationException(ctor, "构造器参数错误", ex);
        } catch (InvocationTargetException ex) {
            throw new BeanInstantiationException(ctor, "构造器抛出异常", ex.getTargetException());
        }
    }

    public static void makeAccessible(Constructor<?> ctor) {
        if ((!Modifier.isPublic(ctor.getModifiers()) ||
                !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {
            ctor.setAccessible(true);
        }
    }

    public static Object execute(Method method, Object target, Object... params) {
        try {
            if (params == null || params.length == 0) {
                return method.invoke(target);
            }
            return method.invoke(target, params);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("参数错误", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("类实例不能为null", e);
        }
    }

    public static String getSimpleName(Class<?> classzz) {
        return classzz.getSimpleName().substring(0, 1).toLowerCase() + classzz.getSimpleName().substring(1);
    }

    public static <T extends Annotation> T getAnnotation(Class<?> targetClass, Class<T> targetAnnotationType) {
        Target target = targetAnnotationType.getAnnotation(Target.class);
        ElementType[] elementTypes = target.value();
        boolean toTop = false;
        for (ElementType elementType : elementTypes) {
            if (elementType.equals(ElementType.ANNOTATION_TYPE)) {
                toTop = true;
                break;
            }
        }
        T annotation = targetClass.getAnnotation(targetAnnotationType);
        if (annotation != null) {
            return annotation;
        }
        if (!toTop) {
            return null;
        }
        Annotation[] annotations = targetClass.getAnnotations();
        for (Annotation other : annotations) {
            T result = getAnnotation(other.getClass(), targetAnnotationType);
            if (result != null)
                return result;
        }
        return null;
    }

    public static Method getSetterMethod(Object obj, String name) {
        name = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
        Method[] methods = obj.getClass().getMethods();
        for (Method m : methods) {
            if (m.getName().equals(name)) {
                return m;
            }
        }
        return null;
    }

    public static void setField(Object target, Method method, Object... value) {
        if (method == null) {
            throw new RuntimeException("没有相应的setter方法:" + target.getClass().getName() + "." + method.getName());
        }
        Class<?>[] types = method.getParameterTypes();
        if (types.length != value.length) {
            throw new IllegalArgumentException("参数不匹配：" + target.getClass().getName() + "." + method.getName() + " 不能匹配 " + value);
        }
        Object args[] = new Object[types.length];
        int i = 0;
        for (Class<?> type : types) {

            switch (type.getSimpleName()) {
                case "Integer":
                    args[i] = Integer.valueOf(value[i++].toString());
                    break;
                case "Double":
                    args[i] = Double.valueOf(value[i++].toString());
                    break;
                case "Float":
                    args[i] = Float.valueOf(value[i++].toString());
                    break;
                case "Boolean":
                    args[i] = Boolean.valueOf(value[i++].toString());
                    break;
                case "String":
                    args[i] = value[i++].toString();
                    break;
                default:
                    args[i] = value[i++];
            }
        }
        try {
            method.invoke(target, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("属性名称不合法或者没有相应的setter方法：" + target.getClass().getName() + "." + method.getName());
        }
    }

}
