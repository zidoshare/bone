package site.zido.bone.core.beans;

/**
 * Bean工厂
 *
 * @author zido
 * @date 2018 /05/10
 * @since 2017 /27/21 下午2:27
 */
public interface BeanFactory {
    /**
     * Gets bean.
     *
     * @param name the name
     * @return the bean
     */
    Object getBean(String name);

    /**
     * Gets bean.
     *
     * @param id          the id
     * @param requireType the require type
     * @return the bean
     */
    Object getBean(String id, Class<?> requireType);

    /**
     * Gets bean.
     *
     * @param requireType the require type
     * @return the bean
     */
    Object getBean(Class<?> requireType);
}
