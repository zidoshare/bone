package site.zido.core.beans.handler;

import site.zido.core.beans.structure.Definition;
import site.zido.core.beans.structure.OnlyMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注解处理器管理类
 *
 * @author zido
 * @since 2017/18/21 下午2:18
 */
public class AnnotationHandlerManager implements IHandlerManager {
    private static AnnotationHandlerManager manager = new AnnotationHandlerManager();
    //处理链
    private List<IHandler> handlers = new ArrayList<>();
    /**
     * 注解处理器map
     */
    private Map<Class<?>, List<IHandler>> handlerMap = new ConcurrentHashMap<>();

    public static AnnotationHandlerManager getInstance() {
        return manager;
    }

    /**
     * 遍历处理链，直到能返回一个描述（处理链中，只有一个能处理此注解）
     *
     * @param classzz 类
     * @return 描述
     */
    public OnlyMap<String, Definition> handle(Class<?> classzz) {
        for (IHandler handler : handlers) {
            OnlyMap<String, Definition> result = handler.handle(classzz);
            if (result != null)
                return result;
        }
        return null;
    }

    /**
     * 注册处理器
     *
     * @param handler
     */
    public void registerHandler(IHandler handler) {

        List<Class<?>> annos = ((IAnnotationHandler) handler).getHandleTargets();
        for (Class<?> anno : annos) {
            if (handlerMap.containsKey(anno)) {
                List<IHandler> iHandlers = handlerMap.get(anno);
                if (!iHandlers.contains(handler))
                    iHandlers.add(handler);
            } else {
                List<IHandler> list = new ArrayList<>();
                list.add(handler);
                handlerMap.put(anno, list);
            }
        }
    }

    /**
     * 处理器执行
     */
    @Override
    public void resolve() {

    }

    public void removeHandler(IHandler handler) {
        handlers.remove(handler);
    }
}
