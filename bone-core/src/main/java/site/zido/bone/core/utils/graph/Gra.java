package site.zido.bone.core.utils.graph;

import java.util.Iterator;

/**
 * 图结构抽象
 *
 * @author zido
 */
public interface Gra<T> extends Iterable<T> {
    class IdCreator {
        private int nodeIdStart = 0;

        int getNodeId() {
            return ++nodeIdStart;
        }
    }

    /**
     * 添加顶点
     *
     * @param t 顶点
     * @return 顶点编号
     */
    int add(T t);

    /**
     * 添加边
     *
     * @param edge1 边编号
     * @param edge2 边编号
     */
    void connect(int edge1, int edge2);

    /**
     * 移除边
     *
     * @param edge1 边编号
     * @param edge2 边编号
     */
    void disConnect(int edge1, int edge2);

    /**
     * 通过编号获取顶点
     *
     * @param index 编号
     * @return 顶点
     */
    T get(int index);

    /**
     * 通过顶点获取边
     *
     * @param index1 顶点编号
     * @param index2 顶点编号
     * @return 边
     */
    Edge get(int index1, int index2);

    /**
     * 得到当前图的迭代器，用于对图进行遍历
     *
     * @param rootIndex 从哪个点开始遍历
     */
    Iterator<T> iterator(int rootIndex);

    /**
     * 获取顶点数量
     * @return 顶点数量
     */
    int size();
}
