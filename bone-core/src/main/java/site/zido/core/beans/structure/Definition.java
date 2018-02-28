package site.zido.core.beans.structure;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean的定义，描述Bean,工厂会通过定义来实例化Bean
 *
 * @author zido
 * @since 2017/21/21 下午2:21
 */
public class Definition {
    private String id = "";
    private String className;
    private List<Property> properties = new ArrayList<>();
    /**
     * 存储类的构造方法，以代理生成bin
     */
    private BeanConstruction construction;
    /**
     * 最终实例
     */
    private Object object;

    private List<DelayMethod> delayMethods = new ArrayList<>(3);

    public Definition() {

    }

    public Definition(String id, String className, List<Property> properties, BeanConstruction construction) {
        this.id = id;
        this.className = className;
        this.properties = properties;
        this.construction = construction;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public BeanConstruction getConstruction() {
        return construction;
    }

    public void setConstruction(BeanConstruction construction) {
        this.construction = construction;
    }

    @Override
    public String toString() {
        return "Definition{" +
                "id='" + id + '\'' +
                ", className='" + className + '\'' +
                ", properties=" + properties +
                ", construction=" + construction +
                '}';
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
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
}
