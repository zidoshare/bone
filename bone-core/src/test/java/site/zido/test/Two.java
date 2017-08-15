package site.zido.test;

public class Two {
    private String render;

    public String getRender() {
        return render;
    }

    public void setRender(String render) {
        this.render = render;
    }

    @Override
    public String toString() {
        return "Two{" +
                "render='" + render + '\'' +
                '}';
    }
}
