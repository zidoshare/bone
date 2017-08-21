package site.zido.core.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务循环执行队列
 *
 * @author zido
 * @since 2017/30/21 下午2:30
 */
public class PostQueue {
    private List<PostTask> list = new ArrayList<>();
    private Boolean e = false;

    /**
     * 一遍一遍的执行task,直到所有的task都完成
     * @param queue 队列
     */
    public static void execute(PostQueue queue){
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
