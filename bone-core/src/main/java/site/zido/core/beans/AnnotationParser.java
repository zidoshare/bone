package site.zido.core.beans;

import site.zido.bone.logger.Logger;
import site.zido.bone.logger.impl.LogManager;
import site.zido.core.beans.handler.AnnotationHandlerManager;
import site.zido.core.beans.handler.EnvResolver;
import site.zido.core.beans.structure.Definition;
import site.zido.core.beans.structure.OnlyMap;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
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
    //针对windows/linux不同平台路径显示（不知道有没用）
    private static String SEPARATOR = File.separator;

    private static Logger logger = LogManager.getLogger(AnnotationParser.class);

    private String packageName = "";

    private Set<Class<?>> classes = new LinkedHashSet<>(128);

    public AnnotationParser(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public void parser() {
        super.parser();
    }

    @Override
    protected Map<String, Definition> getConfig() {
        Set<Class<?>> classes = getClasses();
        Iterator<Class<?>> iter = classes.iterator();
        Map<String, Definition> map = new HashMap<>();
        PostQueue queue = new PostQueue();
        while (iter.hasNext()) {
            Class<?> classzz = iter.next();
            //如果是继承子EnvResolver的，会优先处理， 例如注解处理器,优先注册进来，能够被实例化然后处理其他注解，以实现扩展性
            if (classzz.isAssignableFrom(EnvResolver.class)) {
                OnlyMap<String, Definition> result = AnnotationHandlerManager.getInstance().handle(classzz);
                result.putToMap(map);
            }
            // TODO 其他注解处理
            queue.addTask(() -> true);
        }
        return null;
    }

    private void parserClass(Class<?> classzz) {
        Annotation[] annotations = classzz.getAnnotations();
    }

    public ClassLoader getCurrentClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 查找类
     *
     * @return 类集合
     */
    public Set<Class<?>> findClasses() {
        //每个实例仅能查找一次
        if (!classes.isEmpty())
            return classes;
        //将包名转换为路径名
        String packageDirName = packageName.replace(".", SEPARATOR);
        try {
            Enumeration<URL> resources = getCurrentClassLoader().getResources(packageDirName);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    //获取路径
                    String path = URLDecoder.decode(url.getFile(), "UTF-8");
                    findClasses(packageName, path);
                } else if ("jar".equals(protocol)) {
                    JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
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
                    logger.warn("类 [" + packageName + '.' + className + "] 加载失败");
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
