package site.zido.bone.core.utils.graph;

import site.zido.bone.core.exception.NestedRuntimeException;

/**
 * site.zido.bone.core.utils.graph
 *
 * @author zido
 */
public class NodeNotRootException extends NestedRuntimeException {
    public NodeNotRootException(GraphNode node){
        super(node.toString());
    }
}
