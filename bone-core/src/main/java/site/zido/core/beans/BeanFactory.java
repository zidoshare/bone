package site.zido.core.beans;

public interface BeanFactory {
    public Object getBean(String name);

    <T> T getBean(String name, Class<T> requireType);

    <T> T getBean(Class<T> requireType);

}
