package site.zido.bone.core.beans;

import java.util.HashSet;
import java.util.Set;

/**
 * 解析管理器
 *
 * @author zido
 * @date 2018 /05/10
 * @since 2017 /30/21 下午2:30
 */
public class ParserManager {
    /**
     * The constant parsers.
     */
    private static Set<IBeanParser> parsers = new HashSet<>();

    /**
     * Register parser.
     *
     * @param parser the parser
     */
    public static void registerParser(AbstractBeanParser parser) {
        parsers.add(parser);
    }

    /**
     * Parser all.
     */
    public static void parserAll() {
        for (IBeanParser parser : parsers) {
            parser.parser();
        }
    }
}
