package site.zido.bone.core.utils;

import site.zido.bone.core.beans.structure.DefProperty;

import java.util.Collection;
import java.util.Map;

public class CollectionUtils {

    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }
}
