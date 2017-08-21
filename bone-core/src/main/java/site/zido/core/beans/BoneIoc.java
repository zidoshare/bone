package site.zido.core.beans;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
/**
 * Ioc容器
 *
 * @author zido
 * @since 2017/29/21 下午2:29
 */
public class BoneIoc implements BeanFactory,BeanProvider{
    private static BoneIoc boneIoc = new BoneIoc();
    public static BoneIoc getInstance(){
        return boneIoc;
    }

    private Map<String,Object> ioc = new HashMap<>();
    @Override
    public Object getBean(String name) {
        return ioc.get(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> requireType) {
        return (T)ioc.get(name);
    }

    @Override
    public <T> T getBean(Class<T> requireType) {
        Collection<Object> values = ioc.values();
        for (Object value : values) {
            if(value.getClass().getName().equals(requireType.getName()))
                return (T)value;
        }
        return null;
    }

    @Override
    public void register(String name, Class<?> requireType, Object o) {
        ioc.put(name,o);
    }

    @Override
    public void register(String name, Object o) {
        ioc.put(name,o);
    }

    @Override
    public void register(Class<?> requireType,Object o) {
        String name = requireType.getName();
        ioc.put(name,o);
    }
}
