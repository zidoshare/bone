package site.zido.core.beans.structure;

/**
 * 参数定义，描述方法的参数
 *
 * @author zido
 * @since 17-8-22 下午2:14
 */
public class DefParam {
    private Class<?> type;
    private String value;
    private String ref;

    public DefParam() {

    }

    public DefParam(Class<?> type, String value, String ref) {
        this.type = type;
        this.value = value;
        this.ref = ref;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
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
}
