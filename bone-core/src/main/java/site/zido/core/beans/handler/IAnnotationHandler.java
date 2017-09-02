package site.zido.core.beans.handler;

import java.util.List;

/**
 * 注解处理器接口
 *
 * @author zido
 * @since 17-8-24 下午4:36
 */
public interface IAnnotationHandler extends IHandler {

    /**
     * @return 返回该处理器会处理的对象
     */
    public abstract List<Class<?>> getHandleTargets();
}
