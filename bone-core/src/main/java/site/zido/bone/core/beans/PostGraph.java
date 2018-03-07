package site.zido.bone.core.beans;

import site.zido.bone.core.beans.structure.DefProperty;
import site.zido.bone.core.beans.structure.Definition;
import site.zido.bone.core.beans.structure.DelayMethod;
import site.zido.bone.core.exception.beans.CircleRelyException;
import site.zido.bone.core.exception.beans.LackRelyException;
import site.zido.bone.core.utils.LexUtils;
import site.zido.bone.core.utils.graph.*;
import site.zido.bone.logger.Logger;
import site.zido.bone.logger.impl.LogManager;

import java.util.*;

/**
 * 任务循环执行图结构
 *
 * @author zido
 * @since 2017/30/21 下午2:30
 */
public class PostGraph extends OrthogonalArcGraph<PostTask> {
    private Logger logger = LogManager.getLogger("依赖解析");

    public PostGraph() {

    }

    public PostNeed addTask(PostTask child) {
        return new PostNeed(child, add(child));
    }

    public void execute() {
        checkLack();
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

    public void checkCircle(int index) {
        DfsCircle dfsCircle = new DfsCircle(index);
        if (dfsCircle.hasCycle()) {
            Stack<PostTask> circle = dfsCircle.getCircle();
            StringBuilder sb = new StringBuilder("产生循环依赖\n");
            sb.append(" ...\n").append(" ↓ ").append("\n");
            for (PostTask task : circle) {
                sb.append(task.toString()).append("\n");
                sb.append(" ↓ ").append("\n");
            }
            sb.append(" ...\n");
            logger.error(sb.toString());
            throw new CircleRelyException();
        }
    }

    public void checkLack() {
        Map<Integer, Node<PostTask>> nodeMap = getNodeMap();
        Collection<Node<PostTask>> values = nodeMap.values();
        for (Node<PostTask> value : values) {
            PostTask data = value.getData();
            DefProperty[] properties = data.getProperties();
            List<OrthogonalNode<PostTask>> parents = new ArrayList<>();
            OrthogonalNode node = (OrthogonalNode) value;
            OrthogonalArc in = node.getFirstIn();
            while (in != null) {
                parents.add(getNode(in.getTailVex()));
                in = in.getHeadLink();
            }
            if (parents.size() < properties.length) {
                List<DefProperty> result = new ArrayList<>();
                for (DefProperty property : properties) {
                    if (property.getValue() != null) {
                        continue;
                    }
                    boolean has = false;
                    for (OrthogonalNode<PostTask> parent : parents) {
                        if (LexUtils.DefEquals(property, parent.getData().getDefinition())) {
                            has = true;
                            parents.remove(parent);
                            break;
                        }
                    }
                    if (!has) {
                        result.add(property);
                    }
                }
                if (result.size() > 0)
                    throw new LackRelyException(data, result);
            }

        }
    }

    class DfsCircle {
        private boolean[] marked;
        private int[] edgeTo;
        private Stack<PostTask> cycle;
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
                if (hasCycle())
                    return;
                if (!marked[w]) {
                    edgeTo[w] = v;
                    dfs(w);
                } else if (onStack[w]) {
                    cycle = new Stack<>();
                    for (int x = v; x != w; x = edgeTo[x])
                        cycle.push(getNode(x).getData());
                    cycle.push(getNode(w).getData());
                    cycle.push(getNode(v).getData());
                }
            }
            onStack[v] = false;
        }

        private boolean hasCycle() {
            return cycle != null;
        }

        Stack<PostTask> getCircle() {
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

        public PostNeed need(DefProperty[] properties) {
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
            return this;
        }

        public PostNeed produce(Definition definition) {
            task.produce(definition);
            System.out.println(String.format("入图：[%d]-[%s]", currentIndex, task));
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
            return this;
        }

        public PostNeed produce(DelayMethod delayMethod) {
            task.produce(delayMethod);
            System.out.println(String.format("入图：[%d]-[%s]", currentIndex, task));
            return this;
        }

        public PostNeed produce(DefProperty property) {
            task.produce(property);
            System.out.println(String.format("入图：[%d]-[%s]", currentIndex, task));
            return this;
        }

        public PostNeed addChild(PostTask task) {
            int child = add(task);
            connect(currentIndex, child);
            if (getNode(currentIndex).getFirstIn() != null) {
                checkCircle(currentIndex);
            }
            System.out.println(String.format("连接：[%d]->[%d]", currentIndex, child));
            return new PostNeed(task, child);
        }
    }
}
