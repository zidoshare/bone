package site.zido.bone.components;

public class One {
    private String name;
    private Integer age;
    private Two two;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Two getTwo() {
        return two;
    }

    public void setTwo(Two two) {
        this.two = two;
    }

    @Override
    public String toString() {
        return "One{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", two=" + two +
                '}';
    }
}
