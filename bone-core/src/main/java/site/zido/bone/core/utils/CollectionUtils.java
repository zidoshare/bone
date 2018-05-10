package site.zido.bone.core.utils;

import java.util.Collection;
import java.util.Map;

/**
 * The type Collection utils.
 *
 * @author zido
 * @date 2018 /05/10
 */
public class CollectionUtils {

    /**
     * Is empty boolean.
     *
     * @param collection the collection
     * @return the boolean
     */
    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * Is empty boolean.
     *
     * @param map the map
     * @return the boolean
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }
}
