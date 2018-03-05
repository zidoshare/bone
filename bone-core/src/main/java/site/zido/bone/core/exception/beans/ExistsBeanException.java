package site.zido.bone.core.exception.beans;

import site.zido.bone.core.utils.ValiDateUtils;

public class ExistsBeanException extends FatalBeansException {
    public ExistsBeanException(String id, Object bean) {
        super(ValiDateUtils.isEmpty(id)
                ? String.format("类 [%s] 已经被注入到容器", bean.getClass().getName())
                : String.format("id [%s] 已经被注入到容器", id));
    }

    public ExistsBeanException(Object bean) {
        super(String.format("类 [%s] 已经被注入到容器", bean.getClass().getName()));
    }
}
