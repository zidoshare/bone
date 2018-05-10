package site.zido.bone.core.beans.structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Bean的定义，描述Bean,工厂会通过定义来实例化Bean
 *
 * @author zido
 * @date 2018 /05/10
 * @since 2017 /21/21 下午2:21
 */
public class Definition {
    /**
     * The Id.
     */
    private String id = "";
    /**
     * The Type.
     */
    private Class<?> type;
    /**
     * The Properties.
     */
    private DefProperty[] properties = new DefProperty[0];
    /**
     * 存储类的构造方法，以代理生成bin
     */
    private DefConstruction construction;

    /**
     * The Delay methods.
     */
    private List<DelayMethod> delayMethods = new ArrayList<>(3);

    /**
     * The Is class.
     */
    private boolean isClass = false;

    /**
     * Instantiates a new Definition.
     */
    public Definition() {

    }

    /**
     * Instantiates a new Definition.
     *
     * @param id           the id
     * @param properties   the properties
     * @param construction the construction
     */
    public Definition(String id, DefProperty[] properties, DefConstruction construction) {
        this.id = id;
        this.properties = properties;
        this.construction = construction;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
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
     * Sets properties.
     *
     * @param properties the properties
     */
    public void setProperties(DefProperty[] properties) {
        this.properties = properties;
    }

    /**
     * Gets construction.
     *
     * @return the construction
     */
    public DefConstruction getConstruction() {
        return construction;
    }

    /**
     * Sets construction.
     *
     * @param construction the construction
     */
    public void setConstruction(DefConstruction construction) {
        this.construction = construction;
    }

    /**
     * Gets delay methods.
     *
     * @return the delay methods
     */
    public List<DelayMethod> getDelayMethods() {
        return delayMethods;
    }

    /**
     * Sets delay methods.
     *
     * @param delayMethods the delay methods
     */
    public void setDelayMethods(List<DelayMethod> delayMethods) {
        this.delayMethods = delayMethods;
    }

    /**
     * Add delay method.
     *
     * @param delayMethod the delay method
     */
    public void addDelayMethod(DelayMethod delayMethod) {
        delayMethods.add(delayMethod);
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
     * @param clazz the clazz
     */
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

    /**
     * In properties boolean.
     *
     * @param other the other
     * @return the boolean
     */
    public boolean inProperties(DefProperty[] other) {
        for (DefProperty property : other) {
            String ref = property.getRef();
            Class<?> type = property.getType();
            if (ref.equals(id) && type == this.type) {
                return true;
            }
        }
        return false;
    }

    /**
     * Is class boolean.
     *
     * @return the boolean
     */
    public boolean isClass() {
        return isClass;
    }

    /**
     * Is class.
     *
     * @param aClass the a class
     */
    public void isClass(boolean aClass) {
        isClass = aClass;
    }
}
