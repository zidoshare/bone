package site.zido.bone.core.exception.beans;

/**
 * 标注bean 构造方法异常，必须有且仅有一个构造函数
 *
 * @author zido
 */
public class BeanHaveOneConstructorException extends FatalBeansException {
    private static String BEAN_MUST_HAVE_ONE_CONSTRUCTOR = "bean 构造方法异常，必须有且仅有一个构造方法";

    public BeanHaveOneConstructorException(Class<?> classzz) {
        super(String.format("[%s]" + BEAN_MUST_HAVE_ONE_CONSTRUCTOR, classzz.getName()));
    }
}
