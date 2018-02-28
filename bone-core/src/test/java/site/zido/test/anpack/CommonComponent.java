package site.zido.test.anpack;

import site.zido.core.beans.annotation.Component;
import site.zido.test.One;
import site.zido.test.Three;
import site.zido.test.Two;

import javax.inject.Inject;
import javax.inject.Named;

@Component
public class CommonComponent {
    private One one;
    private Two two;
    private Three three;

    public CommonComponent(One one) {
        this.one = one;
    }

    @Inject
    public void setTwo(Two two) {
        this.two = two;
    }

    @Inject
    public void setThree(@Named("three") Three three) {
        this.three = three;
    }

    public One getOne() {
        return one;
    }

    public Two getTwo() {
        return two;
    }

    public Three getThree() {
        return three;
    }
}
