package site.zido.bone.core.utils;

import site.zido.bone.core.beans.structure.DefProperty;
import site.zido.bone.core.beans.structure.Definition;

import java.util.Objects;

/**
 * 解析工具类
 *
 * @author zido
 * @date 2018 /05/10
 */
public class LexUtils {
    /**
     * Def equals boolean.
     *
     * @param property   the property
     * @param definition the definition
     * @return the boolean
     */
    public static boolean defEquals(DefProperty property, Definition definition) {
        if (property == null || definition == null) {
            return false;
        }
        String ref = property.getRef();
        Class<?> requireType = property.getType();

        String id = definition.getId();
        Class<?> providerType = definition.getType();
        return Objects.equals(ref, id) && (requireType == null || Objects.equals(providerType, requireType));
    }
}
