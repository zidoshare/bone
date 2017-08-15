package site.zido.utils.commons;

import java.lang.reflect.Method;

public class BeanUtils {
    public static Method getSetterMethod(Object obj,String name){
        name = "set"+name.substring(0,1).toUpperCase()+name.substring(1);
        Method[] methods = obj.getClass().getMethods();
        for (Method m : methods) {
            if(m.getName().equals(name)){
                return m;
            }
        }
        return null;
    }
}
