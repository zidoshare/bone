package site.zido.core.beans.handler;

import site.zido.bone.logger.Logger;
import site.zido.bone.logger.impl.LogManager;
import site.zido.core.beans.BeanHaveOneStrucureException;
import site.zido.core.beans.BoneIoc;
import site.zido.core.beans.annotation.Param;
import site.zido.core.beans.structure.Definition;
import site.zido.core.beans.structure.OnlyMap;
import site.zido.utils.commons.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;

public class DefaultHandler extends AbsHandler {
    private static Logger logger = LogManager.getLogger(DefaultHandler.class);
    @Override
    public OnlyMap<String, Definition> handle(Class<?> classzz) {
        //TODO 默认处理器
        Constructor<?>[] constructors = classzz.getConstructors();
        if(constructors.length > 1){
            throw new BeanHaveOneStrucureException();
        }
        Constructor<?> cons = constructors[0];
        Parameter[] params = cons.getParameters();
        Object[] objs = new Object[params.length];
        int i = 0;
        for (Parameter param : params) {
            Param annotation = param.getAnnotation(Param.class);
            if(param.getType().isPrimitive()){
                String value = annotation.value();
                objs[i++] = StringUtils.toOtherType(param.getType(),value);
            }else{
                if(annotation == null)
                    objs[i++] = BoneIoc.getInstance().getBean(param.getType());
                else {
                    try {
                        objs[i++] = BoneIoc.getInstance().getBean(annotation.ref(),Thread.currentThread().getContextClassLoader().loadClass(annotation.type()));
                    } catch (ClassNotFoundException e) {
                        logger.warn("注入类型错误:["+classzz.getName()+"]");
                        objs[i++] = BoneIoc.getInstance().getBean(annotation.ref());
                    }
                }
            }
        }
        try {
            Object o = cons.newInstance(objs);

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("实例化出错 : "+classzz.getName());
        }

        return null;
    }
}
