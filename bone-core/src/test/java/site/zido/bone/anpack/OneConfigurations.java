package site.zido.bone.anpack;

import site.zido.bone.core.beans.annotation.Bean;
import site.zido.bone.core.beans.annotation.Beans;

/**
 * site.zido.test.anpack
 *
 * @author zido
 */
@Beans
public class OneConfigurations {
    @Bean
    public One getOne(Two two) {
        One one = new One();
        one.setAge(25);
        one.setName("zido");
        one.setTwo(two);
        return one;
    }

    @Bean
    public Two getTwo() {
        Two two = new Two();
        two.setRender("render");
        return two;
    }

    @Bean(id = "three")
    public Three getThree(One one, Two two) {
        Three three = new Three();
        three.setOne(one);
        three.setTwo(two);
        return three;
    }
}
