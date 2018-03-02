package site.zido.bone.core.beans;

/**
 * 异常，用来标注bean 构造方法异常，必须有且仅有一个构造函数
 *
 * @author zido
 * @since 2017/27/21 下午2:27
 */
public class BeanHaveOneConstructorException extends RuntimeException {
    private static String BEAN_MUST_HAVE_ONE_CONSTRUCTOR = "bean 构造方法异常，必须有且仅有一个构造函数";

    public BeanHaveOneConstructorException() {
        super(BEAN_MUST_HAVE_ONE_CONSTRUCTOR);
    }

    public BeanHaveOneConstructorException(Throwable e) {
        super(BEAN_MUST_HAVE_ONE_CONSTRUCTOR, e);
    }
}
