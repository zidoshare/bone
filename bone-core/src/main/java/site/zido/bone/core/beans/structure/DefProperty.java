package site.zido.bone.core.beans.structure;

/**
 * 属性定义，用于描述类的属性,方法的参数
 *
 * @author zido
 * @date 2018 /05/10
 * @since 2017 /22/21 下午2:22
 */
public class DefProperty {
    /**
     * The Name.
     */
    private String name;
    /**
     * The Value.
     */
    private String value;
    /**
     * The Ref.
     */
    private String ref = "";
    /**
     * The Type.
     */
    private Class<?> type;
    /**
     * The Target.
     */
    private Object target;

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets ref.
     *
     * @return the ref
     */
    public String getRef() {
        return ref;
    }

    /**
     * Sets ref.
     *
     * @param ref the ref
     */
    public void setRef(String ref) {
        this.ref = ref;
    }

    @Override
    public String toString() {
        return "DefProperty{" +
                "ref='" + ref + '\'' +
                ", type=" + type +
                '}';
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(Class<?> type) {
        this.type = type;
    }

    /**
     * Gets target.
     *
     * @return the target
     */
    public Object getTarget() {
        return target;
    }

    /**
     * Sets target.
     *
     * @param target the target
     */
    public void setTarget(Object target) {
        this.target = target;
    }
}
