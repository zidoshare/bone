package site.zido.bone.core.exception.beans;

/**
 * The type Bad class name exception.
 *
 * @author zido
 * @date 2018 /05/10
 */
public class BadClassNameException extends FatalBeansException {
    /**
     * Instantiates a new Bad class name exception.
     *
     * @param className the class name
     */
    public BadClassNameException(String className) {
        super(String.format("[%s] 未找到相关类", className));
    }
}
