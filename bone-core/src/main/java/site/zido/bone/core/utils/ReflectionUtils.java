package site.zido.bone.core.utils;

import site.zido.bone.core.beans.BeanHaveOneConstructorException;
import site.zido.bone.logger.Logger;
import site.zido.bone.logger.impl.LogManager;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * site.zido.core.utils
 *
 * @author zido
 */
public class ReflectionUtils {
    private static Logger logger = LogManager.getLogger(ReflectionUtils.class);

    public static Constructor<?> getConstructor(Class<?> classzz) {
        Constructor<?>[] constructors = classzz.getConstructors();
        if (constructors == null || constructors.length != 1)
            throw new BeanHaveOneConstructorException();

        return constructors[0];
    }

    public static Object newInstance(Class<?> classzz, Object... params) {

        try {
            Constructor<?> constructor = getConstructor(classzz);
            if (params == null || params.length == 0)
                return constructor.newInstance();
            else
                return constructor.newInstance(params);
        } catch (InstantiationException e) {
            logger.error(classzz.getName() + "必须为普通类");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("需要提供唯一的无参构造函数");
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return null;
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
