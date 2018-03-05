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
    /**
     * 弧集合 //TODO 二维数组实现
     */
    private Map<String, Arc> arcMap;

    public ArcGraph() {
        nodeMap = new LinkedHashMap<>();
        arcMap = new LinkedHashMap<>();
    }

    public ArcGraph(int initialCapacity) {
        nodeMap = new LinkedHashMap<>(initialCapacity);
        arcMap = new LinkedHashMap<>(initialCapacity);
    }

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
        int nodeId = IdCreator.getNodeId();
        node.setIndex(nodeId);
        nodeMap.put(nodeId, node);
        return nodeMap.size() - 1;
    }

    protected void addArc(Arc arc) {
        arcMap.put(createKey(arc), arc);
    }

    private String createKey(Arc arc) {
        return arc.getTailVex() + "-" + arc.getHeadVex();
    }

    private String createKey(int tail, int head) {
        return tail + "-" + head;
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

    /**
     * 通过弧尾和弧头获取弧
     *
     * @param tail 弧尾编号
     * @param head 弧头编号
     * @return 弧
     */
    @Override
    public Arc get(int tail, int head) {
        return arcMap.get(createKey(tail, head));
    }

    /**
     * 获取第一个指向该顶点的弧
     *
     * @param nodeId 顶点编号
     * @return 弧
     */
    protected Arc getOneIn(int nodeId) {
        for (Arc arc : arcMap.values()) {
            if (arc.getHeadVex() == nodeId)
                return arc;
        }
        return null;
    }

    /**
     * 获取第一个由该顶点发出的弧
     *
     * @param nodeId 顶点编号
     * @return 弧
     */
    protected Arc getOneOut(int nodeId) {
        for (Arc arc : arcMap.values()) {
            if (arc.getTailVex() == nodeId)
                return arc;
        }
        return null;
    }
}
