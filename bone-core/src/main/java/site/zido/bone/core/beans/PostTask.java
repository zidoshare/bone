package site.zido.bone.core.beans;

import site.zido.bone.core.utils.graph.GraphNode;

/**
 * 单个任务
 *
 * @author zido
 * @since 2017/30/21 下午2:30
 */
public abstract class PostTask extends GraphNode {

    public abstract boolean run();

}
