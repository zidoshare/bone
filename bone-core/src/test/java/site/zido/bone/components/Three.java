package site.zido.bone.components;

public class Three {
    private One one;
    private Two two;


    public One getOne() {
        return one;
    }

    public void setOne(One one) {
        this.one = one;
    }

    public Two getTwo() {
        return two;
    }

    public void setTwo(Two two) {
        this.two = two;
    }

    @Override
    public String toString() {
        return "Three{" +
                "one=" + one +
                ", two=" + two +
                '}';
    }
}
