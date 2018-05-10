package site.zido.bone.core.beans.structure;

import site.zido.bone.core.utils.Assert;

import java.lang.reflect.Constructor;

/**
 * Bean 构造方法的定义存储结构，用于描述Bean的构造方法
 *
 * @author zido
 * @date 2018 /05/10
 * @since 2017 /19/21 下午2:19
 */
public class DefConstruction {

    /**
     * The Properties.
     */
    private DefProperty[] properties;

    /**
     * The Constructor.
     */
    private Constructor<?> constructor;

    /**
     * Instantiates a new Def construction.
     *
     * @param constructor the constructor
     */
    public DefConstruction(Constructor<?> constructor) {
        Assert.notNull(constructor, "构造器不能为空");
        this.constructor = constructor;
    }

    /**
     * Gets constructor.
     *
     * @return the constructor
     */
    public Constructor<?> getConstructor() {
        return constructor;
    }

    /**
     * Sets constructor.
     *
     * @param constructor the constructor
     */
    public void setConstructor(Constructor<?> constructor) {
        this.constructor = constructor;
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

    @Override
    public String toString() {
        return "DefConstruction{" +
                "properties=" + properties +
                ", constructor=" + constructor +
                '}';
    }
}
