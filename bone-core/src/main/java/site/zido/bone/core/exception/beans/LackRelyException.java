package site.zido.bone.core.exception.beans;

import site.zido.bone.core.beans.AbstractPostTask;
import site.zido.bone.core.beans.structure.DefProperty;

import java.util.List;

/**
 * The type Lack rely exception.
 *
 * @author zido
 * @date 2018 /05/10
 */
public class LackRelyException extends FatalBeansException {
    /**
     * Instantiates a new Lack rely exception.
     *
     * @param data         the data
     * @param propertyList the property list
     */
    public LackRelyException(AbstractPostTask data, List<DefProperty> propertyList) {
        super(data.toString() + " 缺少依赖:" + propertyList);
    }
}
