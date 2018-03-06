package site.zido.bone.core.utils.graph;

/**
 * 弧的十字链表实现
 *
 * @author zido
 */
public class OrthogonalArc extends Arc {
    private OrthogonalArc headLink;
    private OrthogonalArc tailLink;

    public OrthogonalArc(int tailVex, int headVex) {
        super(tailVex, headVex);
    }

    public OrthogonalArc getHeadLink() {
        return headLink;
    }

    public void setHeadLink(OrthogonalArc headLink) {
        this.headLink = headLink;
    }

    public OrthogonalArc getTailLink() {
        return tailLink;
    }

    public void setTailLink(OrthogonalArc tailLink) {
        this.tailLink = tailLink;
    }
}
