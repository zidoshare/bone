package site.zido.bone.core.exception.beans;

public class BadClassNameException extends FatalBeansException {
    public BadClassNameException(String className) {
        super(String.format("[%s] 未找到相关类", className));
    }
}
