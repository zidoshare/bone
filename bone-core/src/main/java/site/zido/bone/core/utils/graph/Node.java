package site.zido.bone.core.utils.graph;

/**
 * 图的顶点抽象
 *
 * @param <T> the type parameter
 * @author zido
 * @date 2018 /05/10
 */
public abstract class Node<T> implements Iterable<Edge> {
    /**
     * The Data.
     */
    private T data;
    /**
     * The Index.
     */
    private int index;

    /**
     * Instantiates a new Node.
     *
     * @param data the data
     */
    public Node(T data) {
        this.data = data;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public T getData() {
        return data;
    }

    /**
     * Sets data.
     *
     * @param data the data
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Gets index.
     *
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets index.
     *
     * @param index the index
     */
    public void setIndex(int index) {
        this.index = index;
    }
}
