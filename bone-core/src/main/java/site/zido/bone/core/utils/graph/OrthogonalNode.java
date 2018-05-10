package site.zido.bone.core.utils.graph;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * 顶点的十字链表实现
 *
 * @param <T> the type parameter
 * @author zido
 * @date 2018 /05/10
 */
public class OrthogonalNode<T> extends AbstractArcNode<T> {
    /**
     * The First in.
     */
    private OrthogonalArc firstIn;
    /**
     * The First out.
     */
    private OrthogonalArc firstOut;

    /**
     * The type O ntr.
     *
     * @author zido
     * @date 2018 /05/10
     */
    class Otr implements Iterator<Edge> {
        /**
         * The Current.
         */
        private OrthogonalArc current;
        /**
         * The Front.
         */
        private boolean front = true;

        /**
         * Instantiates a new O ntr.
         *
         * @param current the current
         */
        Otr(OrthogonalArc current) {
            this(current, true);
        }

        /**
         * Instantiates a new O ntr.
         *
         * @param current the current
         * @param front   the front
         */
        Otr(OrthogonalArc current, boolean front) {
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

    /**
     * Instantiates a new Orthogonal node.
     *
     * @param data the data
     */
    public OrthogonalNode(T data) {
        super(data);
    }

    /**
     * Gets first in.
     *
     * @return the first in
     */
    public OrthogonalArc getFirstIn() {
        return firstIn;
    }

    /**
     * Sets first in.
     *
     * @param firstIn the first in
     */
    public void setFirstIn(OrthogonalArc firstIn) {
        this.firstIn = firstIn;
    }

    /**
     * Gets first out.
     *
     * @return the first out
     */
    public OrthogonalArc getFirstOut() {
        return firstOut;
    }

    /**
     * Sets first out.
     *
     * @param firstOut the first out
     */
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
        return new Otr(firstOut);
    }

    /**
     * Iterator iterator.
     *
     * @param front the front
     * @return the iterator
     */
    public Iterator<Edge> iterator(boolean front) {
        return new Otr(firstOut, front);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrthogonalNode<?> that = (OrthogonalNode<?>) o;
        return Objects.equals(firstIn, that.firstIn) &&
                Objects.equals(firstOut, that.firstOut);
    }

    @Override
    public int hashCode() {

        return Objects.hash(firstIn, firstOut);
    }
}
