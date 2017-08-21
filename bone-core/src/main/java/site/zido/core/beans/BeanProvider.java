package site.zido.core.beans;
/**
 * Bean注册接口
 *
 * @author zido
 * @since 2017/28/21 下午2:28
 */
public interface BeanProvider {
    void register(String name,Class<?> requireType,Object o);
    void register(String name,Object o);
    void register(Class<?> requireType,Object o);
}
