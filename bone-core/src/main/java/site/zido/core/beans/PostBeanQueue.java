package site.zido.core.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * 后置Bean实例化任务调度队列
 */
public class PostBeanQueue {
    private List<PostTask> list = new ArrayList<>();
    private Boolean e = false;

    /**
     * 一遍一遍的执行task,直到所有的task都完成
     * @param queue 队列
     */
    public static void execute(PostBeanQueue queue){
        if(queue.list.isEmpty())
            return ;
        queue.e = true;
        queue.list.removeIf(PostTask::run);
        execute(queue);
    }

    public void addTask(PostTask task){
        list.add(task);
    }

    public boolean isEnd(){
        return e && list.isEmpty();
    }
}
