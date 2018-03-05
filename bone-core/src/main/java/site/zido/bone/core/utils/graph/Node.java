package site.zido.bone.core.utils.graph;

/**
 * 图的顶点
 *
 * @author zido
 */
public abstract class Node<T> {
    private T data;
    private int index;

    public Node(T data){
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
