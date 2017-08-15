package site.zido.test;

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

        logger.info("hello world");
        AbsBeanParser parser = new XmlParser("/applicationContext.xml");
        parser.parser();
        One one = (One) BoneIoc.getInstance().getBean("one");
        System.out.println(one);

    }
}
