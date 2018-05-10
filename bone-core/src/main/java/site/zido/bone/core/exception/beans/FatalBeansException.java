package site.zido.bone.core.exception.beans;

/**
 * 描述Bean失败相关异常 ,例如错误的类或属性等
 *
 * @author zido
 * @date 2018 /05/10
 */
public class FatalBeansException extends AbstractBeansException {
    /**
     * Instantiates a new Fatal beans exception.
     *
     * @param msg the msg
     */
    public FatalBeansException(String msg) {
        super(msg);
    }

    /**
     * Instantiates a new Fatal beans exception.
     *
     * @param msg   the msg
     * @param cause the cause
     */
    public FatalBeansException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
