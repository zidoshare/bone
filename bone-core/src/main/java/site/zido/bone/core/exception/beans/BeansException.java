package site.zido.bone.core.exception.beans;

import site.zido.bone.core.exception.NestedRuntimeException;

/**
 * bean 相关异常
 *
 * @author zido
 */
public abstract class BeansException extends NestedRuntimeException {
    public BeansException(String msg) {
        super(msg);
    }

    public BeansException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
