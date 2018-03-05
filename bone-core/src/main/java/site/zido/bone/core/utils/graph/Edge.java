package site.zido.bone.core.utils.graph;

/**
 * 边
 *
 * @author zido
 */
public class Edge {
    private int start;
    private int end;
    private int weight;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
