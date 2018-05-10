package site.zido.bone.core.exception.beans;

import site.zido.bone.core.utils.ValiDateUtils;

/**
 * The type Exists bean exception.
 *
 * @author zido
 * @date 2018 /05/10
 */
public class ExistsBeanException extends FatalBeansException {
    /**
     * Instantiates a new Exists bean exception.
     *
     * @param id   the id
     * @param bean the bean
     */
    public ExistsBeanException(String id, Object bean) {
        super(ValiDateUtils.isEmpty(id)
                ? String.format("{类:[%s]} 已经被注入到容器", bean.getClass().getName())
                : String.format("{id:[%s] 类:[%s]} 已经被注入到容器", id, bean.getClass().getName()));
    }

    /**
     * Instantiates a new Exists bean exception.
     *
     * @param bean the bean
     */
    public ExistsBeanException(Object bean) {
        super(String.format("{类:[%s]} 已经被注入到容器", bean.getClass().getName()));
    }
}
