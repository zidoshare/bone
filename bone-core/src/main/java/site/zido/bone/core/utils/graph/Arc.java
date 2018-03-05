package site.zido.bone.core.utils.graph;

/**
 * 有向图的弧（边）
 *
 * @author zido
 */
public abstract class Arc extends Edge {

    public Arc(int tailVex, int headVex) {
        setStart(tailVex);
        setEnd(headVex);
    }

    public int getTailVex() {
        return getStart();
    }

    public void setTailVex(int tailVex) {
        setStart(tailVex);
    }

    public int getHeadVex() {
        return getEnd();
    }

    public void setHeadVex(int headVex) {
        setEnd(headVex);
    }
}
