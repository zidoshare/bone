package site.zido.core.props;

import site.zido.utils.commons.PropertiesUtils;

public class PropLoader {
    private PropLoader(){}
    public static void init(){
        PropertiesUtils.use("default.properties");

    }
}
