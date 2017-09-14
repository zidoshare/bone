package site.zido.core;

import site.zido.core.beans.structure.Definition;
import site.zido.core.parser.IParser;
import site.zido.core.scanner.AbsScanner;

/**
 * bean工厂
 * <p>
 *     扫描器-解析器-工厂
 * </p>
 *
 * @author zido
 * @version 17-9-7 下午11:26
 * @since 0.0.1
 */
public class BeanFactory {
    private AbsScanner scanner;
    private IParser parser;
    public Object createBean(Definition definition){
        return null;
    }
    public void produce(){

    }
}
