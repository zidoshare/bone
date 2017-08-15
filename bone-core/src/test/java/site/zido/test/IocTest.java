package site.zido.test;

import org.junit.Test;
import site.zido.core.beans.AbsBeanParser;
import site.zido.core.beans.BoneIoc;
import site.zido.core.beans.XmlParser;

public class IocTest {
    @Test
    public void testIoc(){
        AbsBeanParser parser = new XmlParser("/applicationContext.xml");
        parser.parser();

        One one = (One) BoneIoc.getInstance().getBean("one");
        System.out.println(one);

    }
}
