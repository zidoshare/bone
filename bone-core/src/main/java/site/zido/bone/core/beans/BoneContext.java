package site.zido.bone.core.beans;

import site.zido.bone.core.utils.ValiDateUtils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Ioc容器
 *
 * @author zido
 * @since 2017/29/21 下午2:29
 */
public class BoneContext implements BeanFactory, BeanProvider {
    private static BoneContext boneContext = new BoneContext();
    private Map<Class<?>, Map<String, Object>> ioc = new ConcurrentHashMap<>();

    private BoneContext() {
    }

    public static BoneContext getInstance() {
        return boneContext;
    }

    @Override
    public Object getBean(String id) {
        return getBean(id, null);
    }

    @Override
    public Object getBean(String id, Class<?> requireType) {
        if (id == null && requireType == null) {
            return null;
        }
        Map<String, Object> classMap = null;
        if (requireType == null) {
            Collection<Map<String, Object>> values = ioc.values();
            for (Map<String, Object> value : values) {
                if (value.containsKey(id)) {
                    classMap = value;
                    break;
                }
                classMap = null;
            }
        } else {
            classMap = ioc.get(requireType);
        }
        if (classMap == null) {
            return null;
        }
        return classMap.get(id);
    }

    @Override
    public Object getBean(Class<?> requireType) {
        if (requireType == null) {
            return null;
        }
        return getBean("", requireType);
    }

    @Override
    public void register(String id, Object o) {
        if (o == null) {
            return;
        }
        Class<?> requireType = o.getClass();
        if (ValiDateUtils.isEmpty(id)) {
            id = "";
        }
        Map<String, Object> values;
        if (ioc.get(requireType) == null) {
            values = new ConcurrentHashMap<>();
        } else {
            values = ioc.get(requireType);
        }
        if (values.containsKey(id)) {
            throw new ExistsBeanException();
        }
        values.put(id, o);
        ioc.put(requireType, values);
    }

    @Override
    public void register(Object o) {
        if (o == null) {
            return;
        }
        register("", o);
    }
}
