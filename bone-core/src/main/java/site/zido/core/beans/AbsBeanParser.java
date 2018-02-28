package site.zido.core.beans;

import site.zido.bone.logger.Logger;
import site.zido.bone.logger.impl.LogManager;
import site.zido.core.beans.structure.*;
import site.zido.core.utils.ReflectionUtils;
import site.zido.utils.commons.BeanUtils;
import site.zido.utils.commons.ValiDateUtils;

import java.lang.reflect.Method;
import java.util.List;
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
        return BoneContext.getInstance().getBean(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> requireType) {
        return BoneContext.getInstance().getBean(name, requireType);
    }

    @Override
    public <T> T getBean(Class<T> requireType) {
        return BoneContext.getInstance().getBean(requireType);
    }

    protected abstract List<Definition> getConfig();

    @Override
    public void parser() {
        List<Definition> config = getConfig();
        if (config != null) {
            for (Definition definition : config) {
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
        final List<DelayMethod> delayMethods = definition.getDelayMethods();
        if (delayMethods.size() > 0) {
            if (postQueue == null)
                postQueue = new PostQueue();
            for (final DelayMethod delayMethod : delayMethods) {
                postQueue.addTask(() -> {
                    String[] paramNames = delayMethod.getParamNames();
                    Class<?>[] paramTypes = delayMethod.getParamTypes();

                    Object[] params = null;
                    if (paramTypes != null && paramTypes.length > 0) {
                        params = new Object[paramTypes.length];
                        for (int i = 0; i < paramTypes.length; i++) {
                            Object param;
                            if (ValiDateUtils.isEmpty(paramNames[i])) {
                                param = BoneContext.getInstance().getBean(paramTypes[i]);
                            } else {
                                param = BoneContext.getInstance().getBean(paramNames[i]);
                            }
                            if (param == null)
                                return false;
                            params[i] = param;
                        }
                    }
                    Object target = delayMethod.getTarget();
                    if (target != null) {
                        Object object = ReflectionUtils.execute(delayMethod.getMethod(), target, params);
                        BoneContext.getInstance().register(definition.getId(), object);
                    } else {
                        Object[] finalParams = params;
                        postQueue.addTask(() -> {
                            Object t = delayMethod.getTarget();
                            if (t == null)
                                return false;
                            Object object = ReflectionUtils.execute(delayMethod.getMethod(), t, finalParams);
                            BoneContext.getInstance().register(definition.getId(), object);
                            return true;
                        });
                    }
                    return true;
                });
            }
            return;
        }
        String className = definition.getClassName();

        Class _classzz;

        try {
            _classzz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("未找到相关class类:" + className);
        }

        BeanConstruction cons = definition.getConstruction();
        if (cons == null) {
            Object object = ReflectionUtils.newInstance(_classzz);
            for (DelayMethod delayMethod : delayMethods) {
                delayMethod.setTarget(object);
            }
            injectFieldToObject(definition);
            BoneContext.getInstance().register(definition.getId(), object);
        } else {
            postQueue.addTask(() -> {
                List<DefParam> defParams = cons.getParams();
                Object[] params = new Object[defParams.size()];
                for (int i = 0; i < defParams.size(); i++) {
                    DefParam defParam = defParams.get(i);
                    if (!ValiDateUtils.isEmpty(defParam.getValue())) {
                        params[i] = defParam.getValue();
                    } else if (!ValiDateUtils.isEmpty(defParam.getRef())) {
                        params[i] = BoneContext.getInstance().getBean(defParam.getRef());
                    } else if (defParam.getType() != null) {
                        params[i] = BoneContext.getInstance().getBean(defParam.getType());
                    } else {
                        throw new RuntimeException("构造参数解析错误");
                    }
                    if (params[i] == null) {
                        return false;
                    }
                }
                Object object = ReflectionUtils.newInstance(_classzz, params);
                for (DelayMethod delayMethod : delayMethods) {
                    delayMethod.setTarget(object);
                }
                injectFieldToObject(definition);
                BoneContext.getInstance().register(definition.getId(), object);
                return true;
            });
        }


    }

    private void injectFieldToObject(Definition definition) {
        Object object = definition.getObject();
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
                        postQueue.addTask(() -> {
                            Object other = BoneContext.getInstance().getBean(p.getRef());
                            if (other == null)
                                return false;
                            BeanUtils.setField(object, method, other);
                            return true;
                        });
                    } else {
                        BeanUtils.setField(object, method, o);
                    }

                }
            }
        }
    }
}
