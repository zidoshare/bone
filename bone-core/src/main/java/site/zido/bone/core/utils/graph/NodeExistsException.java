package site.zido.bone.core.utils.graph;

import site.zido.bone.core.exception.AbstractNestedRuntimeException;

/**
 * The type Node exists exception.
 *
 * @author zido
 * @date 2018 /05/10
 */
public class NodeExistsException extends AbstractNestedRuntimeException {
    /**
     * Instantiates a new Node exists exception.
     *
     * @param node the node
     */
    public NodeExistsException(AbstractGraphNode node) {
        super("节点重复:" + node);
    }
}
