package site.zido.utils.business;

import site.zido.utils.commons.CodeUtils;
import site.zido.utils.commons.CollectionUtils;
import site.zido.utils.commons.StringUtils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.util.Base64;
import java.util.HashMap;

/**
 * 密文工具
 * <p>加密解密信息</p>
 *
 * @author zido
 * @since 2017/5/25 0025
 */
public class CipherUtils {
  public static final String RSA = "RSA";
  public static final String AES = "AES";
  public static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";
  public static final String PUBLIC_KEY = "publicKey";
  public static final String PRIVATE_KEY = "privateKey";

  /**
   * RSA密钥长度必须是64的倍数，在512~65536之间。默认是1024
   */
  public static final int KEY_SIZE = 2048;

  /**
   * 生成公钥和私钥
   *
   * @throws NoSuchAlgorithmException 不可用
   */
  public static HashMap<String, Object> getKeys() throws NoSuchAlgorithmException {
    HashMap<String, Object> map = new HashMap<>();
    KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(RSA);
    keyPairGen.initialize(KEY_SIZE);
    KeyPair keyPair = keyPairGen.generateKeyPair();
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
    map.put(PUBLIC_KEY, publicKey);
    map.put(PRIVATE_KEY, privateKey);
    return map;
  }

  /**
   * 使用模和指数生成RSA公钥
   * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
   * /None/NoPadding】
   *
   * @param modulus  模
   * @param exponent 指数
   * @return 公钥
   */
  public static RSAPublicKey getPublicKey(String modulus, String exponent) {
    try {
      BigInteger b1 = new BigInteger(modulus);
      BigInteger b2 = new BigInteger(exponent);
      KeyFactory keyFactory = KeyFactory.getInstance(RSA);
      RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
      return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 使用模和指数生成RSA私钥
   * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
   * /None/NoPadding】
   *
   * @param modulus  模
   * @param exponent 指数
   * @return
   */
  public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {
    try {
      BigInteger b1 = new BigInteger(modulus);
      BigInteger b2 = new BigInteger(exponent);
      KeyFactory keyFactory = KeyFactory.getInstance(RSA);
      RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
      return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 公钥加密
   *
   * @param data
   * @param publicKey
   * @return
   * @throws Exception
   */
  public static String RSAEncode(String data, RSAPublicKey publicKey)
          throws Exception {
    Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    // 模长
    int key_len = publicKey.getModulus().bitLength() / 8;
    // 加密数据长度 <= 模长-11
    String[] datas = StringUtils.splitString(data, key_len - 11);
    StringBuilder mi = new StringBuilder();
    //如果明文长度大于模长-11则要分组加密
    for (String s : datas) {
      mi.append(CodeUtils.bcd2Str(cipher.doFinal(s.getBytes())));
    }
    return mi.toString();
  }

  /**
   * 私钥解密
   *
   * @param data
   * @param privateKey
   * @return
   * @throws Exception
   */
  public static String RSADecode(String data, RSAPrivateKey privateKey)
          throws Exception {
    Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    //模长
    int key_len = privateKey.getModulus().bitLength() / 8;
    byte[] bytes = data.getBytes();
    byte[] bcd = CodeUtils.ASCII_To_BCD(bytes, bytes.length);
    //如果密文长度大于模长则要分组解密
    StringBuilder ming = new StringBuilder();
    byte[][] arrays = CollectionUtils.splitArray(bcd, key_len);
    for (byte[] arr : arrays) {
      ming.append(new String(cipher.doFinal(arr)));
    }
    return ming.toString();
  }

  /**
   * aes加密
   *
   * @param content  明文
   * @param password 密码
   * @return 密文
   */
  public static String AESEncode(String content, String password) {
    try {
      KeyGenerator kgen = KeyGenerator.getInstance(AES);
      SecureRandom instance = SecureRandom.getInstance("SHA1PRNG");
      instance.setSeed(password.getBytes());
      kgen.init(128, instance);
      SecretKey secretKey = kgen.generateKey();
      byte[] enCodeFormat = secretKey.getEncoded();
      SecretKeySpec key = new SecretKeySpec(enCodeFormat, AES);
      Cipher cipher = Cipher.getInstance(AES);// 创建密码器
      byte[] byteContent = content.getBytes("utf-8");
      cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
      return Base64.getEncoder().encodeToString(cipher.doFinal(byteContent)); // 加密
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    }
    return "";
  }

  /**
   * 解密
   *
   * @param content  待解密内容
   * @param password 解密密钥
   * @return 明文
   */
  public static String AESDecode(String content, String password) {
    try {
      KeyGenerator kgen = KeyGenerator.getInstance(AES);
      SecureRandom instance = SecureRandom.getInstance("SHA1PRNG");
      instance.setSeed(password.getBytes());
      kgen.init(128, instance);
      SecretKey secretKey = kgen.generateKey();
      byte[] enCodeFormat = secretKey.getEncoded();
      SecretKeySpec key = new SecretKeySpec(enCodeFormat, AES);
      Cipher cipher = Cipher.getInstance(AES);// 创建密码器
      cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
      byte[] result = cipher.doFinal(Base64.getDecoder().decode(content));
      return new String(result);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    }
    return "";
  }
}
