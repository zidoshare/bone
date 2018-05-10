package site.zido.bone.core.utils.graph;

import site.zido.bone.core.exception.AbstractNestedRuntimeException;

/**
 * site.zido.bone.core.utils.graph
 *
 * @author zido
 * @date 2018 /05/10
 */
public class NodeNotRootException extends AbstractNestedRuntimeException {
    /**
     * Instantiates a new Node not root exception.
     *
     * @param node the node
     */
    public NodeNotRootException(AbstractGraphNode node){
        super(node.toString());
    }
}
