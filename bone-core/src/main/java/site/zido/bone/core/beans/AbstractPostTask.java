package site.zido.bone.core.beans;

import site.zido.bone.core.beans.structure.DefProperty;
import site.zido.bone.core.beans.structure.Definition;
import site.zido.bone.core.beans.structure.DelayMethod;
import site.zido.bone.core.utils.ValiDateUtils;

/**
 * 单个任务
 *
 * @author zido
 * @date 2018 /05/10
 * @since 2017 /30/21 下午2:30
 */
public abstract class AbstractPostTask {

    /**
     * The Properties.
     */
    private DefProperty[] properties = new DefProperty[0];
    /**
     * The Definition.
     */
    private Definition definition;
    /**
     * The Delay method.
     */
    private DelayMethod delayMethod;
    /**
     * The Tail counter.
     */
    private int tailCounter = 0;
    /**
     * The Property.
     */
    private DefProperty property;

    /**
     * 真正执行的内容
     *
     * @param params 需要的参数
     */
    public abstract void execute(Object[] params);

    /**
     * Run boolean.
     *
     * @return the boolean
     */
    public boolean run() {
        DefProperty[] properties = getProperties();
        if (properties == null || properties.length == 0) {
            execute(null);
            return true;
        }
        Object[] params = new Object[properties.length];
        for (int i = 0; i < properties.length; i++) {
            DefProperty defProperty = properties[i];
            if (!ValiDateUtils.isEmpty(defProperty.getValue())) {
                params[i] = defProperty.getValue();
            } else if (!ValiDateUtils.isEmpty(defProperty.getRef())) {
                params[i] = BoneContext.getInstance().getBean(defProperty.getRef());
            } else if (defProperty.getType() != null) {
                params[i] = BoneContext.getInstance().getBean(defProperty.getType());
            }
            if (params[i] == null) {
                return false;
            }
        }
        execute(params);
        return true;
    }

    /**
     * Get properties def property [ ].
     *
     * @return the def property [ ]
     */
    public DefProperty[] getProperties() {
        return properties;
    }

    /**
     * Need.
     *
     * @param properties the properties
     */
    public void need(DefProperty[] properties) {
        this.properties = properties;
        this.tailCounter = properties.length;
    }

    /**
     * Produce.
     *
     * @param definition the definition
     */
    public void produce(Definition definition) {
        this.definition = definition;
    }

    /**
     * Gets definition.
     *
     * @return the definition
     */
    public Definition getDefinition() {
        return definition;
    }

    /**
     * Gets tail counter.
     *
     * @return the tail counter
     */
    public int getTailCounter() {
        return tailCounter;
    }

    /**
     * Reduce int.
     *
     * @return the int
     */
    public int reduce() {
        return --tailCounter;
    }

    @Override
    public String toString() {
        return definition != null ?
                String.format("生成bean[id:%s,type:%s]", getDefinition().getId(), getDefinition().getType().getName()) :
                delayMethod != null?String.format("执行延迟方法%s", delayMethod)
                        :String.format("执行延迟注入属性%s", property);
    }

    /**
     * Produce.
     *
     * @param delayMethod the delay method
     */
    public void produce(DelayMethod delayMethod) {
        this.delayMethod = delayMethod;
    }

    /**
     * Produce.
     *
     * @param property the property
     */
    public void produce(DefProperty property) {
        this.property = property;
    }
}
