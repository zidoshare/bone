package site.zido.utils.cipher;


import org.junit.Assert;
import org.junit.Test;
import site.zido.utils.business.CipherUtils;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;

/**
 * 加密解密测试
 *
 * @author zido
 * @since 2017/5/26 0026
 */
public class TestCipherUtils {
    @Test
    public void testRSA() throws Exception {
        HashMap<String, Object> map = CipherUtils.getKeys();
        //生成公钥和私钥
        RSAPublicKey publicKey = (RSAPublicKey) map.get(CipherUtils.PUBLIC_KEY);
        RSAPrivateKey privateKey = (RSAPrivateKey) map.get(CipherUtils.PRIVATE_KEY);

        //模
        String modulus = publicKey.getModulus().toString();
        //公钥指数
        String public_exponent = publicKey.getPublicExponent().toString();
        //私钥指数
        String private_exponent = privateKey.getPrivateExponent().toString();
        //明文
        String ming = "adawddfbsdgbdfghnghjkmghj,m";
        //使用模和指数生成公钥和私钥
        RSAPublicKey pubKey = CipherUtils.getPublicKey(modulus, public_exponent);
        RSAPrivateKey priKey = CipherUtils.getPrivateKey(modulus, private_exponent);
        //加密后的密文
        String mi = CipherUtils.RSAEncode(ming, pubKey);
        System.out.println("密文：" + mi);
        System.out.println("密文长度：" + mi.length());
        //解密后的明文
        ming = CipherUtils.RSADecode(mi, priKey);
        System.out.println("明文：" + ming);
    }

    @Test
    public void testAES() {
        String start = "123456";
        System.out.println("明文：" + start);

        String s = CipherUtils.AESEncode(start, "123456");

        System.out.println("密文：" + s);

        String result = CipherUtils.AESDecode(s, "123456");

        System.out.println("解密后：" + result);

        Assert.assertTrue("加解密错误", start.equals(result));

    }

    @Test
    public void testRSAAndAES() throws Exception {

        HashMap<String, Object> map = CipherUtils.getKeys();
        //生成公钥和私钥
        RSAPublicKey publicKey = (RSAPublicKey) map.get(CipherUtils.PUBLIC_KEY);
        RSAPrivateKey privateKey = (RSAPrivateKey) map.get(CipherUtils.PRIVATE_KEY);

        //模
        String modulus = publicKey.getModulus().toString();
        //公钥指数
        String public_exponent = publicKey.getPublicExponent().toString();
        //私钥指数
        String private_exponent = privateKey.getPrivateExponent().toString();
        //明文
        String ming = "adawddfbsdgbdfghnghjkmghj,m";
        String aesPassword = "123456";

        //使用模和指数生成公钥和私钥
        RSAPublicKey pubKey = CipherUtils.getPublicKey(modulus, public_exponent);
        RSAPrivateKey priKey = CipherUtils.getPrivateKey(modulus, private_exponent);
        //加密后的aes password
        String password = CipherUtils.RSAEncode(aesPassword, pubKey);
        System.out.println("aesKey：" + password);
        //加密后的密文
        String mi = CipherUtils.AESEncode(ming, aesPassword);
        System.out.println("密文：" + mi);
        String newKey = CipherUtils.RSADecode(password, priKey);
        //解密后的明文
        ming = CipherUtils.AESDecode(mi, newKey);
        System.out.println("明文：" + ming);
    }
}
