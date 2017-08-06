package site.zido.utils.file;

import org.junit.Assert;
import org.junit.Test;
import site.zido.utils.commons.FileUtils;

/**
 * description
 * <p>fill description</p>
 *
 * @author zido
 * @since 2017/6/27 0027
 */
public class TestFileUtils {
    @Test
    public void testConfuseFileName(){
        String s = FileUtils.confuseFileName("123.jpg");
        System.out.println(s);
        Assert.assertNotEquals("测试不通过",s,"123.jpg");
    }
    @Test
    public void testHash(){
        StringBuilder sb = new StringBuilder("1");

        System.out.println(sb.hashCode());
    }
}
