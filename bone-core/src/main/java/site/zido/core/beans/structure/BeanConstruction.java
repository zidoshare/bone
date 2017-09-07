package site.zido.core.beans.structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Bean 构造方法的定义存储结构，用于描述Bean的构造方法
 *
 * @author zido
 * @since 2017/19/21 下午2:19
 */
public class BeanConstruction {
    private List<DefParam> params = new ArrayList<>();
    private DefParam[] contents = new DefParam[0];

    public DefParam[] getParams() {
        int length = contents.length;
        if (length != params.size()) {
            contents = new DefParam[params.size()];
            for (int i = 0; i < length; i++) {
                contents[i] = params.get(i);
            }
        }
        return contents;
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
