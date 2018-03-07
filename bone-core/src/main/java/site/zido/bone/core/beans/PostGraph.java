package site.zido.bone.core.beans;

import site.zido.bone.core.beans.structure.DefProperty;
import site.zido.bone.core.beans.structure.Definition;
import site.zido.bone.core.beans.structure.DelayMethod;
import site.zido.bone.core.exception.beans.CircleRelyException;
import site.zido.bone.core.utils.LexUtils;
import site.zido.bone.core.utils.graph.*;

import java.util.*;

/**
 * 任务循环执行图结构
 *
 * @author zido
 * @since 2017/30/21 下午2:30
 */
public class PostGraph extends OrthogonalArcGraph<PostTask> {

    PostGraph() {

    }

    public PostNeed addChild(PostTask child) {
        return new PostNeed(child, add(child));
    }

    public void execute() {
        Stack<Integer> stack = new Stack<>();
        Map<Integer, Node<PostTask>> nodeMap = getNodeMap();
        for (Integer id : nodeMap.keySet()) {
            OrthogonalNode<PostTask> node = getNode(id);
            if (node.getFirstIn() == null) {
                stack.push(id);
            }
        }
        Set<Integer> visits = new HashSet<>();
        while (!stack.isEmpty()) {
            Integer index = stack.peek();
            OrthogonalNode<PostTask> currentNode = getNode(index);
            OrthogonalArc in = currentNode.getFirstIn();
            boolean isFill = true;
            //加入所有的头结点
            while (in != null) {
                int tailVex = in.getTailVex();
                in = in.getHeadLink();
                if (visits.contains(tailVex)) {
                    continue;
                }
                isFill = false;
                stack.push(tailVex);
            }
            if (!isFill) {
                continue;
            }
            System.out.println(stack);
            //满足执行条件出栈，前面已经获取，这里不需要再次获取
            stack.pop();
            if (!visits.contains(index))
                currentNode.getData().run();
            visits.add(index);
            //获取指向的所有顶点
            OrthogonalArc arc = currentNode.getFirstOut();
            while (arc != null) {
                int headVex = arc.getHeadVex();
                arc = arc.getTailLink();
                if (visits.contains(headVex)) {
                    continue;
                }
                stack.push(headVex);
            }
        }
    }

    private void checkCircle(int index) {
        DfsCircle dfsCircle = new DfsCircle(index);
        if (dfsCircle.hasCycle()) {
            throw new CircleRelyException(dfsCircle.getCircle());
        }
    }

    class DfsCircle {
        private boolean[] marked;
        private int[] edgeTo;
        private Stack<Definition> cycle;
        private boolean[] onStack;

        private DfsCircle(int index) {
            int size = size() + 1;
            onStack = new boolean[size];
            edgeTo = new int[size];
            marked = new boolean[size];
            dfs(index);
        }

        private void dfs(int v) {
            onStack[v] = true;
            marked[v] = true;
            OrthogonalNode<PostTask> node = getNode(v);
            for (Edge edge : node) {
                int w = edge.getEnd();
                if (hasCycle()) return;
                if (!marked[w]) {
                    edgeTo[w] = v;
                    dfs(w);
                } else if (onStack[w]) {
                    cycle = new Stack<>();
                    for (int x = v; x != w; x = edgeTo[x])
                        cycle.push(getNode(x).getData().getDefinition());
                    cycle.push(getNode(w).getData().getDefinition());
                    cycle.push(getNode(v).getData().getDefinition());
                }
            }
            onStack[v] = false;
        }

        private boolean hasCycle() {
            return cycle != null;
        }

        Iterable<Definition> getCircle() {
            return cycle;
        }

    }

    class PostNeed {
        private PostTask task;
        private int currentIndex;

        private PostNeed(PostTask task, int currentIndex) {
            this.task = task;
            this.currentIndex = currentIndex;
        }

        public PostProduce need(DefProperty[] properties) {
            if (properties == null)
                properties = new DefProperty[0];
            for (DefProperty property : properties) {
                Map<Integer, Node<PostTask>> nodeMap = getNodeMap();
                Set<Integer> ids = nodeMap.keySet();
                for (Integer id : ids) {
                    Definition definition = nodeMap.get(id).getData().getDefinition();
                    if (LexUtils.DefEquals(property, definition)) {
                        connect(id, currentIndex);
                        System.out.println(String.format("连接：[%d]->[%d]", id, currentIndex));
                        //当此顶点再含有一根出线时，检测是否存在环
                        if (getNode(currentIndex).getFirstOut() != null) {
                            checkCircle(currentIndex);
                        }
                    }
                }
            }
            task.need(properties);
            return new PostProduce(task, currentIndex);
        }
    }

    class PostProduce {
        private PostTask task;
        private int currentIndex;

        private PostProduce(PostTask task, int currentIndex) {
            this.task = task;
            this.currentIndex = currentIndex;
        }

        public void produce(Definition definition) {
            Map<Integer, Node<PostTask>> nodeMap = getNodeMap();
            Set<Integer> ids = nodeMap.keySet();
            for (Integer id : ids) {
                Node<PostTask> node = nodeMap.get(id);
                DefProperty[] properties = node.getData().getProperties();
                for (DefProperty property : properties) {
                    if (LexUtils.DefEquals(property, definition)) {
                        connect(currentIndex, id);
                        System.out.println(String.format("连接：[%d]->[%d]", currentIndex, id));
                        //当此顶点再含有一根入线时，检测是否存在环
                        if (getNode(currentIndex).getFirstIn() != null) {
                            checkCircle(currentIndex);
                        }
                    }
                }
            }
            task.produce(definition);
            System.out.println(String.format("入图：[%d]-[%s]", currentIndex, task));
        }

        public void produce(DelayMethod delayMethod) {
            task.produce(delayMethod);
            System.out.println(String.format("入图：[%d]-[%s]", currentIndex, task));
        }
    }
}
