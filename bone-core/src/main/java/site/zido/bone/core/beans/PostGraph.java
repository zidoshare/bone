package site.zido.bone.core.beans;

import site.zido.bone.core.utils.graph.GraphNode;
import site.zido.bone.core.utils.graph.NullGraphNode;
import site.zido.bone.core.utils.graph.StrictGraph;

import java.util.LinkedList;

/**
 * 任务循环执行队列
 *
 * @author zido
 * @since 2017/30/21 下午2:30
 */
public class PostGraph extends StrictGraph {

    private LinkedList<PostTask> list = new LinkedList<>();

    public PostGraph(GraphNode node) {
        super(node);
    }

    public static PostGraph newGraph() {
        return new PostGraph(new NullGraphNode());
    }

    @Override
    public GraphNode addChild(GraphNode child) {
        for (GraphNode node : this) {
            if (node instanceof BeanExecuteTask) {
                //TODO 添加到图中
            }
        }
        return child;
    }

    /**
     * 一遍一遍的执行task,直到所有的task都完成
     *
     * @param queue 队列
     */
    public static boolean execute(PostGraph queue) {
        int len = queue.list.size();
        LinkedList<PostTask> tempList = new LinkedList<>();
        while (!queue.list.isEmpty()) {
            PostTask pop = queue.list.pop();
            if (pop.run()) {
                continue;
            }
            tempList.offer(pop);
        }

        if (tempList.isEmpty()) {
            return true;
        }

        if (tempList.size() == len) {
            return false;
        }
        queue.list = tempList;
        return execute(queue);
    }
}
