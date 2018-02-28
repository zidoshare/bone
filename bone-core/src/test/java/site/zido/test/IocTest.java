package site.zido.test;

import org.junit.Assert;
import org.junit.Test;
import site.zido.bone.logger.Logger;
import site.zido.bone.logger.impl.LogManager;
import site.zido.core.beans.*;

import java.util.Set;

public class IocTest {
    /**
     * xml注入测试
     */
    @Test
    public void testXmlIoc() {
        Logger logger = LogManager.getLogger(IocTest.class);
        XmlParser xmlParser = new XmlParser("/applicationContext.xml");
        xmlParser.parser();
        One one = (One) BoneContext.getInstance().getBean("one");

        Two two = BoneContext.getInstance().getBean("two", Two.class);

        Three three = BoneContext.getInstance().getBean("three", Three.class);

        Assert.assertNotNull("one 注入失败", one);
        Assert.assertNotNull("two 注入失败", two);
        Assert.assertNotNull("three 注入失败", three);
        logger.info(three.toString());
        logger.info("xml注入成功");
    }

    /**
     * 注解注入测试
     */
    @Test
    public void testAnnotation() {
        Logger logger = LogManager.getLogger(IocTest.class);
        AnnotationParser parser = new AnnotationParser("site.zido.test.anpack");
        Set<Class<?>> classes = parser.loadClasses();
        parser.setClasses(classes);
        parser.parser();
        One one = (One) BoneContext.getInstance().getBean("one");

        Two two = BoneContext.getInstance().getBean("two", Two.class);

        Three three = BoneContext.getInstance().getBean("three", Three.class);

        Assert.assertNotNull("one 注入失败", one);
        Assert.assertNotNull("two 注入失败", two);
        Assert.assertNotNull("three 注入失败", three);
        logger.info(three.toString());
        logger.info("类注入成功");
    }
}
