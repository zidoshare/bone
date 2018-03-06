package site.zido.bone.core.utils.graph;

import site.zido.bone.core.exception.NestedRuntimeException;

/**
 * site.zido.bone.core.utils.graph
 *
 * @author zido
 */
public class NoSuchNodeException extends NestedRuntimeException {
    public NoSuchNodeException(int index) {
        super("编号为" + index + "的顶点在图中不存在");
    }
}
