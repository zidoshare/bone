package site.zido.core.utils;

import site.zido.bone.logger.Logger;
import site.zido.bone.logger.impl.LogManager;
import site.zido.core.beans.BeanHaveOneStrucureException;

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
        Constructor<?> constructor;
        try {
            constructor = classzz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new BeanHaveOneStrucureException();
        }
        return constructor;
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
            e.printStackTrace();
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
}
