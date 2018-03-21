package site.zido.bone.core.exception.beans;

public class CircleRelyException extends FatalBeansException {
    public CircleRelyException() {
        super("含有循环依赖");
    }
}
