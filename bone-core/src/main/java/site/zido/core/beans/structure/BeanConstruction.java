package site.zido.core.beans.structure;

import java.util.ArrayList;
import java.util.List;
/**
 * Bean 构造方法的定义存储结构，用于描述Bean的构造方法
 *
 * @author zido
 * @since 2017/19/21 下午2:19
 */
public class BeanConstruction {
    private List<Param> params = new ArrayList<>();

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "BeanConstruction{" +
                "params=" + params +
                '}';
    }
}
