package site.zido.bone.core.beans;

/**
 * Bean工厂
 *
 * @author zido
 * @since 2017/27/21 下午2:27
 */
public interface BeanFactory {
    Object getBean(String name);

    Object getBean(String id, Class<?> requireType);

    Object getBean(Class<?> requireType);
}
