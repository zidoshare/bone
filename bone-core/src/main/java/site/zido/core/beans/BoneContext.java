package site.zido.core.beans;

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
    private Map<String, Object> ioc = new ConcurrentHashMap<>();

    private BoneContext() {
    }

    public static BoneContext getInstance() {
        return boneContext;
    }

    @Override
    public Object getBean(String name) {
        return ioc.get(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> requireType) {
        return (T) ioc.get(name);
    }

    @Override
    public <T> T getBean(Class<T> requireType) {
        Collection<Object> values = ioc.values();
        for (Object value : values) {
            if (value.getClass().getName().equals(requireType.getName()))
                return (T) value;
        }
        return null;
    }

    @Override
    public void register(String name, Class<?> requireType, Object o) {
        ioc.put(name, o);
    }

    @Override
    public void register(String name, Object o) {
        ioc.put(name, o);
    }

    @Override
    public void register(Class<?> requireType, Object o) {
        String name = requireType.getName();
        ioc.put(name, o);
    }
}
