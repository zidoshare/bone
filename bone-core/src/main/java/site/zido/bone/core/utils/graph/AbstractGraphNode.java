package site.zido.bone.core.utils.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Graph node.
 *
 * @author zido
 * @date 2018 /05/10
 */
public abstract class AbstractGraphNode {
    /**
     * The Nodes.
     */
    private List<AbstractGraphNode> nodes = new ArrayList<>(3);
    /**
     * The Parents.
     */
    private List<AbstractGraphNode> parents = new ArrayList<>(3);

    /**
     * Instantiates a new Graph node.
     */
    public AbstractGraphNode() {

    }

    /**
     * Add node.
     *
     * @param node the node
     */
    public void addNode(AbstractGraphNode node) {
        nodes.add(node);
        node.getParents().add(this);
    }

    /**
     * Gets nodes.
     *
     * @return the nodes
     */
    public List<AbstractGraphNode> getNodes() {
        return nodes;
    }

    /**
     * Sets nodes.
     *
     * @param nodes the nodes
     */
    public void setNodes(List<AbstractGraphNode> nodes) {
        this.nodes = nodes;
    }

    /**
     * Gets parents.
     *
     * @return the parents
     */
    public List<AbstractGraphNode> getParents() {
        return parents;
    }

    /**
     * Sets parents.
     *
     * @param parents the parents
     */
    public void setParents(List<AbstractGraphNode> parents) {
        this.parents = parents;
    }
}
