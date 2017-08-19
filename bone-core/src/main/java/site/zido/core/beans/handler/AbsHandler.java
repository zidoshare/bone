package site.zido.core.beans.handler;

import site.zido.core.beans.structure.Definition;
import site.zido.core.beans.structure.OnlyMap;

public abstract class AbsHandler {
    public abstract OnlyMap<String,Definition> handle(Class<?> handle);
}
