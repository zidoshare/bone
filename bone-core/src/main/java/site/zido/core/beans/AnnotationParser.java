package site.zido.core.beans;

import site.zido.bone.logger.Logger;
import site.zido.bone.logger.impl.LogManager;
import site.zido.context.EnvResolver;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AnnotationParser extends AbsBeanParser{
    //针对windows/ubuntu不同平台路径显示（不知道有没用）
    private static String SEPARATOR = File.separator;

    private static Logger logger = LogManager.getLogger(AnnotationParser.class);

    private String packageName = "";

    private Set<Class<?>> classes = new LinkedHashSet<>(128);

    public AnnotationParser(String packageName){
        this.packageName = packageName;
    }

    @Override
    public void parser() {

    }

    @Override
    protected Map<String, Definition> getConfig() {
        Set<Class<?>> classes = getClasses();
        Iterator<Class<?>> iter = classes.iterator();
        PostQueue queue = new PostQueue();
        while (iter.hasNext()){
            Class<?> classzz = iter.next();
            //如果是EnvResolver的子类，移交处理器链进行优先处理
            if(classzz.isAssignableFrom(EnvResolver.class)){
                Constructor<?>[] constructors = classzz.getConstructors();
                if(constructors.length > 1){
                    throw new RuntimeException("bone 并不知道你要使用哪个构造函数 : "+classzz.getName());
                }
                Constructor<?> cons = constructors[0];
                Class<?>[] types = cons.getParameterTypes();
                Object[] objs = new Object[types.length];
                int i = 0;
                for (Class<?> type : types) {
                    if(type.isPrimitive()){
                        throw new RuntimeException("构造函数不接受基础类型 ： "+classzz.getName());
                    }
                    Object bean = BoneIoc.getInstance().getBean(type);
                    objs[i++] = bean;
                }
                try {
                    Object o = cons.newInstance(objs);

                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException("实例化出错 : "+classzz.getName());
                }
                
            }
            queue.addTask(()-> true);
        }
        return null;
    }

    private void parserClass(Class<?> classzz){
        Annotation[] annotations = classzz.getAnnotations();
    }

    public ClassLoader getCurrentClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }
    public Set<Class<?>> findClasses(){
        //每个实例仅能查找一次
        if(!classes.isEmpty())
            return classes;
        //将包名转换为路径名
        String packageDirName = packageName.replace(".", SEPARATOR);
        try{
            Enumeration<URL> resources = getCurrentClassLoader().getResources(packageDirName);
            while(resources.hasMoreElements()){
                URL url = resources.nextElement();
                String protocol = url.getProtocol();
                if("file".equals(protocol)){
                    //获取路径
                    String path = URLDecoder.decode(url.getFile(), "UTF-8");
                    findClasses(packageName, path);
                }else if("jar".equals(protocol)){
                    JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()){
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        if(name.startsWith(SEPARATOR)){
                            name = name.substring(1);
                        }
                        if(name.startsWith(packageDirName)){
                            int index = name.lastIndexOf(SEPARATOR);
                            if(index != -1){
                                packageName = name.substring(0,index).replace(SEPARATOR,".");
                            }

                            if(index != -1){
                                if(name.endsWith(".class") && !entry.isDirectory()){
                                    String className = name.substring(packageName.length()+1,name.length()-6);
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

        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName
     * @param packagePath
     */
    private void findClasses(String packageName,
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
                findClasses(packageName + "."
                                + file.getName(), file.getAbsolutePath());
            } else {
                String className = file.getName().substring(0,
                        file.getName().length() - 6);
                try {
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    logger.warn("类 ["+packageName + '.' + className+"] 加载失败");
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
