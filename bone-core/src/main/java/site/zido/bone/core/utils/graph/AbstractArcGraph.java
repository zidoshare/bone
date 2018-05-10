package site.zido.bone.core.utils.graph;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 有向图接口
 *
 * @param <T> the type parameter
 * @author zido
 * @date 2018 /05/10
 */
public abstract class AbstractArcGraph<T> implements Graph<T> {

    /**
     * 顶点集合
     */
    private Map<Integer, Node<T>> nodeMap;
    /**
     * The Id creator.
     */
    private IdCreator idCreator = new IdCreator();

    /**
     * Instantiates a new AbstractArc graph.
     */
    public AbstractArcGraph() {
        nodeMap = new LinkedHashMap<>();
    }

    /**
     * Instantiates a new AbstractArc graph.
     *
     * @param initialCapacity the initial capacity
     */
    public AbstractArcGraph(int initialCapacity) {
        nodeMap = new LinkedHashMap<>(initialCapacity);
    }

    /**
     * Iterator iterator.
     *
     * @param front the front
     * @return the iterator
     */
    public abstract Iterator<T> iterator(boolean front);

    /**
     * Iterator iterator.
     *
     * @param rootIndex the root index
     * @param front     the front
     * @return the iterator
     */
    public abstract Iterator<T> iterator(int rootIndex, boolean front);

    /**
     * Gets node map.
     *
     * @return the node map
     */
    protected Map<Integer, Node<T>> getNodeMap() {
        return nodeMap;
    }

    /**
     * Gets node.
     *
     * @param index the index
     * @return the node
     */
    protected Node<T> getNode(int index) {
        return nodeMap.get(index);
    }

    /**
     * 添加包装顶点
     *
     * @param node 顶点包装类
     * @return 顶点编号 int
     */
    protected int add(Node<T> node) {
        int nodeId = IdCreator.getNodeId();
        node.setIndex(nodeId);
        nodeMap.put(nodeId, node);
        return nodeId;
    }

    /**
     * 通过编号获取顶点
     *
     * @param index 编号
     * @return 顶点
     */
    @Override
    public T get(int index) {
        return getNode(index).getData();
    }

    @Override
    public int size() {
        return nodeMap.size();
    }
}
