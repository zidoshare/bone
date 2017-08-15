package site.zido.test;

import org.junit.Assert;
import org.junit.Test;
import site.zido.bone.logger.Logger;
import site.zido.bone.logger.impl.LogManager;
import site.zido.core.beans.AbsBeanParser;
import site.zido.core.beans.BoneIoc;
import site.zido.core.beans.XmlParser;

public class IocTest {
    @Test
    public void testIoc(){
        Logger logger = LogManager.getLogger(IocTest.class);


        AbsBeanParser parser = new XmlParser("/applicationContext.xml");
        parser.parser();
        One one = (One)BoneIoc.getInstance().getBean("one");

        Two two = BoneIoc.getInstance().getBean("two", Two.class);

        Three three = BoneIoc.getInstance().getBean("three", Three.class);

        Assert.assertNotNull("one 注入失败",one);
        Assert.assertNotNull("two 注入失败",two);
        Assert.assertNotNull("three 注入失败",three);
        logger.info(three.toString());
        logger.info("xml注入成功");
    }
}
