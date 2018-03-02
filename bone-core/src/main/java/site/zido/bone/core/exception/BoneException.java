package site.zido.bone.core.exception;

public class BoneException extends Exception {
    public BoneException(String msg) {
        super(msg);
    }

    public BoneException(String msg, Throwable e) {
        this(msg);
        this.initCause(e);
    }
}
