package site.zido.bone.core.utils.graph;

/**
 * 有向图节点抽象
 *
 * @author zido
 */
public abstract class ArcNode<T> extends Node<T> {
    public ArcNode(T data) {
        super(data);
    }
}
