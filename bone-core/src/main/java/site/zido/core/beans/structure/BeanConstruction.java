package site.zido.core.beans.structure;

import java.util.ArrayList;
import java.util.List;

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
