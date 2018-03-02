package site.zido.bone.core.beans.structure;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean 构造方法的定义存储结构，用于描述Bean的构造方法
 *
 * @author zido
 * @since 2017/19/21 下午2:19
 */
public class BeanConstruction {
    private List<DefParam> params = new ArrayList<>();

    public List<DefParam> getParams() {
        return params;
    }

    public void addParam(DefParam p) {
        this.params.add(p);
    }

    @Override
    public String toString() {
        return "BeanConstruction{" +
                "params=" + params +
                '}';
    }
}
