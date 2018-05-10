package site.zido.bone.core.beans;

/**
 * Bean注册接口
 *
 * @author zido
 * @date 2018 /05/10
 * @since 2017 /28/21 下午2:28
 */
public interface BeanProvider {

    /**
     * Register.
     *
     * @param name the name
     * @param o    the o
     */
    void register(String name, Object o);

    /**
     * Register.
     *
     * @param o the o
     */
    void register(Object o);
}
