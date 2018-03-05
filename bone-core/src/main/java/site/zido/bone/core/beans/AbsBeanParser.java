package site.zido.bone.core.beans;

import site.zido.bone.core.beans.structure.DefConstruction;
import site.zido.bone.core.beans.structure.DefProperty;
import site.zido.bone.core.beans.structure.Definition;
import site.zido.bone.core.beans.structure.DelayMethod;
import site.zido.bone.core.exception.beans.FatalBeansException;
import site.zido.bone.core.utils.ReflectionUtils;
import site.zido.bone.core.utils.task.PostQueue;
import site.zido.bone.core.utils.task.PostTask;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * 抽象解析类，扩展解析方式，继承此类，返回config即可。
 *
 * @author zido
 * @since 2017/23/21 下午2:23
 */
public abstract class AbsBeanParser implements IBeanParser {

    private PostQueue postQueue = new PostQueue();

    protected abstract List<Definition> getConfig();

    @Override
    public void parser() {
        List<Definition> config = getConfig();
        if (config != null) {
            for (Definition definition : config) {
                registerBean(definition);
            }
            if (!PostQueue.execute(postQueue)) {
                LinkedList<PostTask> list = postQueue.getList();
                StringBuilder sb = new StringBuilder("Bean实例化异常，请检查依赖是否被注入或包含循环依赖，以下为执行集合：\n");
                for (PostTask postTask : list) {
                    String idStr = "";
                    String beanClass;
                    StringBuilder needStr = new StringBuilder();
                    if (postTask instanceof BeanExecuteTask) {
                        BeanExecuteTask task = (BeanExecuteTask) postTask;
                        Definition definition = task.getDefinition();
                        if (!"".equals(definition.getId())) {
                            idStr = " 实例[id:" + definition.getId() + "] ";
                        }
                        beanClass = " 实例[类:" + definition.getType().getName() + "] ";
                        DefProperty[] properties = task.getProperties();
                        if (properties.length > 0) {
                            needStr.append("需要的bean为：");
                        }
                        for (DefProperty property : properties) {
                            needStr.append("[").append(property.getRef()).append("|").append(property.getType().getName()).append("]");
                        }
                        sb.append(idStr).append(beanClass).append(needStr).append("\n");
                    }
                }
                throw new FatalBeansException(sb.toString());
            }
        }
    }


    ClassLoader getCurrentClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 通过Bean的定义来生成实例
     *
     * @param definition Bean描述
     * @return 实例
     */
    private void registerBean(Definition definition) {
        final List<DelayMethod> delayMethods = definition.getDelayMethods();

        Class<?> type = definition.getType();
        if (type != null) {
            DefConstruction cons = definition.getConstruction();
            if (cons == null) {
                Object object = ReflectionUtils.newInstance(type);
                injectFieldToObject(definition, object);
                //通过构造方法生成的类，需要给类中的延迟执行方法设置目标类
                for (DelayMethod delayMethod : delayMethods) {
                    delayMethod.setTarget(object);
                }
                BoneContext.getInstance().register(definition.getId(), object);
            } else {
                postQueue.addTask(new BeanExecuteTask() {
                    @Override
                    public void run(Object[] params) {
                        Object object = ReflectionUtils.instantiateClass(cons.getConstructor(), params);
                        injectFieldToObject(definition, object);
                        //通过构造方法生成的类，需要给类中的延迟执行方法设置目标类
                        for (DelayMethod delayMethod : delayMethods) {
                            delayMethod.setTarget(object);
                        }
                        BoneContext.getInstance().register(definition.getId(), object);
                    }
                }.need(cons.getProperties()).produce(definition));
            }
        }

        if (delayMethods.size() > 0) {
            for (final DelayMethod delayMethod : delayMethods) {
                postQueue.addTask(new BeanExecuteTask() {
                    @Override
                    public void run(Object[] params) {
                        Object target = delayMethod.getTarget();
                        if (target != null) {
                            Object object = delayMethod.execute(params);
                            BoneContext.getInstance().register(definition.getId(), object);
                        } else {
                            postQueue.addTask(new ExtraBeanExecuteTask() {
                                @Override
                                public boolean check() {
                                    return null != delayMethod.getTarget();
                                }

                                @Override
                                public void run(Object[] params) {
                                    Object object = delayMethod.execute(params);
                                    BoneContext.getInstance().register(definition.getId(), object);
                                }
                            }.need(this).produce(definition));
                        }
                    }
                }.need(delayMethod.getProperties()).produce(definition));
            }
        }
    }

    private void injectFieldToObject(Definition definition, Object object) {
        if (definition.getProperties() != null) {
            for (DefProperty p : definition.getProperties()) {
                Method method = ReflectionUtils.getSetterMethod(object, p.getName());
                if (p.getValue() == null) {
                    postQueue.addTask(new BeanExecuteTask() {
                        @Override
                        public void run(Object[] params) {
                            ReflectionUtils.setField(object, method, params[0]);
                        }
                    }.need(new DefProperty[]{p}));
                } else {
                    ReflectionUtils.setField(object, method, p.getValue());
                }
            }
        }
    }
}
