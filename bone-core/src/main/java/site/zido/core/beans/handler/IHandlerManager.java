package site.zido.core.beans.handler;

/**
 * 处理器管理器接口
 *
 * @author zido
 * @since 17-8-24 下午4:31
 */
public interface IHandlerManager {
    /**
     * 注册处理器
     *
     * @param handler 处理器
     */
    public void registerHandler(IHandler handler);

    /**
     * 处理器执行
     */
    public void resolve();
}
