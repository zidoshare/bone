package site.zido.bone.core.utils.graph;

/**
 * 没有承载任何信息的node节点(常用来做根节点)
 *
 * @author zido
 * @date 2018 /05/10
 */
public class NullGraphNode extends AbstractGraphNode {

    /**
     * The Root.
     */
    private boolean root;

    /**
     * Instantiates a new Null graph node.
     */
    public NullGraphNode() {
        this(true);
    }

    /**
     * Instantiates a new Null graph node.
     *
     * @param isRoot the is root
     */
    public NullGraphNode(boolean isRoot) {
        this.root = isRoot;
    }

    @Override
    public String toString() {
        return root ? "根节点" : "空节点";
    }
}
