package site.zido.bone.core.utils.graph;

/**
 * 没有承载任何信息的node节点(常用来做根节点)
 *
 * @author zido
 */
public class NullGraphNode extends GraphNode {

    private boolean root;

    public NullGraphNode() {
        this(true);
    }

    public NullGraphNode(boolean isRoot) {
        this.root = isRoot;
    }

    @Override
    public String toString() {
        return root ? "根节点" : "空节点";
    }
}
