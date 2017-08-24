package site.zido.core.beans.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * xml处理管理器实现
 *
 * @author zido
 * @since 17-8-24 下午4:41
 */
public class XmlHandlerManager implements IHandlerManager{
    /**
     * xml处理器map
     */
    private Map<String,List<IHandler>> handlerXmlMap =  new ConcurrentHashMap<>();
    /**
     * 注册处理器
     *
     * @param handler 处理器
     */
    @Override
    public void registerHandler(IHandler handler) {
        List<String> xmlTags = ((IXmlHandler)handler).getHandleXmlTags();
        for (String xmlTag : xmlTags) {
            if(handlerXmlMap.containsKey(xmlTag)){
                List<IHandler> iHandlers = handlerXmlMap.get(xmlTag);
                if(!iHandlers.contains(handler))
                    iHandlers.add(handler);
            } else {
                List<IHandler> list = new ArrayList<>();
                list.add(handler);
                handlerXmlMap.put(xmlTag,list);
            }
        }
    }

    /**
     * 处理器执行
     */
    @Override
    public void resolve() {
        //TODO 扫描xml文件
    }
}
