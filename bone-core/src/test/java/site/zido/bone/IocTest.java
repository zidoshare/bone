package site.zido.bone;

import org.junit.Assert;
import org.junit.Test;
import site.zido.bone.anpack.One;
import site.zido.bone.components.CommonComponent;
import site.zido.bone.core.beans.AnnotationParser;
import site.zido.bone.core.beans.BoneContext;
import site.zido.bone.core.beans.IBeanParser;
import site.zido.bone.core.beans.XmlParser;
import site.zido.bone.xml.Three;
import site.zido.bone.xml.Two;

public class IocTest {
    /**
     * xml注入测试
     */
    @Test
    public void testXmlIoc() {
        IBeanParser xmlParser = new XmlParser("/applicationContext.xml");
        xmlParser.parser();
        Object one = BoneContext.getInstance().getBean("one");
        Object two = BoneContext.getInstance().getBean("two", Two.class);
        Object three = BoneContext.getInstance().getBean("three", Three.class);
        Assert.assertNotNull("one 注入失败", one);
        Assert.assertNotNull("two 注入失败", two);
        Assert.assertNotNull("three 注入失败", three);
    }

    /**
     * 注解注入测试
     */
    @Test
    public void testAnnotation() {
        AnnotationParser parser = new AnnotationParser("site.zido.bone.anpack");
        parser.parser();
        Object one = BoneContext.getInstance().getBean(One.class);
        Object two = BoneContext.getInstance().getBean(site.zido.bone.anpack.Two.class);
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
        IBeanParser parser = new AnnotationParser("site.zido.bone.components");
        parser.parser();
        CommonComponent component = (CommonComponent) BoneContext.getInstance().getBean("common", CommonComponent.class);
        Assert.assertNotNull("component 注入失败", component);
        Assert.assertNotNull("Inject 属性注入失败", component.getOtherOne());
    }
}
