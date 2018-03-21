package site.zido.bone.core.utils.graph;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 顶点的十字链表实现
 *
 * @author zido
 */
public class OrthogonalNode<T> extends ArcNode<T> {
    private OrthogonalArc firstIn;
    private OrthogonalArc firstOut;

    class ONtr implements Iterator<Edge> {
        private OrthogonalArc current;
        private boolean front = true;

        ONtr(OrthogonalArc current) {
            this(current, true);
        }

        ONtr(OrthogonalArc current, boolean front) {
            this.current = current;
            this.front = front;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Edge next() {
            OrthogonalArc result = current;
            if (result == null) {
                throw new NoSuchElementException();
            }
            current = front ? current.getTailLink() : current.getHeadLink();
            return result;
        }
    }

    public OrthogonalNode(T data) {
        super(data);
    }

    public OrthogonalArc getFirstIn() {
        return firstIn;
    }

    public void setFirstIn(OrthogonalArc firstIn) {
        this.firstIn = firstIn;
    }

    public OrthogonalArc getFirstOut() {
        return firstOut;
    }

    public void setFirstOut(OrthogonalArc firstOut) {
        this.firstOut = firstOut;
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Edge> iterator() {
        return new ONtr(firstOut);
    }

    public Iterator<Edge> iterator(boolean front) {
        return new ONtr(firstOut, front);
    }
}
