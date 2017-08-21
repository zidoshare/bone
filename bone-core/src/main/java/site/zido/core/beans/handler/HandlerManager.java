package site.zido.core.beans.handler;

import site.zido.core.beans.structure.Definition;
import site.zido.core.beans.structure.OnlyMap;

import java.util.ArrayList;
import java.util.List;
/**
 * 处理器管理类
 *
 * @author zido
 * @since 2017/18/21 下午2:18
 */
public class HandlerManager {
    private static HandlerManager manager = new HandlerManager();
    //处理链
    private List<AbsHandler> handlers = new ArrayList<>();

    public static HandlerManager getInstance(){
        return manager;
    }

    public OnlyMap<String,Definition> handle(Class<?> classzz){
        for (AbsHandler handler : handlers) {
            OnlyMap<String, Definition> result = handler.handle(classzz);
            if(result != null)
                return result;
        }
        return null;
    }

    public void registerHandler(AbsHandler handler){
        handlers.add(handler);
    }

    public void removeHandler(AbsHandler handler){
        handlers.remove(handler);
    }

}
