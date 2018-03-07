package site.zido.bone.core.beans.structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Bean的定义，描述Bean,工厂会通过定义来实例化Bean
 *
 * @author zido
 * @since 2017/21/21 下午2:21
 */
public class Definition {
    private String id = "";
    private Class<?> type;
    private DefProperty[] properties = new DefProperty[0];
    /**
     * 存储类的构造方法，以代理生成bin
     */
    private DefConstruction construction;

    private List<DelayMethod> delayMethods = new ArrayList<>(3);

    private boolean isClass = false;

    public Definition() {

    }

    public Definition(String id, DefProperty[] properties, DefConstruction construction) {
        this.id = id;
        this.properties = properties;
        this.construction = construction;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DefProperty[] getProperties() {
        return properties;
    }

    public void setProperties(DefProperty[] properties) {
        this.properties = properties;
    }

    public DefConstruction getConstruction() {
        return construction;
    }

    public void setConstruction(DefConstruction construction) {
        this.construction = construction;
    }

    public List<DelayMethod> getDelayMethods() {
        return delayMethods;
    }

    public void setDelayMethods(List<DelayMethod> delayMethods) {
        this.delayMethods = delayMethods;
    }

    public void addDelayMethod(DelayMethod delayMethod) {
        delayMethods.add(delayMethod);
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> clazz) {
        this.type = clazz;
    }

    @Override
    public String toString() {
        return "Definition{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", properties=" + Arrays.toString(properties) +
                ", construction=" + construction +
                ", delayMethods=" + delayMethods +
                '}';
    }

    public boolean inProperties(DefProperty[] other) {
        for (DefProperty property : other) {
            String ref = property.getRef();
            Class<?> type = property.getType();
            if (ref.equals(id) && type == this.type)
                return true;
        }
        return false;
    }

    public boolean isClass() {
        return isClass;
    }

    public void isClass(boolean aClass) {
        isClass = aClass;
    }
}
