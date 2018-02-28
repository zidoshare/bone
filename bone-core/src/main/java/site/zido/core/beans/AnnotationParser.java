package site.zido.core.beans;

import site.zido.bone.logger.Logger;
import site.zido.bone.logger.impl.LogManager;
import site.zido.core.beans.annotation.Bean;
import site.zido.core.beans.annotation.Beans;
import site.zido.core.beans.annotation.Component;
import site.zido.core.beans.structure.Definition;
import site.zido.core.beans.structure.DelayMethod;
import site.zido.core.utils.ReflectionUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 注解解析器
 *
 * @author zido
 * @since 2017/25/21 下午2:25
 */
public class AnnotationParser extends AbsBeanParser {
    private static String SEPARATOR = File.separator;

    private static Logger logger = LogManager.getLogger(AnnotationParser.class);

    private String packageName;

    private Set<Class<?>> classes = new LinkedHashSet<>(128);

    public AnnotationParser(String packageName) {
        this.packageName = packageName;
    }

    @Override
    protected Map<String, Definition> getConfig() {
        Set<Class<?>> classes = loadClasses();
        Iterator<Class<?>> iter = classes.iterator();
        Map<String, Definition> map = new HashMap<>();
        while (iter.hasNext()) {
            Class<?> classzz = iter.next();
            if (classzz.isAnnotationPresent(Beans.class)) {
                parseClass(classzz, map);
            }else{
                Component annotation = classzz.getAnnotation(Component.class);
                if(annotation == null){
                    Annotation[] annotations = classzz.getAnnotations();
                    for (Annotation annotation1 : annotations) {

                    }
                }
            }
        }
        return map;
    }

    private void parseClass(Class<?> classzz, Map<String, Definition> map) {
        Object obj = ReflectionUtils.newInstance(classzz);
        if (obj == null) {
            return;
        }
        Method[] methods = classzz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Bean.class)) {
                Definition definition = new Definition();
                Bean annotation = method.getAnnotation(Bean.class);
                String id = annotation.id();
                if ("".equals(id)) {
                    Class<?> returnType = method.getReturnType();
                    if (returnType == null) {
                        continue;
                    }
                    id = ReflectionUtils.getSimpleName(returnType);
                }
                if (method.getParameterCount() > 0) {
                    DelayMethod delayMethod = new DelayMethod();
                    delayMethod.setMethod(method);
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    Class<?>[] paramTypes = new Class<?>[parameterTypes.length];
                    String[] paramNames = new String[parameterTypes.length];
                    for (int i = 0; i < parameterTypes.length; i++) {
                        paramTypes[i] = parameterTypes[i];
                        paramNames[i] = ReflectionUtils.getSimpleName(parameterTypes[i]);
                    }
                    delayMethod.setParamTypes(paramTypes);
                    delayMethod.setTarget(obj);
                    delayMethod.setParamNames(paramNames);
                    definition.setDelayMethod(delayMethod);
                    definition.setId(id);
                    map.put(id, definition);
                } else {
                    try {
                        Object o = method.invoke(obj);
                        definition.setId(id);
                        definition.setObject(o);
                        map.put(id, definition);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private ClassLoader getCurrentClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 查找类
     *
     * @return 类集合
     */
    public Set<Class<?>> loadClasses() {
        //每个实例仅能查找一次
        if (!classes.isEmpty())
            return classes;
        //将包名转换为路径名
        String packageDirName = packageName.replace(".", SEPARATOR);
        Enumeration<URL> resources;
        try {
            resources = getCurrentClassLoader().getResources(packageDirName);
        } catch (IOException e) {
            throw new RuntimeException(packageName + "包加载异常");
        }
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            String protocol = url.getProtocol();
            if ("file".equals(protocol)) {
                //获取路径
                String path;
                try {
                    path = URLDecoder.decode(url.getFile(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    logger.error(url + "加载异常,编码不支持,需要UTF-8编码", e);
                    continue;
                }
                loadClasses(packageName, path);
            } else if ("jar".equals(protocol)) {
                JarFile jar;
                try {
                    jar = ((JarURLConnection) url.openConnection()).getJarFile();
                } catch (IOException e) {
                    logger.error("jar包：" + url + "加载异常", e);
                    continue;
                }
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    if (name.startsWith(SEPARATOR)) {
                        name = name.substring(1);
                    }
                    if (name.startsWith(packageDirName)) {
                        int index = name.lastIndexOf(SEPARATOR);
                        if (index != -1) {
                            packageName = name.substring(0, index).replace(SEPARATOR, ".");
                        }

                        if (index != -1) {
                            if (name.endsWith(".class") && !entry.isDirectory()) {
                                String className = name.substring(packageName.length() + 1, name.length() - 6);
                                try {
                                    classes.add(getCurrentClassLoader().loadClass(className));
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
        return classes;
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName 包名
     * @param packagePath 路径
     */
    private void loadClasses(String packageName,
                             String packagePath) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        //查找class
        File[] dirFiles = dir.listFiles(file -> (file.isDirectory())
                || (file.getName().endsWith(".class")));
        assert dirFiles != null;
        for (File file : dirFiles) {
            if (file.isDirectory()) {
                loadClasses(packageName + "."
                        + file.getName(), file.getAbsolutePath());
            } else {
                String className = file.getName().substring(0,
                        file.getName().length() - 6);
                try {
                    classes.add(getCurrentClassLoader().loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    logger.error("类 [" + packageName + '.' + className + "] 加载失败", e);
                }
            }
        }
    }

    public Set<Class<?>> getClasses() {
        return classes;
    }

    public void setClasses(Set<Class<?>> classes) {
        this.classes = classes;
    }
}
