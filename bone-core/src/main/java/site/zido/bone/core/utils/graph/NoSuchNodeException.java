package site.zido.bone.core.utils.graph;

import site.zido.bone.core.exception.AbstractNestedRuntimeException;

/**
 * site.zido.bone.core.utils.graph
 *
 * @author zido
 * @date 2018 /05/10
 */
public class NoSuchNodeException extends AbstractNestedRuntimeException {
    /**
     * Instantiates a new No such node exception.
     *
     * @param index the index
     */
    public NoSuchNodeException(int index) {
        super("编号为" + index + "的顶点在图中不存在");
    }
}
