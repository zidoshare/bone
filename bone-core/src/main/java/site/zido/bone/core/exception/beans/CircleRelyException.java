package site.zido.bone.core.exception.beans;

/**
 * The type Circle rely exception.
 *
 * @author zido
 * @date 2018 /05/10
 */
public class CircleRelyException extends FatalBeansException {
    /**
     * Instantiates a new Circle rely exception.
     */
    public CircleRelyException() {
        super("含有循环依赖");
    }
}
