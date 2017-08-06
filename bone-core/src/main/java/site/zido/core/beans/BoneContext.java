package site.zido.core.beans;

/**
 * 容器
 */
public class BoneContext implements BeanFactory{
    private static BoneContext boneContext = new BoneContext();
    private BoneContext(){}

    public static BoneContext getInstance(){
        return boneContext;
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
}
