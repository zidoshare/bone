package site.zido.bone.components;

import site.zido.bone.core.beans.annotation.Component;

import javax.inject.Inject;
import javax.inject.Named;

@Component(id = "common")
public class CommonComponent {
    private One one;
    private Two two;
    private Three three;
    @Inject
    private One otherOne;

    @Inject
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

    public One getOtherOne() {
        return otherOne;
    }

    public void setOtherOne(One otherOne) {
        this.otherOne = otherOne;
    }
}
