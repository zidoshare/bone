package site.zido.bone.core.utils.graph;

public class StrictGraph extends Graph {
    public StrictGraph(GraphNode node) {
        super(node);
    }

    @Override
    public GraphNode addChild(GraphNode parent, GraphNode child) throws NodeExistsException {
        if (contains(child)) {
            throw new NodeExistsException(child);
        }
        return super.addChild(parent, child);
    }
}
