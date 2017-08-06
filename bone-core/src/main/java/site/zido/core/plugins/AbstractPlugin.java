package site.zido.core.plugins;

public abstract class AbstractPlugin {
    //开始，环境开始时调用
    public abstract void start();
    //执行，环境变化时调用（有可能是多次）
    public abstract void run();
    //结束，环境结束时调用
    public abstract void end();
}
