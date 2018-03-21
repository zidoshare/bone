package site.zido.bone.components;

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
    public Two getTwo(Five five) {
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

    @Bean
    public Four getFour(Two two) {
        return new Four();
    }

    @Bean
    public Five getFive() {
        return new Five();
    }
}
