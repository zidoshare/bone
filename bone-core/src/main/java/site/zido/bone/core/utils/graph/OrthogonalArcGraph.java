package site.zido.bone.core.utils.graph;

import java.util.*;

/**
 * 有向图的十字链表实现(正向逆向都具备)
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
     * 添加弧
     *
     * @param tail 弧尾编号
     * @param head 弧头编号
     */
    @Override
    public void connect(int tail, int head) {
        OrthogonalNode<T> tailNode = getNode(tail);
        if (tailNode == null) {
            throw new NoSuchNodeException(tail);
        }
        OrthogonalNode<T> headNode = getNode(head);
        if (headNode == null) {
            throw new NoSuchNodeException(head);
        }

        if (get(tail, head) != null) {
            return;
        }

        OrthogonalArc arc = new OrthogonalArc(tail, head);

        //处理弧头顶点
        //替代节点，完成替代
        arc.setTailLink(tailNode.getFirstOut());
        tailNode.setFirstOut(arc);

        //处理弧尾顶点
        arc.setHeadLink(headNode.getFirstIn());
        headNode.setFirstIn(arc);
    }

    /**
     * 移除弧
     *
     * @param tail 弧尾编号
     * @param head 弧头编号
     */
    @Override
    public void disConnect(int tail, int head) {
        OrthogonalNode<T> tailNode = getNode(tail);
        if (tailNode == null) {
            throw new NoSuchNodeException(tail);
        }
        OrthogonalNode<T> headNode = getNode(head);
        if (headNode == null) {
            throw new NoSuchNodeException(head);
        }
        throw new UnsupportedOperationException("disConnect");
    }

    /**
     * 通过弧尾和弧头获取弧
     *
     * @param tail 弧尾编号
     * @param head 弧头编号
     * @return 弧
     */
    @Override
    public OrthogonalArc get(int tail, int head) {
        OrthogonalNode<T> node = getNode(tail);
        OrthogonalArc out = node.getFirstOut();
        while (out != null) {
            if (out.getEnd() == head) {
                return out;
            }
            out = out.getTailLink();
        }
        return null;
    }

    class OAGtr implements Iterator<T> {
        private OrthogonalNode<T> root;
        private boolean front;
        private Queue<OrthogonalNode<T>> queue = new LinkedList<>();
        private Set<OrthogonalNode<T>> visits = new LinkedHashSet<>();

        public OAGtr(OrthogonalNode<T> root) {
            this(root, true);
        }

        public OAGtr(OrthogonalNode<T> root, boolean front) {
            this.root = root;
            this.front = front;
            queue.offer(root);
        }

        @Override
        public boolean hasNext() {
            return queue.isEmpty();
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            OrthogonalNode<T> result = queue.poll();
            visits.add(result);

            OrthogonalArc arc = front ? result.getFirstOut() : result.getFirstIn();
            while (arc != null) {
                OrthogonalNode<T> node = OrthogonalArcGraph.this.getNode(front ? arc.getHeadVex() : arc.getTailVex());
                if (visits.contains(node)) {
                    continue;
                }
                queue.offer(node);
                arc = front ? arc.getTailLink() : arc.getHeadLink();
            }
            return result.getData();
        }
    }

    /**
     * 得到当前图的迭代器，用于对图进行遍历
     *
     * @param rootIndex 从哪个点开始遍历
     */
    @Override
    public Iterator<T> iterator(int rootIndex) {
        return iterator(rootIndex, true);
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<T> iterator() {
        return iterator(0, true);
    }

    @Override
    public Iterator<T> iterator(boolean front) {
        return iterator(0, front);
    }

    @Override
    public Iterator<T> iterator(int rootIndex, boolean front) {
        return new OAGtr(getNode(rootIndex), front);
    }
}
