package site.zido.core.beans;

import site.zido.core.exception.BoneRuntimeException;

public class BeanHaveOneStrucureException extends BoneRuntimeException {
    private static String BEAN_MUST_HAVE_ONE_STRRCURE = "bean 构造方法异常，必须有且仅有一个构造函数";
    public BeanHaveOneStrucureException(){
        super(BEAN_MUST_HAVE_ONE_STRRCURE);
    }

    public BeanHaveOneStrucureException(Throwable e){
        super(BEAN_MUST_HAVE_ONE_STRRCURE,e);

    }
}
