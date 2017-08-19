package site.zido.core.beans;

import site.zido.bone.logger.Logger;
import site.zido.bone.logger.impl.LogManager;
import site.zido.core.beans.structure.BeanConstruction;
import site.zido.core.beans.structure.Definition;
import site.zido.core.beans.structure.Param;
import site.zido.core.beans.structure.Property;
import site.zido.utils.commons.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 抽象解析类，扩展解析方式，集成此类，返回config即可。
 */
public abstract class AbsBeanParser implements BeanFactory,IBeanParser{

    private Logger logger = LogManager.getLogger(AbsBeanParser.class);
    public PostQueue postQueue;
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

    protected abstract Map<String,Definition> getConfig();
    @Override
    public void parser(){
        Map<String,Definition> config = getConfig();
        if(config != null){
            for(Map.Entry<String,Definition> entry : config.entrySet()){
                String beanId = entry.getKey();
                Definition definition = entry.getValue();

                Object object = createBean(definition);
                BoneIoc.getInstance().register(beanId,object);
            }
            PostQueue.execute(postQueue);
        }
    }
    protected Object createBean(Definition definition){
        String beanId = definition.getId();
        String className = definition.getClassName();

        Class _classzz;

        Object object;

        try{
            _classzz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("未找到相关class类:"+className);
        }

        try{
            BeanConstruction cons = definition.getConstruction();
            if(cons == null){
                object = _classzz.newInstance();
            }else{
                Constructor[] constructors = _classzz.getConstructors();
                if(constructors.length > 1)
                    throw new RuntimeException("bean应当有且仅有一个构造方法："+_classzz.getName());
                Constructor constructor = constructors[0];
                List<Param> params = cons.getParams();
                constructor.newInstance(params.toArray());
            }

            object = _classzz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("该类缺少一个构造方法:"+_classzz.getName());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        if(definition.getProperties() != null){
            for(Property p : definition.getProperties()){
                if(p.getValue() != null){
                    Method method = BeanUtils.getSetterMethod(object,p.getName());
                    BeanUtils.setField(object,method,p.getValue());
                }
                if(p.getRef() != null){
                    Method method = BeanUtils.getSetterMethod(object,p.getName());
                    Object o = getBean(p.getRef());
                    if(o == null){
                        if(postQueue == null)
                            postQueue = new PostQueue();
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
