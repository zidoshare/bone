package site.zido.core.exception;

public class BoneRuntimeException extends RuntimeException{
    public BoneRuntimeException(String msg){
        super(msg);
    }

    public BoneRuntimeException(String msg,Throwable e){
        this(msg);
        this.initCause(e);
    }
}
