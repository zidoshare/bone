package site.zido.core.scanner;

import site.zido.core.parser.IParser;

/**
 * 扫描器接口
 *
 * @author zido
 * @version 17-9-7 下午10:48
 * @since 0.0.1
 */
public abstract class AbsScanner {
    private IParser parser;
    public AbsScanner(IParser parser){
        this.parser = parser;
    }

    public abstract void nextItem();
}
