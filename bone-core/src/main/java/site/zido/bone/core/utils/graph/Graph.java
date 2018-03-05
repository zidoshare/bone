package site.zido.bone.core.utils.graph;

import java.util.*;

public class Graph implements Iterable<GraphNode> {
    private GraphNode root;

    public Graph(GraphNode node) {
        if (node.getParents().size() > 0) {
            throw new NodeNotRootException(node);
        }
        root = node;
    }

    public static Graph newGraph() {
        return new Graph(new NullGraphNode());
    }

    public GraphNode addChild(GraphNode node) {
        addChild(root, node);
        return node;
    }

    public GraphNode addChild(GraphNode parent, GraphNode child) {
        parent.addNode(child);
        return child;
    }

    public List<GraphNode> getList() {
        List<GraphNode> result = new ArrayList<>();
        Queue<GraphNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            GraphNode poll = queue.poll();
            result.add(poll);
            queue.addAll(poll.getNodes());
        }
        return result;
    }

    public boolean contains(GraphNode node) {
        for (GraphNode graphNode : this) {
            if (graphNode.equals(node))
                return true;
        }
        return false;
    }

    @Override
    public Iterator<GraphNode> iterator() {
        return new Gtr(root);
    }


    private class Gtr implements Iterator<GraphNode> {
        private Queue<GraphNode> list = new LinkedList<>();
        private GraphNode currentNode;

        Gtr(GraphNode root) {
            List<GraphNode> nodes = root.getNodes();
            list.addAll(nodes);
        }

        @Override
        public boolean hasNext() {
            return !list.isEmpty();
        }

        @Override
        public GraphNode next() {
            GraphNode node = list.poll();
            currentNode = node;
            list.addAll(node.getNodes());
            return node;
        }

        @Override
        public void remove() {
            List<GraphNode> nodes = currentNode.getNodes();
            list.removeAll(nodes);
            List<GraphNode> parents = currentNode.getParents();
            for (GraphNode parent : parents) {
                parent.getNodes().remove(currentNode);
            }
        }
    }


}
