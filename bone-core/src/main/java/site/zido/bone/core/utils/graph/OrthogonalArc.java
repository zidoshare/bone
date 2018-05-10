package site.zido.bone.core.utils.graph;

/**
 * 弧的十字链表实现
 *
 * @author zido
 * @date 2018 /05/10
 */
public class OrthogonalArc extends AbstractArc {
    /**
     * The Head link.
     */
    private OrthogonalArc headLink;
    /**
     * The Tail link.
     */
    private OrthogonalArc tailLink;

    /**
     * Instantiates a new Orthogonal arc.
     *
     * @param tailVex the tail vex
     * @param headVex the head vex
     */
    public OrthogonalArc(int tailVex, int headVex) {
        super(tailVex, headVex);
    }

    /**
     * Gets head link.
     *
     * @return the head link
     */
    public OrthogonalArc getHeadLink() {
        return headLink;
    }

    /**
     * Sets head link.
     *
     * @param headLink the head link
     */
    public void setHeadLink(OrthogonalArc headLink) {
        this.headLink = headLink;
    }

    /**
     * Gets tail link.
     *
     * @return the tail link
     */
    public OrthogonalArc getTailLink() {
        return tailLink;
    }

    /**
     * Sets tail link.
     *
     * @param tailLink the tail link
     */
    public void setTailLink(OrthogonalArc tailLink) {
        this.tailLink = tailLink;
    }
}
