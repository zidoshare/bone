package site.zido.core.beans.handler;

import site.zido.core.beans.structure.Definition;
import site.zido.core.beans.structure.OnlyMap;
/**
 * AbsHandler 抽象处理类，用于组成处理器链来处理注解
 *
 * @author zido
 * @since 2017/16/21 下午2:16
 */
public abstract class AbsHandler {
    /**
     * 处理方法，通过class返回 实体的id和定义 结构
     * @param handle 由调用者传入class
     * @return 定义
     */
    public abstract OnlyMap<String,Definition> handle(Class<?> handle);
}
