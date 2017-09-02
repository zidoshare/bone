package site.zido.core.beans.handler;

import java.util.List;

/**
 * xml处理器接口
 *
 * @author zido
 * @since 17-8-24 下午4:38
 */
public interface IXmlHandler extends IHandler {
    /**
     * @return 返回该处理器会处理的xml标签
     */
    public abstract List<String> getHandleXmlTags();
}
