package site.zido.bone.core.beans.structure;

import site.zido.bone.core.utils.Assert;

import java.lang.reflect.Constructor;

/**
 * Bean 构造方法的定义存储结构，用于描述Bean的构造方法
 *
 * @author zido
 * @since 2017/19/21 下午2:19
 */
public class DefConstruction {

    private DefProperty[] properties;

    private Constructor<?> constructor;

    public DefConstruction(Constructor<?> constructor) {
        Assert.notNull(constructor, "构造器不能为空");
        this.constructor = constructor;
    }

    public Constructor<?> getConstructor() {
        return constructor;
    }

    public void setConstructor(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    public DefProperty[] getProperties() {
        return properties;
    }

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
