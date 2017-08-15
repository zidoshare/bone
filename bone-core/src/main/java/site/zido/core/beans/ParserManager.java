package site.zido.core.beans;

import java.util.HashSet;
import java.util.Set;

public class ParserManager {
    //parser 不重复
    private static Set<IBeanParser> parsers = new HashSet<>();
    public static void registerParser(AbsBeanParser parser){
        parsers.add(parser);
    }
    public static void parserAll(){
        for (IBeanParser parser : parsers) {
            parser.parser();
        }
    }
}
