package site.zido.bone.core.utils.graph;

/**
 * 顶点的十字链表实现
 *
 * @author zido
 */
public class OrthogonalNode<T> extends Node<T> {
    private Arc firstIn;
    private Arc firstOut;

    public OrthogonalNode(T data) {
        super(data);
    }

    public Arc getFirstIn() {
        return firstIn;
    }

    public void setFirstIn(Arc firstIn) {
        this.firstIn = firstIn;
    }

    public Arc getFirstOut() {
        return firstOut;
    }

    public void setFirstOut(Arc firstOut) {
        this.firstOut = firstOut;
    }
}
