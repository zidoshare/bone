package site.zido.core.beans;

public interface BeanProvider {
    void register(String name,Class<?> requireType,Object o);
    void register(String name,Object o);
    void register(Class<?> requireType);
}
