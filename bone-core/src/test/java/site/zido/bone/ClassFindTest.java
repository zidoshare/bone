package site.zido.bone;

import org.junit.Assert;
import org.junit.Test;
import site.zido.bone.core.beans.AnnotationParser;

import java.util.Set;

public class ClassFindTest {
    @Test
    public void findTest(){
        AnnotationParser parser = new AnnotationParser("site.zido.core.beans");
        Set<Class<?>> classes = parser.loadClasses();
        Assert.assertTrue("类扫描失败",!classes.isEmpty());
    }
}
