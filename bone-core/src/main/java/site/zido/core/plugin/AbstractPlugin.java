package site.zido.core.plugin;

public interface  AbstractPlugin {
    //开始，环境开始时调用
    public void start();
    //执行，环境变化时调用（有可能是多次）
    public void run();
    //结束，环境结束时调用
    public void end();
}
