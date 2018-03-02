package site.zido.bone.core.beans;

import java.util.LinkedList;

/**
 * 任务循环执行队列
 *
 * @author zido
 * @since 2017/30/21 下午2:30
 */
public class PostQueue {
    private LinkedList<PostTask> list = new LinkedList<>();
    private Boolean e = false;

    /**
     * 一遍一遍的执行task,直到所有的task都完成
     *
     * @param queue 队列
     */
    public static void execute(PostQueue queue) {
        queue.e = true;
        int len = queue.list.size();
        LinkedList<PostTask> tempList = new LinkedList<>();
        while (!queue.list.isEmpty()) {
            PostTask pop = queue.list.pop();
            if (pop.run()) {
                continue;
            }
            tempList.offer(pop);
        }
        if (tempList.size() == len) {
            throw new RuntimeException("依赖解析异常，包含循环依赖或有依赖未注入");
        }
        if (tempList.isEmpty()) {
            return;
        }
        queue.list = tempList;
        execute(queue);
    }

    public void addTask(PostTask task) {
        list.offer(task);
    }

    public boolean isEnd() {
        return e && list.isEmpty();
    }
}
