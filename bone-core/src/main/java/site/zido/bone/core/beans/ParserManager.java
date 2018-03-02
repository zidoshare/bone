package site.zido.bone.core.beans;

import java.util.HashSet;
import java.util.Set;

/**
 * 解析管理器
 *
 * @author zido
 * @since 2017/30/21 下午2:30
 */
public class ParserManager {
    //parser 不重复
    private static Set<IBeanParser> parsers = new HashSet<>();

    public static void registerParser(AbsBeanParser parser) {
        parsers.add(parser);
    }

    public static void parserAll() {
        for (IBeanParser parser : parsers) {
            parser.parser();
        }
    }
}
