package site.zido.bone.core.beans;

public abstract class ExtraBeanExecuteTask extends PostTask {
    public abstract boolean check();

    @Override
    public final boolean run() {
        return check() && super.run();
    }

    public ExtraBeanExecuteTask need(BeanExecuteTask task) {
        super.need(task.getProperties());
        return this;
    }
}
