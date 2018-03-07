package site.zido.bone.core.exception.beans;

import site.zido.bone.core.beans.PostTask;
import site.zido.bone.core.beans.structure.DefProperty;

import java.util.List;

/**
 * site.zido.bone.core.exception.beans
 *
 * @author zido
 */
public class LackRelyException extends FatalBeansException {
    public LackRelyException(PostTask data, List<DefProperty> propertyList) {
        super(data.toString() + " 缺少依赖:" + propertyList);
    }
}
