package site.zido.core.beans;

/**
 * 容器
 * @deprecated 已废除，没删除，是因为有个地方引用，暂时不想改
 */
public class BoneContext implements BeanFactory {
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
