package site.zido.bone.core.beans;

import site.zido.bone.core.beans.structure.DefConstruction;
import site.zido.bone.core.beans.structure.DefProperty;
import site.zido.bone.core.beans.structure.Definition;
import site.zido.bone.core.beans.structure.DelayMethod;
import site.zido.bone.core.utils.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 抽象解析类，扩展解析方式，继承此类，返回config即可。
 *
 * @author zido
 * @since 2017/23/21 下午2:23
 */
public abstract class AbsBeanParser implements IBeanParser {

    private PostGraph postGraph = new PostGraph();

    protected abstract List<Definition> getConfig();

    @Override
    public void parser() {
        List<Definition> config = getConfig();
        if (config != null) {
            for (Definition definition : config) {
                registerBean(definition);
            }
            postGraph.execute();
        }
    }


    ClassLoader getCurrentClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 通过Bean的定义来生成实例
     *
     * @param definition Bean描述
     */
    private void registerBean(Definition definition) {
        final List<DelayMethod> delayMethods = definition.getDelayMethods();
        DefProperty[] properties = definition.getProperties();
        if (definition.isClass()) {
            Class<?> type = definition.getType();
            DefConstruction cons = definition.getConstruction();
            PostGraph.PostNeed next;
            if (cons == null) {
                next = postGraph.addTask(new PostTask() {
                    @Override
                    public void execute(Object[] params) {
                        Object object = ReflectionUtils.newInstance(type);
                        for (DefProperty property : properties) {
                            property.setTarget(object);
                        }
                        //通过构造方法生成的类，需要给类中的延迟执行方法设置目标类
                        for (DelayMethod delayMethod : delayMethods) {
                            delayMethod.setTarget(object);
                        }
                        BoneContext.getInstance().register(definition.getId(), object);
                    }
                }).need(properties).produce(definition);
            } else {
                next = postGraph.addTask(new PostTask() {
                    @Override
                    public void execute(Object[] params) {
                        Object object = ReflectionUtils.instantiateClass(cons.getConstructor(), params);
                        for (DefProperty property : properties) {
                            property.setTarget(object);
                        }
                        //通过构造方法生成的类，需要给类中的延迟执行方法设置目标类
                        for (DelayMethod delayMethod : delayMethods) {
                            delayMethod.setTarget(object);
                        }
                        BoneContext.getInstance().register(definition.getId(), object);
                    }
                }).need(cons.getProperties()).produce(definition);
            }
            if (properties != null) {
                for (DefProperty p : properties) {
                    if (p.getValue() == null) {
                        next.addChild(new PostTask() {
                            @Override
                            public void execute(Object[] params) {
                                Object object = p.getTarget();
                                Method method = ReflectionUtils.getSetterMethod(object, p.getName());
                                ReflectionUtils.setField(object, method, params[0]);
                            }
                        }).need(new DefProperty[]{p}).produce(p);
                    } else {
                        next.addChild(new PostTask() {
                            @Override
                            public void execute(Object[] params) {
                                Object object = p.getTarget();
                                Method method = ReflectionUtils.getSetterMethod(object, p.getName());
                                ReflectionUtils.setField(object, method, p.getValue());
                            }
                        }).produce(p);
                    }
                }
            }
            if (delayMethods.size() > 0) {
                for (final DelayMethod delayMethod : delayMethods) {
                    next.addChild(new PostTask() {
                        @Override
                        public void execute(Object[] params) {
                            Object object = delayMethod.execute(params);
                            BoneContext.getInstance().register(definition.getId(), object);
                        }
                    }).need(delayMethod.getProperties()).produce(delayMethod);
                }
            }
        }else {
            for (final DelayMethod delayMethod : delayMethods) {
                postGraph.addTask(new PostTask() {
                    @Override
                    public void execute(Object[] params) {
                        Object object = delayMethod.execute(params);
                        BoneContext.getInstance().register(definition.getId(), object);
                    }
                }).need(delayMethod.getProperties()).produce(definition);
            }
        }
    }
}
