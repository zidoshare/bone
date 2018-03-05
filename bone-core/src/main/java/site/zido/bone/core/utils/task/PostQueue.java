package site.zido.bone.core.utils.task;

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
    public static boolean execute(PostQueue queue) {
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

        if (tempList.isEmpty()) {
            return true;
        }

        if (tempList.size() == len) {
            return false;
        }
        queue.list = tempList;
        return execute(queue);
    }

    public void addTask(PostTask task) {
        list.offer(task);
    }

    public boolean isEnd() {
        return e && list.isEmpty();
    }

    public LinkedList<PostTask> getList() {
        return list;
    }

    public void setList(LinkedList<PostTask> list) {
        this.list = list;
    }
}
