package site.zido.core.beans;

public class BoneIoc implements BeanFactory,BeanProvider{
    private static BoneIoc boneIoc = new BoneIoc();
    public static BoneIoc getInstance(){
        return boneIoc;
    }
    @Override
    public Object getBean(String name) {
        return null;
    }

    @Override
    public <T> T getBean(String name, Class<T> requireType) {
        return null;
    }

    @Override
    public <T> T getBean(Class<T> requireType) {
        return null;
    }

    @Override
    public void register(String name, Class<?> requireType, Object o) {

    }

    @Override
    public void register(String name, Object o) {

    }

    @Override
    public void register(Class<?> requireType) {

    }
}
