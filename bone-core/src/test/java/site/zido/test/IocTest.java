package site.zido.test;

import org.junit.Assert;
import org.junit.Test;
import site.zido.bone.logger.Logger;
import site.zido.bone.logger.impl.LogManager;
import site.zido.core.beans.*;
import site.zido.test.anpack.CommonComponent;

import java.util.Set;

public class IocTest {
    /**
     * xml注入测试
     */
    @Test
    public void testXmlIoc() {
        IBeanParser xmlParser = new XmlParser("/applicationContext.xml");
        xmlParser.parser();
        One one = (One) BoneContext.getInstance().getBean("one");
        Two two = BoneContext.getInstance().getBean("two", Two.class);
        Three three = BoneContext.getInstance().getBean("three", Three.class);
        Assert.assertNotNull("one 注入失败", one);
        Assert.assertNotNull("two 注入失败", two);
        Assert.assertNotNull("three 注入失败", three);
    }

    /**
     * 注解注入测试
     */
    @Test
    public void testAnnotation() {
        IBeanParser parser = new AnnotationParser("site.zido.test.anpack");
        parser.parser();
        One one = (One) BoneContext.getInstance().getBean("one");
        Two two = BoneContext.getInstance().getBean("two", Two.class);
        Three three = BoneContext.getInstance().getBean("three", Three.class);
        Assert.assertNotNull("one 注入失败", one);
        Assert.assertNotNull("two 注入失败", two);
        Assert.assertNotNull("three 注入失败", three);
    }

    /**
     * Component注解测试
     */
    @Test
    public void testComponent() {
        IBeanParser parser = new AnnotationParser("site.zido.test.anpack");
        parser.parser();
        CommonComponent component = BoneContext.getInstance().getBean(CommonComponent.class);
        Assert.assertNotNull("component 注入失败", component);
    }
}
