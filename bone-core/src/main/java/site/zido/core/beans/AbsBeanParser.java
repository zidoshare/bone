package site.zido.core.beans;

import site.zido.utils.commons.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 抽象解析类，扩展解析方式，集成此类，返回config即可。
 */
public abstract class AbsBeanParser implements BeanFactory{
    //parser 不重复
    public static Set<AbsBeanParser> parsers = new HashSet<>();
    public static void registParser(AbsBeanParser parser){
        parsers.add(parser);
    }

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
        Map<String,Bean>config = getConfig();
        if(config != null){
            for(Map.Entry<String,Bean> entry : config.entrySet()){
                String beanId = entry.getKey();
                Bean bean = entry.getValue();

                Object object = createBean(bean);
                BoneIoc.getInstance().register(beanId,object);
            }
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
                    if(method == null){
                        throw new RuntimeException("没有相应的setter方法:"+object.getClass().getName()+"."+p.getName());
                    }
                    try {
                        method.invoke(object,p.getValue());
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException("属性名称不合法或者没有相应的setter方法："+object.getClass().getName()+"."+p.getName());
                    }
                }
                if(p.getRef() != null){
                    Method method = BeanUtils.getSetterMethod(object,p.getName());
                    if(method == null){
                        throw new RuntimeException("属性名称不合法或者没有相应的setter方法："+object.getClass().getName()+"."+p.getName());
                    }
                    Object o = getBean(p.getRef());
                    if(o == null){
                        throw new RuntimeException("容器内没有相应的对象:"+p.getRef());
                    }else{
                        try{
                            method.invoke(object,o);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException("属性名称不合法或者没有相应的setter方法："+object.getClass().getName()+"."+p.getName());
                        }
                    }
                }
            }
        }
        return object;
    }
}
