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
 * @date 2018 /05/10
 * @since 2017 /30/21 下午2:30
 */
public class PostGraph extends OrthogonalArcGraph<AbstractPostTask> {
    /**
     * The Logger.
     */
    private Logger logger = LogManager.getLogger("依赖解析");

    /**
     * Instantiates a new Post graph.
     */
    public PostGraph() {

    }

    /**
     * Add task post need.
     *
     * @param child the child
     * @return the post need
     */
    public PostNeed addTask(AbstractPostTask child) {
        return new PostNeed(child, add(child));
    }

    /**
     * Execute.
     */
    public void execute() {
        checkLack();
        Stack<Integer> stack = new Stack<>();
        Map<Integer, Node<AbstractPostTask>> nodeMap = getNodeMap();
        for (Integer id : nodeMap.keySet()) {
            OrthogonalNode<AbstractPostTask> node = getNode(id);
            if (node.getFirstIn() == null) {
                stack.push(id);
            }
        }
        Set<Integer> visits = new HashSet<>();
        while (!stack.isEmpty()) {
            Integer index = stack.peek();
            OrthogonalNode<AbstractPostTask> currentNode = getNode(index);
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
            if (!visits.contains(index)) {
                currentNode.getData().run();
            }
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

    /**
     * Check circle.
     *
     * @param index the index
     */
    public void checkCircle(int index) {
        DfsCircle dfsCircle = new DfsCircle(index);
        if (dfsCircle.hasCycle()) {
            Stack<AbstractPostTask> circle = dfsCircle.getCircle();
            StringBuilder sb = new StringBuilder("产生循环依赖\n");
            sb.append(" ...\n").append(" ↓ ").append("\n");
            for (AbstractPostTask task : circle) {
                sb.append(task.toString()).append("\n");
                sb.append(" ↓ ").append("\n");
            }
            sb.append(" ...\n");
            logger.error(sb.toString());
            throw new CircleRelyException();
        }
    }

    /**
     * Check lack.
     */
    public void checkLack() {
        Map<Integer, Node<AbstractPostTask>> nodeMap = getNodeMap();
        Collection<Node<AbstractPostTask>> values = nodeMap.values();
        for (Node<AbstractPostTask> value : values) {
            AbstractPostTask data = value.getData();
            DefProperty[] properties = data.getProperties();
            List<OrthogonalNode<AbstractPostTask>> parents = new ArrayList<>();
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
                    for (OrthogonalNode<AbstractPostTask> parent : parents) {
                        if (LexUtils.defEquals(property, parent.getData().getDefinition())) {
                            has = true;
                            parents.remove(parent);
                            break;
                        }
                    }
                    if (!has) {
                        result.add(property);
                    }
                }
                if (result.size() > 0) {
                    throw new LackRelyException(data, result);
                }
            }

        }
    }

    /**
     * The type Dfs circle.
     *
     * @author zido
     * @date 2018 /05/10
     */
    class DfsCircle {
        /**
         * The Marked.
         */
        private boolean[] marked;
        /**
         * The Edge to.
         */
        private int[] edgeTo;
        /**
         * The Cycle.
         */
        private Stack<AbstractPostTask> cycle;
        /**
         * The On stack.
         */
        private boolean[] onStack;

        /**
         * Instantiates a new Dfs circle.
         *
         * @param index the index
         */
        private DfsCircle(int index) {
            int size = size() + 1;
            onStack = new boolean[size];
            edgeTo = new int[size];
            marked = new boolean[size];
            dfs(index);
        }

        /**
         * Dfs.
         *
         * @param v the v
         */
        private void dfs(int v) {
            onStack[v] = true;
            marked[v] = true;
            OrthogonalNode<AbstractPostTask> node = getNode(v);
            for (Edge edge : node) {
                int w = edge.getEnd();
                if (hasCycle()) {
                    return;
                }
                if (!marked[w]) {
                    edgeTo[w] = v;
                    dfs(w);
                } else if (onStack[w]) {
                    cycle = new Stack<>();
                    for (int x = v; x != w; x = edgeTo[x]) {
                        cycle.push(getNode(x).getData());
                    }
                    cycle.push(getNode(w).getData());
                    cycle.push(getNode(v).getData());
                }
            }
            onStack[v] = false;
        }

        /**
         * Has cycle boolean.
         *
         * @return the boolean
         */
        private boolean hasCycle() {
            return cycle != null;
        }

        /**
         * Gets circle.
         *
         * @return the circle
         */
        Stack<AbstractPostTask> getCircle() {
            return cycle;
        }

    }

    /**
     * The type Post need.
     *
     * @author zido
     * @date 2018 /05/10
     */
    class PostNeed {
        /**
         * The Task.
         */
        private AbstractPostTask task;
        /**
         * The Current index.
         */
        private int currentIndex;

        /**
         * Instantiates a new Post need.
         *
         * @param task         the task
         * @param currentIndex the current index
         */
        private PostNeed(AbstractPostTask task, int currentIndex) {
            this.task = task;
            this.currentIndex = currentIndex;
        }

        /**
         * Need post need.
         *
         * @param properties the properties
         * @return the post need
         */
        public PostNeed need(DefProperty[] properties) {
            if (properties == null) {
                properties = new DefProperty[0];
            }
            for (DefProperty property : properties) {
                Map<Integer, Node<AbstractPostTask>> nodeMap = getNodeMap();
                Set<Integer> ids = nodeMap.keySet();
                for (Integer id : ids) {
                    Definition definition = nodeMap.get(id).getData().getDefinition();
                    if (LexUtils.defEquals(property, definition)) {
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

        /**
         * Produce post need.
         *
         * @param definition the definition
         * @return the post need
         */
        public PostNeed produce(Definition definition) {
            task.produce(definition);
            System.out.println(String.format("入图：[%d]-[%s]", currentIndex, task));
            Map<Integer, Node<AbstractPostTask>> nodeMap = getNodeMap();
            Set<Integer> ids = nodeMap.keySet();
            for (Integer id : ids) {
                Node<AbstractPostTask> node = nodeMap.get(id);
                DefProperty[] properties = node.getData().getProperties();
                for (DefProperty property : properties) {
                    if (LexUtils.defEquals(property, definition)) {
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

        /**
         * Produce post need.
         *
         * @param delayMethod the delay method
         * @return the post need
         */
        public PostNeed produce(DelayMethod delayMethod) {
            task.produce(delayMethod);
            System.out.println(String.format("入图：[%d]-[%s]", currentIndex, task));
            return this;
        }

        /**
         * Produce post need.
         *
         * @param property the property
         * @return the post need
         */
        public PostNeed produce(DefProperty property) {
            task.produce(property);
            System.out.println(String.format("入图：[%d]-[%s]", currentIndex, task));
            return this;
        }

        /**
         * Add child post need.
         *
         * @param task the task
         * @return the post need
         */
        public PostNeed addChild(AbstractPostTask task) {
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
