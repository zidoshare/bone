package site.zido.core.beans;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AnnotationParser extends AbsBeanParser{
    //针对windows/ubuntu不同平台路径显示（不知道有没用）
    private static String SEPARATOR = File.separator;
    @Override
    protected Map<String, Definition> getConfig() {
        return null;
    }

    public List<Class<?>> getClasses(String packageName){
        List<Class<?>> classes = new ArrayList<>();

        boolean recursive = true;
        //将包名转换为路径名
        String packageDirName = packageName.replaceAll(".", SEPARATOR);
        
        try{
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while(resources.hasMoreElements()){
                URL url = resources.nextElement();
                String protocol = url.getProtocol();
                if("file".equals(protocol)){
                    //获取路径
                    String path = URLDecoder.decode(url.getFile(), "UTF-8");

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
                                        //TODO Class.forName()可以实现加载时调用static代码块，等待选取方案
                                        classes.add(Thread.currentThread().getContextClassLoader().loadClass(className));
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
        return null;
    }
}
