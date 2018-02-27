package site.zido.core.beans;

import site.zido.bone.logger.Logger;
import site.zido.bone.logger.impl.LogManager;
import site.zido.core.beans.structure.*;
import site.zido.core.utils.ReflectionUtils;
import site.zido.utils.commons.BeanUtils;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 抽象解析类，扩展解析方式，继承此类，返回config即可。
 *
 * @author zido
 * @since 2017/23/21 下午2:23
 */
public abstract class AbsBeanParser implements BeanFactory, IBeanParser {

    public PostQueue postQueue;
    private Logger logger = LogManager.getLogger(AbsBeanParser.class);

    @Override
    public Object getBean(String name) {
        return BoneIoc.getInstance().getBean(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> requireType) {
        return BoneIoc.getInstance().getBean(name, requireType);
    }

    @Override
    public <T> T getBean(Class<T> requireType) {
        return BoneIoc.getInstance().getBean(requireType);
    }

    protected abstract Map<String, Definition> getConfig();

    @Override
    public void parser() {
        Map<String, Definition> config = getConfig();
        if (config != null) {
            for (Map.Entry<String, Definition> entry : config.entrySet()) {
                Definition definition = entry.getValue();
                registerBean(definition);
            }
            PostQueue.execute(postQueue);
        }
    }

    /**
     * 通过Bean的定义来生成实例
     *
     * @param definition Bean描述
     * @return 实例
     */
    protected void registerBean(Definition definition) {
        if (definition.getObject() != null) {
            Object object = definition.getObject();
            BoneIoc.getInstance().register(definition.getId(), object);
            return;
        }
        DelayMethod delayMethod = definition.getDelayMethod();
        if (delayMethod != null) {
            if (postQueue == null)
                postQueue = new PostQueue();
            postQueue.addTask(() -> {
                String[] paramNames = delayMethod.getParamNames();
                Class<?>[] paramTypes = delayMethod.getParamTypes();
                Object[] params = new Object[paramTypes.length];
                for (int i = 0; i < paramTypes.length; i++) {
                    Object param = BoneIoc.getInstance().getBean(paramTypes[i]);
                    if (param == null) {
                        param = BoneIoc.getInstance().getBean(paramNames[i]);
                        if (param == null)
                            return false;
                    }
                    params[i] = param;
                }
                Object object = ReflectionUtils.execute(delayMethod.getMethod(), delayMethod.getTarget(), params);
                BoneIoc.getInstance().register(definition.getId(), object);
                return true;
            });
            return;
        }
        String className = definition.getClassName();

        Class _classzz;

        Object object;

        try {
            _classzz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("未找到相关class类:" + className);
        }

        BeanConstruction cons = definition.getConstruction();
        if (cons == null) {
            object = ReflectionUtils.newInstance(_classzz);
        } else {
            DefParam[] params = cons.getParams();
            object = ReflectionUtils.newInstance(_classzz, (Object[]) params);
        }

        if (definition.getProperties() != null) {
            for (Property p : definition.getProperties()) {
                if (p.getValue() != null) {
                    Method method = BeanUtils.getSetterMethod(object, p.getName());
                    BeanUtils.setField(object, method, p.getValue());
                }
                if (p.getRef() != null) {
                    Method method = BeanUtils.getSetterMethod(object, p.getName());
                    Object o = getBean(p.getRef());
                    if (o == null) {
                        if (postQueue == null)
                            postQueue = new PostQueue();
                        Object finalObject = object;
                        postQueue.addTask(() -> {
                            Object other = BoneIoc.getInstance().getBean(p.getRef());
                            if (other == null)
                                return false;
                            BeanUtils.setField(finalObject, method, other);
                            return true;
                        });
                    } else {
                        BeanUtils.setField(object, method, o);
                    }

                }
            }
        }
        BoneIoc.getInstance().register(definition.getId(), object);
    }
}
