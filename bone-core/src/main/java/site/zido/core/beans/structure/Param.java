package site.zido.core.beans.structure;

public class Param {
    private String type;
    private String value;
    private String ref;

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

    @Override
    public String toString() {
        return "Param{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                ", ref='" + ref + '\'' +
                '}';
    }
}
