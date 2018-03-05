package site.zido.bone.core.exception.beans;

/**
 * 描述Bean失败相关异常 ,例如错误的类或属性等
 *
 * @author zido
 */
public class FatalBeansException extends BeansException {
    public FatalBeansException(String msg) {
        super(msg);
    }

    public FatalBeansException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
