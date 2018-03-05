package site.zido.bone.core.utils.graph;

import site.zido.bone.core.exception.NestedRuntimeException;

public class NodeExistsException extends NestedRuntimeException {
    public NodeExistsException(GraphNode node) {
        super("节点重复:" + node);
    }
}
