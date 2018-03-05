package site.zido.bone.core.utils.graph;

import java.util.Iterator;

/**
 * 有向图的十字链表实现
 *
 * @author zido
 */
public class OrthogonalArcGraph<T> extends ArcGraph<T> {

    /**
     * 添加顶点
     *
     * @param t 顶点
     * @return 顶点编号
     */
    @Override
    public int add(T t) {
        OrthogonalNode<T> node = new OrthogonalNode<>(t);
        return add(node);
    }

    @Override
    protected OrthogonalNode<T> getNode(int index) {
        return (OrthogonalNode<T>) super.getNode(index);
    }

    /**
     * 添加弧 //TODO 等待完成
     *
     * @param tail 弧尾编号
     * @param head 弧头编号
     */
    @Override
    public void connect(int tail, int head) {
        Arc arc = new OrthogonalArc(tail, head);
        addArc(arc);
    }

    /**
     * 移除弧
     *
     * @param tail 弧尾编号
     * @param head 弧头编号
     */
    @Override
    public void disConnect(int tail, int head) {

    }

    /**
     * 得到当前图的迭代器，用于对图进行遍历
     *
     * @param root 从哪个点开始遍历
     */
    @Override
    public Iterator<T> iterator(T root) {
        return null;
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<T> iterator() {
        return null;
    }

}
