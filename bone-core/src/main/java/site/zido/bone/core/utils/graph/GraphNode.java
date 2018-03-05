package site.zido.bone.core.utils.graph;

import java.util.ArrayList;
import java.util.List;

public class GraphNode {
    private List<GraphNode> nodes = new ArrayList<>(3);
    private GraphNode parent = null;

    public void addNode(GraphNode node) {
        nodes.add(node);
    }

    public List<GraphNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<GraphNode> nodes) {
        this.nodes = nodes;
    }

    public GraphNode getParent() {
        return parent;
    }

    public void setParent(GraphNode parent) {
        this.parent = parent;
    }
}
