package site.zido.core.beans.handler;

import site.zido.bone.logger.Logger;
import site.zido.bone.logger.impl.LogManager;
import site.zido.core.beans.BeanHaveOneStrucureException;
import site.zido.core.beans.BoneIoc;
import site.zido.core.beans.annotation.Param;
import site.zido.core.beans.structure.BeanConstruction;
import site.zido.core.beans.structure.DefParam;
import site.zido.core.beans.structure.Definition;
import site.zido.core.beans.structure.OnlyMap;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

/**
 * 默认处理器 （未完成）
 * <P>处理Bean相关注解</P>
 *
 * @author zido
 * @since 2017/17/21 下午2:17
 */
public class DefaultHandler implements IHandler {
    private static Logger logger = LogManager.getLogger(DefaultHandler.class);
    @Override
    public OnlyMap<String, Definition> handle(Class<?> classzz) {
        //获取构造器
        Constructor<?>[] constructors = classzz.getConstructors();
        if(constructors.length > 1){
            throw new BeanHaveOneStrucureException();
        }
        Constructor<?> cons = constructors[0];

        //获取构造器的参数
        Parameter[] params = cons.getParameters();
        Object[] objs = new Object[params.length];

        //开始构造[实例描述]

        //[构造方法描述]
        BeanConstruction beanConstruction = new BeanConstruction();
        int i = 0;
        //遍历参数，构造[参数描述]
        for (Parameter param : params) {
            try {
                beanConstruction.addParam(getValueFromAnnotation(param));
            } catch (ClassNotFoundException e) {
                logger.warn("注入类型错误:["+classzz.getName()+"]");
                objs[i++] = BoneIoc.getInstance().getBean(param.getAnnotation(Param.class).ref());
            }
        }
        Definition def = new Definition();
        def.setConstruction(beanConstruction);
//        def.setId();
        return new OnlyMap<>();
    }

    private DefParam getValueFromAnnotation(Parameter param) throws ClassNotFoundException {
        Param annotation = param.getAnnotation(Param.class);
        //如果是基本类型，查看注解上的value
        if(param.getType().isPrimitive()){
            if(annotation == null){

            }
            String value = annotation.value();
            return new DefParam(null,value,null);
        }else{
            if(annotation == null)
                return new DefParam(param.getType(),null,null);
            else {
                return new DefParam(
                        Thread.currentThread().getContextClassLoader().loadClass(annotation.type()),
                        null,annotation.ref());
            }
        }
    }
}
