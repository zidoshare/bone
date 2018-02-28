package site.zido.core.utils;

import site.zido.bone.logger.Logger;
import site.zido.bone.logger.impl.LogManager;
import site.zido.core.beans.BeanHaveOneConstructorException;
import site.zido.core.beans.structure.BeanConstruction;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
}
