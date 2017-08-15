package site.zido.core.beans;

import java.util.Map;

/**
 * xml文件 Bean解析类
 *
 * @author zido
 * @since 2017/5/28 0028
 */
public class XmlParser extends AbsBeanParser {
    //实体类容器
    private BoneIoc ioc = BoneIoc.getInstance();
    private Map<String,Definition> config;
    private String path = null;

    public XmlParser(String path){
        this.path = path;
        ParserManager.registerParser(this);
    }

    @Override
    protected Map<String, Definition> getConfig() {
        return XmlConfig.getConfig(path);
    }
}
