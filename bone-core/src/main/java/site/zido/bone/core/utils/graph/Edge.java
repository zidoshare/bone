package site.zido.bone.core.utils.graph;

/**
 * 边
 *
 * @author zido
 * @date 2018 /05/10
 */
public class Edge {
    /**
     * The Start.
     */
    private int start;
    /**
     * The End.
     */
    private int end;
    /**
     * The Weight.
     */
    private int weight;

    /**
     * Gets start.
     *
     * @return the start
     */
    public int getStart() {
        return start;
    }

    /**
     * Sets start.
     *
     * @param start the start
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * Gets end.
     *
     * @return the end
     */
    public int getEnd() {
        return end;
    }

    /**
     * Sets end.
     *
     * @param end the end
     */
    public void setEnd(int end) {
        this.end = end;
    }

    /**
     * Gets weight.
     *
     * @return the weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Sets weight.
     *
     * @param weight the weight
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }
}
