package site.zido.core.beans.handler;

/**
 * 环境抽象类，主要用于标示适配
 *
 * @author zido
 * @since 2017/18/21 下午2:18
 */
public abstract class EnvResolver {
    public abstract void onStart();

    public abstract void onChange();

    public abstract void onEnd();
}
