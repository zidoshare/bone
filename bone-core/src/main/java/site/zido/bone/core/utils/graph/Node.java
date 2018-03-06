package site.zido.bone.core.utils.graph;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 图的顶点抽象
 *
 * @author zido
 */
public abstract class Node<T> implements Iterable<Edge> {
    private T data;
    private int index;

    public Node(T data) {
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
