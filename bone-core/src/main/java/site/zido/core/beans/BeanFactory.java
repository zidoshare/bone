package site.zido.core.beans;

/**
 * Bean工厂
 *
 * @author zido
 * @since 2017/27/21 下午2:27
 */
public interface BeanFactory {
    Object getBean(String name);

    <T> T getBean(String name, Class<T> requireType);

    <T> T getBean(Class<T> requireType);
}
