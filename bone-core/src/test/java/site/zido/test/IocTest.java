package site.zido.test;

import org.junit.Assert;
import org.junit.Test;
import site.zido.bone.core.beans.AnnotationParser;
import site.zido.bone.core.beans.BoneContext;
import site.zido.bone.core.beans.IBeanParser;
import site.zido.bone.core.beans.XmlParser;
import site.zido.test.anpack.One;
import site.zido.test.anpack.Three;
import site.zido.test.anpack.Two;
import site.zido.test.components.CommonComponent;

public class IocTest {
    /**
     * xml注入测试
     */
    @Test
    public void testXmlIoc() {
        IBeanParser xmlParser = new XmlParser("/applicationContext.xml");
        xmlParser.parser();
        Object one = BoneContext.getInstance().getBean("one");
        Object two = BoneContext.getInstance().getBean("two", site.zido.test.xml.Two.class);
        Object three = BoneContext.getInstance().getBean("three", site.zido.test.xml.Three.class);
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
        Object one = BoneContext.getInstance().getBean(One.class);
        Object two = BoneContext.getInstance().getBean(Two.class);
        Object three = BoneContext.getInstance().getBean("three");
        Assert.assertNotNull("one 注入失败", one);
        Assert.assertNotNull("two 注入失败", two);
        Assert.assertNotNull("three 注入失败", three);
    }

    /**
     * Component注解测试
     */
    @Test
    public void testComponent() {
        IBeanParser parser = new AnnotationParser("site.zido.test.components");
        parser.parser();
        CommonComponent component = (CommonComponent) BoneContext.getInstance().getBean("common", CommonComponent.class);
        Assert.assertNotNull("component 注入失败", component);
        Assert.assertNotNull("Inject 属性注入失败", component.getOtherOne());
    }
}
