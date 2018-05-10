package site.zido.bone.core.exception.beans;

import site.zido.bone.core.exception.AbstractNestedRuntimeException;

/**
 * bean 相关异常
 *
 * @author zido
 * @date 2018 /05/10
 */
public abstract class AbstractBeansException extends AbstractNestedRuntimeException {
    /**
     * Instantiates a new Beans exception.
     *
     * @param msg the msg
     */
    public AbstractBeansException(String msg) {
        super(msg);
    }

    /**
     * Instantiates a new Beans exception.
     *
     * @param msg   the msg
     * @param cause the cause
     */
    public AbstractBeansException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
