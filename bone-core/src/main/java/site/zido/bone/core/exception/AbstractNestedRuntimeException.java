package site.zido.bone.core.exception;

import site.zido.bone.core.utils.NestedRuntimeUtils;

/**
 * 包装运行时异常信息,规范化异常信息，框架内部使用
 *
 * @author zido
 * @date 2018 /05/10
 */
public abstract class AbstractNestedRuntimeException extends RuntimeException {
    /**
     * Instantiates a new Nested runtime exception.
     *
     * @param msg the msg
     */
    public AbstractNestedRuntimeException(String msg) {
        super(msg);
    }

    /**
     * Instantiates a new Nested runtime exception.
     *
     * @param msg   the msg
     * @param cause the cause
     */
    public AbstractNestedRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    @Override
    public String getMessage() {
        return NestedRuntimeUtils.buildMessage(super.getMessage(), getCause());
    }

    /**
     * Gets root cause.
     *
     * @return the root cause
     */
    public Throwable getRootCause() {
        return NestedRuntimeUtils.getRootCause(getCause());
    }

    /**
     * Gets original cause.
     *
     * @return the original cause
     */
    public Throwable getOriginalCause() {
        return NestedRuntimeUtils.getOriginalCause(getCause());
    }

    /**
     * 检测是否包含给定异常类
     *
     * @param type 检测类
     * @return true /false
     */
    public boolean contains(Class<?> type) {
        if (type == null) {
            return false;
        }
        if (type.isInstance(this)) {
            return true;
        }
        Throwable cause = getCause();
        if (cause == this) {
            return false;
        }
        if (cause instanceof AbstractNestedRuntimeException) {
            return ((AbstractNestedRuntimeException) cause).contains(type);
        } else {
            while (cause != null) {
                if (type.isInstance(cause)) {
                    return true;
                }
                if (cause.getCause() == cause) {
                    break;
                }
                cause = cause.getCause();
            }
            return false;
        }
    }
}
