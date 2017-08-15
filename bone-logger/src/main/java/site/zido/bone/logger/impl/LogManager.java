package site.zido.bone.logger.impl;

import site.zido.bone.logger.Logger;

public class LogManager {
    public static Logger getLogger(String name){
        return new Slf4jLogger(name);
    }

    public static Logger getLogger(Class<?> classzz){
        return new Slf4jLogger(classzz);
    }
}
