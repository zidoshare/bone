package site.zido.utils.cipher;

import org.junit.Assert;
import org.junit.Test;
import site.zido.utils.business.BankCardUtils;

/**
 * 银行卡测试
 *
 * @author zido
 * @since 2017/5/31 0031
 */
public class TestBankCardUtils {
    @Test
    public void testCheck() {
        boolean b = BankCardUtils.checkBankCard("6217853100012885059");
        Assert.assertTrue("校验失败", b);
    }

    @Test
    public void testGetName() {
        String name = BankCardUtils.getNameOfBank("6217853100012885059");
        System.out.println(name);
    }
}
