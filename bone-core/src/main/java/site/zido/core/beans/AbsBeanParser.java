package site.zido.core.beans;

import site.zido.bone.logger.Logger;
import site.zido.bone.logger.impl.LogManager;
import site.zido.utils.commons.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 抽象解析类，扩展解析方式，集成此类，返回config即可。
 */
public abstract class AbsBeanParser implements BeanFactory{

    private Logger logger = LogManager.getLogger(AbsBeanParser.class);
    //parser 不重复
    public static Set<AbsBeanParser> parsers = new HashSet<>();
    public static void registParser(AbsBeanParser parser){
        parsers.add(parser);
    }
    public PostBeanQueue postQueue;
    @Override
    public Object getBean(String name) {
        return BoneIoc.getInstance().getBean(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> requireType) {
        return BoneIoc.getInstance().getBean(name,requireType);
    }

    @Override
    public <T> T getBean(Class<T> requireType) {
        return BoneIoc.getInstance().getBean(requireType);
    }

    protected abstract Map<String,Bean> getConfig();

    public void parser(){
        Map<String,Bean> config = getConfig();
        if(config != null){
            for(Map.Entry<String,Bean> entry : config.entrySet()){
                String beanId = entry.getKey();
                Bean bean = entry.getValue();

                Object object = createBean(bean);
                BoneIoc.getInstance().register(beanId,object);
            }
            PostBeanQueue.execute(postQueue);
        }
    }
    protected Object createBean(Bean bean){
        String beanId = bean.getId();
        String className = bean.getClassName();

        Class _classzz = null;

        Object object = null;

        try{
            _classzz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("未找到相关class类:"+className);
        }

        try{
            object = _classzz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("该类缺少一个无参构造方法:"+_classzz.getName());
        }

        if(bean.getProperties() != null){
            for(Property p : bean.getProperties()){
                if(p.getValue() != null){
                    Method method = BeanUtils.getSetterMethod(object,p.getName());
                    BeanUtils.setField(object,method,p.getValue());
                }
                if(p.getRef() != null){
                    Method method = BeanUtils.getSetterMethod(object,p.getName());
                    Object o = getBean(p.getRef());
                    if(o == null){
                        if(postQueue == null)
                            postQueue = new PostBeanQueue();
                        Object finalObject = object;
                        postQueue.addTask(() -> {
                            Object other = BoneIoc.getInstance().getBean(p.getRef());
                            if(other == null)
                                return false;
                            BeanUtils.setField(finalObject,method,other);
                            return true;
                        });
                    }else{
                        BeanUtils.setField(object,method,o);
                    }

                }
            }
        }
        return object;
    }
}
