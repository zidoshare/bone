package site.zido.core.beans;

public class ExistsBeanException extends RuntimeException {
    public ExistsBeanException() {
        super("bean 已存在");
    }
}
