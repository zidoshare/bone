package site.zido.test.components;

import site.zido.bone.core.beans.annotation.Bean;
import site.zido.bone.core.beans.annotation.Beans;

@Beans
public class CommonConfiguration {
    @Bean
    public One getOne(Two two) {
        One one = new One();
        one.setAge(26);
        one.setName("zido");
        one.setTwo(two);
        return one;
    }

    @Bean
    public Two getTwo() {
        Two two = new Two();
        two.setRender("two");
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
