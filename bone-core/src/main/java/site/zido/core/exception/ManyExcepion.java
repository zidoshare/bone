package site.zido.core.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * 本异常没有特殊意义，仅用来封装多个异常
 */
public class ManyExcepion extends Exception{
    //容纳所有异常
    private List<Throwable> causes = new ArrayList<Throwable>();
    //构造函数，传递一个异常列表
    public ManyExcepion(List<? extends Throwable> _causes){
        causes.addAll(_causes);
    }

    public <T extends Throwable> boolean addExce(T cause){
        return causes.add(cause);
    }

    //读取所有的异常
    public List<Throwable> getException(){
        return causes;
    }
}
