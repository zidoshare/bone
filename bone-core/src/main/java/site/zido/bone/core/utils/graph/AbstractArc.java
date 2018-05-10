package site.zido.bone.core.utils.graph;

/**
 * 有向图的弧（边）
 *
 * @author zido
 * @date 2018 /05/10
 */
public abstract class AbstractArc extends Edge {

    /**
     * Instantiates a new AbstractArc.
     *
     * @param tailVex the tail vex
     * @param headVex the head vex
     */
    public AbstractArc(int tailVex, int headVex) {
        setStart(tailVex);
        setEnd(headVex);
    }

    /**
     * Gets tail vex.
     *
     * @return the tail vex
     */
    public int getTailVex() {
        return getStart();
    }

    /**
     * Sets tail vex.
     *
     * @param tailVex the tail vex
     */
    public void setTailVex(int tailVex) {
        setStart(tailVex);
    }

    /**
     * Gets head vex.
     *
     * @return the head vex
     */
    public int getHeadVex() {
        return getEnd();
    }

    /**
     * Sets head vex.
     *
     * @param headVex the head vex
     */
    public void setHeadVex(int headVex) {
        setEnd(headVex);
    }
}
