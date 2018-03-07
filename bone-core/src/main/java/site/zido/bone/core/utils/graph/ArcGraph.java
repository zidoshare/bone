package site.zido.bone.core.utils.graph;

import java.util.*;

/**
 * 有向图接口
 *
 * @author zido
 */
public abstract class ArcGraph<T> implements Gra<T> {

    /**
     * 顶点集合
     */
    private Map<Integer, Node<T>> nodeMap;
    private IdCreator idCreator = new IdCreator();

    public ArcGraph() {
        nodeMap = new LinkedHashMap<>();
    }

    public ArcGraph(int initialCapacity) {
        nodeMap = new LinkedHashMap<>(initialCapacity);
    }

    public abstract Iterator<T> iterator(boolean front);

    public abstract Iterator<T> iterator(int rootIndex, boolean front);

    protected Map<Integer, Node<T>> getNodeMap() {
        return nodeMap;
    }

    protected Node<T> getNode(int index) {
        return nodeMap.get(index);
    }

    /**
     * 添加包装顶点
     *
     * @param node 顶点包装类
     * @return 顶点编号
     */
    protected int add(Node<T> node) {
        int nodeId = idCreator.getNodeId();
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
