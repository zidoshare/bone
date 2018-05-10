package site.zido.bone.core.utils.graph;

/**
 * 有向图节点抽象
 *
 * @param <T> the type parameter
 * @author zido
 * @date 2018 /05/10
 */
public abstract class AbstractArcNode<T> extends Node<T> {
    /**
     * Instantiates a new AbstractArc node.
     *
     * @param data the data
     */
    public AbstractArcNode(T data) {
        super(data);
    }
}
