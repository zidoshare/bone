package site.zido.bone.logger;

public abstract class Logger {
    protected Logger(String name) {

    }

    protected Logger(Class<?> classzz) {

    }

    public abstract void info(String msg);

    public abstract void error(String msg);

    public void error(String msg, Throwable t) {
        error(msg);
        t.printStackTrace();
    }

    public abstract void debug(String msg);

    public abstract void warn(String msg);

    public abstract void trace(String msg);
}
