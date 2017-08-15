package site.zido.utils.commons;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanUtils {
    public static Method getSetterMethod(Object obj, String name) {
        name = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
        Method[] methods = obj.getClass().getMethods();
        for (Method m : methods) {
            if (m.getName().equals(name)) {
                return m;
            }
        }
        return null;
    }

    public static void setField(Object target, Method method, Object... value) {
        if (method == null) {
            throw new RuntimeException("没有相应的setter方法:" + target.getClass().getName() + "." + method.getName());
        }
        Class<?>[] types = method.getParameterTypes();
        if (types.length != value.length) {
            throw new IllegalArgumentException("参数不匹配：" + target.getClass().getName() + "." + method.getName() + " 不能匹配 " + value);
        }
        Object args[] = new Object[types.length];
        int i = 0;
        for (Class<?> type : types) {

            switch (type.getSimpleName()) {
                case "Integer":
                    args[i] = Integer.valueOf(value[i++].toString());
                    break;
                case "Double":
                    args[i] = Double.valueOf(value[i++].toString());
                    break;
                case "Float":
                    args[i] = Double.valueOf(value[i++].toString());
                    break;
                case "Boolean":
                    args[i] = Boolean.valueOf(value[i++].toString());
                    break;
                case "Date":
                    args[i] = DateUtils.parseDate(value[i++].toString());
                    break;
                case "String":
                    args[i] = value[i++].toString();
                    break;
                default:
                    args[i] = value[i++];
            }
        }
        try {
            method.invoke(target, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("属性名称不合法或者没有相应的setter方法：" + target.getClass().getName() + "." + method.getName());
        }
    }
}
