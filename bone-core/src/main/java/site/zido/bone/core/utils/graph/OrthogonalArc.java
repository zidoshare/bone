package site.zido.bone.core.utils.graph;

/**
 * 弧的十字链表实现
 *
 * @author zido
 */
public class OrthogonalArc extends Arc {
    private int headLink;
    private int tailLink;

    public OrthogonalArc(int tailVex, int headVex) {
        super(tailVex, headVex);
    }

    public int getHeadLink() {
        return headLink;
    }

    public void setHeadLink(int headLink) {
        this.headLink = headLink;
    }

    public int getTailLink() {
        return tailLink;
    }

    public void setTailLink(int tailLink) {
        this.tailLink = tailLink;
    }
}
