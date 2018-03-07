package site.zido.bone.core.beans;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import site.zido.bone.core.beans.structure.DefProperty;
import site.zido.bone.core.beans.structure.Definition;
import site.zido.bone.core.exception.beans.BadClassNameException;
import site.zido.bone.core.utils.Assert;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * xml文件解析器
 *
 * @author zido
 * @since 2017/5/28 0028
 */
public class XmlParser extends AbsBeanParser {

    private String path;

    public XmlParser(String path) {
        this.path = path;
    }

    @Override
    protected List<Definition> getConfig() {
        return getConfig(path);
    }

    private List<Definition> getConfig(String path) {
        Assert.hasLength(path, "xml扫描路径不能为空");
        List<Definition> configMap = new ArrayList<>(5);
        Document doc;

        SAXReader reader = new SAXReader();
        InputStream in = XmlParser.class.getResourceAsStream(path);

        try {
            doc = reader.read(in);
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new RuntimeException("xml文件路径错误");
        }

        String xpath = "//bean";
        List<Element> list = doc.selectNodes(xpath);

        if (list != null) {
            for (Element beanElement : list) {
                Definition definition = new Definition();
                definition.isClass(true);

                String id = beanElement.attributeValue("id");

                String className = beanElement.attributeValue("class");

                definition.setId(id);
                try {
                    Class<?> clazz = getCurrentClassLoader().loadClass(className);
                    definition.setType(clazz);
                } catch (ClassNotFoundException e) {
                    throw new BadClassNameException(className);
                }

                List<Element> proList = beanElement.elements("property");
                if (proList != null) {
                    DefProperty[] properties = new DefProperty[proList.size()];
                    int i = 0;
                    for (Element proElement : proList) {
                        DefProperty prop = new DefProperty();
                        String propName = proElement.attributeValue("name");
                        String propValue = proElement.attributeValue("value");
                        String propRef = proElement.attributeValue("ref");
                        prop.setName(propName);
                        prop.setValue(propValue);
                        prop.setRef(propRef);
                        properties[i++] = prop;
                    }
                    definition.setProperties(properties);
                }
                if (configMap.stream().anyMatch(def -> null != id && !"".equals(id) && id.equals(def.getId()))) {
                    throw new RuntimeException("bean节点id重复:" + id);
                }
                configMap.add(definition);
            }
        }
        return configMap;
    }
}
