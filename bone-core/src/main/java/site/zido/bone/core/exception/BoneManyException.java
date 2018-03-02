package site.zido.bone.core.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * 本异常没有特殊意义，仅用来封装多个异常
 *
 * @author zido
 * @since 2017/31/21 下午2:31
 */
public class BoneManyException extends Exception {
    //容纳所有异常
    private List<Throwable> causes = new ArrayList<>();

    //构造函数，传递一个异常列表
    public BoneManyException(List<? extends Throwable> _causes) {
        causes.addAll(_causes);
    }

    public <T extends Throwable> boolean addException(T cause) {
        return causes.add(cause);
    }

    //读取所有的异常
    public List<Throwable> getExceptions() {
        return causes;
    }
}
