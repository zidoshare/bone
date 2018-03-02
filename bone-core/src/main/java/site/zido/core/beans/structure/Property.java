package site.zido.core.beans.structure;

/**
 * 属性定义，用于描述类的属性
 *
 * @author zido
 * @since 2017/22/21 下午2:22
 */
public class Property {
    private String name;
    private String value;
    private String ref;
    private Class<?> type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    @Override
    public String toString() {
        return "Property{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", ref='" + ref + '\'' +
                '}';
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }
}
