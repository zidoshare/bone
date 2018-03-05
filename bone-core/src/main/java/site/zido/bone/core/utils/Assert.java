package site.zido.bone.core.utils;

import java.util.Collection;
import java.util.Map;

/**
 * 断言类
 *
 * @author zido
 */
public abstract class Assert {
    /**
     * 断言：不能为空
     *
     * @param object 对象
     * @param msg    信息
     */
    public static void notNull(Object object, String msg) {
        if (object == null)
            throw new IllegalArgumentException(msg);
    }

    public static void notEmpty(Object[] array, String message) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void noNullElements(Object[] array, String message) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new IllegalArgumentException(message);
                }
            }
        }
    }

    public static void notEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }


    public static void notEmpty(Map<?, ?> map, String message) {
        if (CollectionUtils.isEmpty(map)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void hasLength(String path, String message) {
        if (path == null || path.length() == 0) {
            throw new IllegalArgumentException(message);
        }
    }
}
