package site.zido.bone.core.beans;

import site.zido.bone.core.beans.annotation.Bean;
import site.zido.bone.core.beans.annotation.Beans;
import site.zido.bone.core.beans.annotation.Component;
import site.zido.bone.core.beans.structure.DefConstruction;
import site.zido.bone.core.beans.structure.DefProperty;
import site.zido.bone.core.beans.structure.Definition;
import site.zido.bone.core.beans.structure.DelayMethod;
import site.zido.bone.core.utils.ReflectionUtils;
import site.zido.bone.logger.Logger;
import site.zido.bone.logger.impl.LogManager;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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
    protected List<Definition> getConfig() {
        Set<Class<?>> classes = loadClasses();
        Iterator<Class<?>> iter = classes.iterator();
        List<Definition> list = new ArrayList<>();
        while (iter.hasNext()) {
            Class<?> classzz = iter.next();
            if (classzz.isAnnotationPresent(Beans.class)) {
                parseClass(classzz, list);
            } else {
                Component annotation = ReflectionUtils.getAnnotation(classzz, Component.class);
                if (annotation != null) {
                    String id = annotation.id();
                    Definition definition = parseComponent(classzz, id);
                    if (definition != null) {
                        list.add(definition);
                    }
                }
            }
        }
        return list;
    }

    private Definition parseComponent(Class<?> classzz, String id) {
        Definition definition = new Definition();
        definition.setId(id);
        definition.setType(classzz);
        definition.isClass(true);
        //解析构造器
        Constructor<?> constructor = ReflectionUtils.getOnlyConstructor(classzz);
        DefConstruction defConstruction = new DefConstruction(constructor);

        if (constructor.getParameterCount() > 0) {
            Parameter[] parameters = constructor.getParameters();
            DefProperty[] properties = new DefProperty[parameters.length];
            int i = 0;
            for (Parameter parameter : parameters) {
                DefProperty defProperty = new DefProperty();
                defProperty.setType(parameter.getType());
                defProperty.setName(parameter.getName());
                Inject annotation = parameter.getAnnotation(Inject.class);
                if (annotation != null) {
                    Named named = parameter.getAnnotation(Named.class);
                    if (named != null) {
                        String name = named.value();
                        defProperty.setRef(name);
                    }
                }
                properties[i++] = defProperty;
            }
            defConstruction.setProperties(properties);
        }

        definition.setConstruction(defConstruction);

        //解析所有需要被注入的方法
        Method[] methods = classzz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getAnnotation(Inject.class) == null)
                continue;
            DelayMethod delayMethod = parseMethod(method);
            definition.addDelayMethod(delayMethod);
        }
        //解析所有需要被注入的属性
        Field[] fields = classzz.getDeclaredFields();
        ArrayList<DefProperty> properties = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Inject.class)) {
                DefProperty defProperty = new DefProperty();
                Named named = field.getAnnotation(Named.class);
                if (named != null) {
                    String name = named.value();
                    defProperty.setRef(name);
                }
                defProperty.setName(field.getName());
                defProperty.setType(field.getType());
                properties.add(defProperty);
            }
        }
        DefProperty[] defProperties = new DefProperty[properties.size()];
        definition.setProperties(properties.toArray(defProperties));
        return definition;
    }

    private DelayMethod parseMethod(Method method) {
        DelayMethod delayMethod = new DelayMethod();
        delayMethod.setMethod(method);
        if (method.getParameterCount() > 0) {
            Class<?>[] paramTypes = method.getParameterTypes();

            Parameter[] parameters = method.getParameters();

            DefProperty[] properties = new DefProperty[paramTypes.length];
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];

                DefProperty property = new DefProperty();

                property.setType(parameter.getType());
                property.setName(parameter.getName());
                Named annotation = parameter.getAnnotation(Named.class);
                if (annotation != null) {
                    property.setRef(annotation.value());
                } else {
                    property.setRef("");
                }

                properties[i] = property;
            }

            delayMethod.setProperties(properties);
        }
        return delayMethod;
    }

    private void parseClass(Class<?> classzz, List<Definition> list) {
        Object obj = ReflectionUtils.newInstance(classzz);
        if (obj == null) {
            return;
        }
        Method[] methods = classzz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Bean.class)) {
                Definition definition = new Definition();
                definition.isClass(false);
                Bean annotation = method.getAnnotation(Bean.class);
                String id = annotation.id();
                Class<?> returnType = method.getReturnType();
                if (returnType == null) {
                    continue;
                }
                DelayMethod delayMethod = parseMethod(method);
                delayMethod.setTarget(obj);
                definition.addDelayMethod(delayMethod);
                definition.setId(id);
                definition.setType(method.getReturnType());
                list.add(definition);
            }
        }
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
