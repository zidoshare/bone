# 写在前面

最近想要实现一个java web框架。但是一直有一些细节难以敲定和想通，当然，也碍于个人技术有限。

我对于学习一直是觉得实践才是硬道理，我一直没有去深入的看spring的源码，是因为我觉得自己还没有实现过，很难体会那种框架的核心痛点，也就没有那种茅塞顿开的感觉，我喜欢这种感觉。

所以我想先实现一个框架，了解其中痛点，然后再深入学习spring源码，来看看大神如何解决这些问题的。

我的框架命名为[bone](https://github.com/zidoshare/bone)，我希望它不一定是多么的成熟，但是我希望它一定是小巧的，能够支撑起整个身体的框架。

对于功能的展望，我还是希望它麻雀虽小，五脏俱全，具备ioc、di、aop、mvc、orm等功能。在实现中，很小部分参考了[latke](https://github.com/b3log/latke)（日志）和spring的代码（异常）。

本篇将介绍自己对于ioc和di的一些思考，具体代码是对D大翻译的[jsr-330](http://blog.csdn.net/dl88250/article/details/4838803)规范的实现。

# 对与ioc与di的构思

对于ioc和di，我觉得主要考虑的问题有：

* 依赖解析
* 依赖缺失
* 循环依赖

对于这几点，依赖的解析还好说，我们可以对每个bean进行透彻的分析。为每个需要注入的构造方法、普通方法、属性进行分析，通过反射获取所需要的属性。
重要的是，如何解决循环依赖和依赖缺失的问题？

在最开始，我第一次实现ioc和di的时候，根本没有好的方法，所以我想到了一种解决方案。我把所有的需要执行的实例化/构造方法/普通方法/属性。看做时一个任务（Task）。

然后把这些任务统统一股脑放在队列中，然后对队列执行出队操作(pop)。然后新建一个失败队列，如果执行失败，也就是说依赖在容器中找不到，那么进入（push）失败队列。
然后待执行任务队列为空时，把失败队列做为新的任务队列，继续循环。

这样不停的去循环执行，如果某一次，任务队列的长度和失败队列的长度相等，那么代表所有的待执行任务都执行失败了，那么肯定是依赖缺失或者循环依赖了。

然后我按照这种思路去做了第一个版本的ioc。做完后发现，这个想法简直是太蠢了，报错根本不精准，也不知道是哪个任务因为什么原因失败了，是依赖缺失？还是循环依赖。另外还有问题是，
有时候task执行会再生成新的task，这种情况也根本检测不出来。

所以我开始寻找其他的解决方案。

在某一次突然的想法中，徒然发现，这些bean和依赖关系不正好能够对应上数据结构中的有向图(arc graph)结构吗？
然后我完全可以通过有向图的检测环的算法来检测是否有循环依赖，另外可以通过检测每个任务所需的bean和这个任务所对应的顶点（node）数量是否想等不就能够判定依赖缺失的问题了吗？
所以，我依照这种想法，开始实现自己的ioc和di（我也不知道spring/latke是否是这样实现的，任性～）。


# 注入实现

对每个需要生成的bean，我都会生成一个描述类(Definition)。它会存储每个bean的具体构建方式、依赖、以及可能执行的方法、需要被注入的属性等等。
这几个都分散在类 DefConstruction(描述构造器),DefProperty(描述属性)，DelayMethod(描述方法)。通过这些类。构造出一个一个的描述。然后解析这些描述，就能够生成图结构，最后进入我们的分析执行阶段啦。

对与容器注入，为了实现class扫描和xml以及后来可能会有的其他注入方式，我抽象除了AbsBeanParser类。它能够通过子类获取的Definitions生成图结构，并执行注入。
AnnotationParser是注解注入bean的扫描器，而XmlParser是xml注入bean的扫描器。xml目前对于bean的注入还是只是支持比较简单的语法。
主要是AnnotationParser注解扫描器。会尽可能的对每个bean的构造器，属性，方法去分析，以生成合适的Definition。从而实现类似Spring bean注入的体验(当然还是差了很大一截)。

我对每个需要扫描的类，大概分为两种大类型，一种是直接直接方法生成bean的，这种指需要代理执行方法，并向方法中提供相关参数即可。
一种是Component类，这种是类的实例化交给ioc的，这种类会尽量根据构造函数进行实例化。

具体代码如下，比较混乱，也贴上，希望能有帮助吧

```java
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

```

然后获取到Definition集合之后遍历每一个描述，找到其中每个需要执行的任务并实例化，然后注入到图结构中，建立每个bean的依赖关系。

通过Definition构造图的代码如下：


```java
/**
 * 抽象解析类，扩展解析方式，继承此类，返回config即可。
 *
 * @author zido
 * @since 2017/23/21 下午2:23
 */
public abstract class AbsBeanParser implements IBeanParser 
/**
     * 通过Bean的定义来生成实例
     *
     * @param definition Bean描述
     */
    private void registerBean(Definition definition) {
        final List<DelayMethod> delayMethods = definition.getDelayMethods();

        if (definition.isClass()) {
            Class<?> type = definition.getType();
            DefConstruction cons = definition.getConstruction();
            if (cons == null) {
                postGraph.addChild(new PostTask() {
                    @Override
                    public void execute(Object[] params) {
                        Object object = ReflectionUtils.newInstance(type);
                        injectFieldToObject(definition, object);
                        //通过构造方法生成的类，需要给类中的延迟执行方法设置目标类
                        for (DelayMethod delayMethod : delayMethods) {
                            delayMethod.setTarget(object);
                        }
                        BoneContext.getInstance().register(definition.getId(), object);
                    }
                }).need(definition.getProperties()).produce(definition);
            } else {
                DefProperty[] arr1 = definition.getProperties();
                DefProperty[] arr2 = cons.getProperties();
                DefProperty[] properties = new DefProperty[arr1.length + arr2.length];
                for (int i = 0; i < properties.length; i++) {
                    if (i >= arr1.length) {
                        properties[i] = arr2[i - arr2.length];
                    } else {
                        properties[i] = arr1[i];
                    }
                }
                postGraph.addChild(new PostTask() {
                    @Override
                    public void execute(Object[] params) {
                        Object object = ReflectionUtils.instantiateClass(cons.getConstructor(), params);
                        injectFieldToObject(definition, object);
                        //通过构造方法生成的类，需要给类中的延迟执行方法设置目标类
                        for (DelayMethod delayMethod : delayMethods) {
                            delayMethod.setTarget(object);
                        }
                        BoneContext.getInstance().register(definition.getId(), object);
                    }
                }).need(properties).produce(definition);
            }
        }

        if (delayMethods.size() > 0) {
            for (final DelayMethod delayMethod : delayMethods) {
                PostGraph.PostProduce need = postGraph.addChild(new PostTask() {
                    @Override
                    public void execute(Object[] params) {
                        Object target = delayMethod.getTarget();
                        if (target != null) {
                            Object object = delayMethod.execute(params);
                            BoneContext.getInstance().register(definition.getId(), object);
                        } else {
                            postGraph.addChild(new ExtraBeanExecuteTask() {
                                @Override
                                public boolean check() {
                                    return null != delayMethod.getTarget();
                                }

                                @Override
                                public void execute(Object[] params) {
                                    Object object = delayMethod.execute(params);
                                    BoneContext.getInstance().register(definition.getId(), object);
                                }
                            }).need(this.getProperties()).produce(definition);
                        }
                    }
                }).need(delayMethod.getProperties());
                if (definition.isClass()) {
                    need.produce(delayMethod);
                } else {
                    need.produce(definition);
                }
            }
        }
    }

    private void injectFieldToObject(Definition definition, Object object) {
        if (definition.getProperties() != null) {
            for (DefProperty p : definition.getProperties()) {
                Method method = ReflectionUtils.getSetterMethod(object, p.getName());
                if (p.getValue() == null) {
                    postGraph.addChild(new PostTask() {
                        @Override
                        public void execute(Object[] params) {
                            ReflectionUtils.setField(object, method, params[0]);
                        }
                    }).need(new DefProperty[]{p});
                } else {
                    ReflectionUtils.setField(object, method, p.getValue());
                }
            }
        }
    }
}
```

# 最后时关于有向图的构建

出于本身这个项目也是个人练习所用，所以我在某些问题上是真的纠结了很久，对与图的实现，都采用了非常复杂的继承关系。
想要高度抽象，结果个人技术不精，没能完全的抽象好，不过也还是能看。
![PostGraph.java继承关系图](http://odp22tnw6.bkt.clouddn.com/blog/content/graph-st.png)。
其中 Graph类是整个图结构的接口，ArcGraph是有向图抽象类，OrthogonalArcGraph是有向图的十字链表实现。而PostGraph是扩展自有向图，用来容纳整个任务执行的图结构。

为什么选择十字链表实现，是因为十字链表是图结构存储的非常好的一种方式，它将邻接表和逆邻接表结合起来，使得每个顶点都能访问它所有对应的弧，以及指向它的顶点和它指向的顶点，而不需要重新循环，
这对于我们依赖图构建有非常大的帮助。

## 简单介绍图在十字链表的实现（具体请查看专业博文）：

图由顶点和弧组成。在 A->b这个图中，A、b为顶点，而这个箭头如何表示呢？它用弧头和弧尾组成，A的这一边为弧头，b的这一边为弧尾。
每个顶点类存储四个数据（所有顶点类需要单独维护，能够通过标示访问即可），分别为当前顶点标示(index)，具体数据(data),firstIn(第一个指向此顶点的弧)，firstOut(此顶点第一个出去的弧)。
而弧类也存储四个数据，分别为tailVex(弧尾所在的点标识),headVex(弧头所在的点标识),headLink(弧头指向想通顶点的弧),tailLink(弧尾指向相同顶点的弧)。

具体看下图。
![](http://odp22tnw6.bkt.clouddn.com/blog/content/graph.jpg)。

接下来就是靠代码去维护这一段关系，其实主要是表现在添加弧的时候去维护这个关系，具体代码：
```java
    public class OrthogonalArcGraph<T>{
        /**
         * 添加弧
         *
         * @param tail 弧尾编号
         * @param head 弧头编号
         */
        @Override
        public void connect(int tail, int head) {
            OrthogonalNode<T> tailNode = getNode(tail);
            if (tailNode == null) {
                throw new NoSuchNodeException(tail);
            }
            OrthogonalNode<T> headNode = getNode(head);
            if (headNode == null) {
                throw new NoSuchNodeException(head);
            }
        
            if (get(tail, head) != null) {
                return;
            }
        
            OrthogonalArc arc = new OrthogonalArc(tail, head);
        
            //处理弧头顶点
            //替代节点，完成替代
            arc.setTailLink(tailNode.getFirstOut());
            tailNode.setFirstOut(arc);
        
            //处理弧尾顶点
            arc.setHeadLink(headNode.getFirstIn());
            headNode.setFirstIn(arc);
        }
    }
```

只是简单讲解，其他请查看相关博文介绍，非常清楚。

# 写在最后

ioc/di的实现还有很多的缺陷，但是实现了基本功能，剩下的只是细节处理了，将暂时放下，去实现接下来aop和mvc模块，也是非常难的模块。
不过还好我时间还算充裕，我将努力去实现它，如果你对我的实现感兴趣，不妨给个star，项目地址在这里[bone](https://github.com/zidoshare/bone)。
希望能有大佬多提意见，也希望有志同道合的朋友和我一起讨论开发，谢谢！