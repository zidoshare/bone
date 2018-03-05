package site.zido.bone.core.utils.graph;

import java.util.ArrayList;
import java.util.List;

public abstract class GraphNode {
    private List<GraphNode> nodes = new ArrayList<>(3);
    private List<GraphNode> parents = new ArrayList<>(3);

    public GraphNode() {

    }

    public void addNode(GraphNode node) {
        nodes.add(node);
        node.getParents().add(this);
    }

    public List<GraphNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<GraphNode> nodes) {
        this.nodes = nodes;
    }

    public List<GraphNode> getParents() {
        return parents;
    }

    public void setParents(List<GraphNode> parents) {
        this.parents = parents;
    }
}
