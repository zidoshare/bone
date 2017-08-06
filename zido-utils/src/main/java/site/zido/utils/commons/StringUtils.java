package site.zido.utils.commons;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author zido
 * @since 2017/5/26 0026
 */
public class StringUtils {
  public static String toString(InputStream inputStream) throws IOException {
    StringBuilder out = new StringBuilder();
    byte[] b = new byte[4096];
    for (int n; (n = inputStream.read(b)) != -1; ) {
      out.append(new String(b, 0, n));
    }
    return out.toString();
  }

  public static InputStream toInputStream(String str) {
    return new ByteArrayInputStream(str.getBytes());
  }

  /**
   * 拆分字符串
   */
  public static String[] splitString(String string, int len) {
    int x = string.length() / len;
    int y = string.length() % len;
    int z = 0;
    if (y != 0) {
      z = 1;
    }
    String[] strings = new String[x + z];
    String str = "";
    for (int i = 0; i < x + z; i++) {
      if (i == x + z - 1 && y != 0) {
        str = string.substring(i * len, i * len + y);
      } else {
        str = string.substring(i * len, i * len + len);
      }
      strings[i] = str;
    }
    return strings;
  }

  public static boolean isEmpty(String s) {
    return null == s || "".equals(s);
  }

  public static boolean isNotEmpty(String s) {
    return !isEmpty(s);
  }

  public static String replaceBlank(String str) {
    String dest = "";
    if (str != null) {
      Pattern p = Pattern.compile("\\s*|\t|\r|\n");
      Matcher m = p.matcher(str);
      dest = m.replaceAll("");
    }
    return dest;
  }

  /**
   * 判断字符串是否是true
   * <p>
   *   符合一下条件为true:
   *   <br>
   *   数字且非0值 || 非空字符串 || "true"
   * </p>
   *
   * @param s
   * @return
   */
  public static Boolean isTrue(String s){
    if(isEmpty(s))
      return false;
    if(ValiDateUtils.isNumber(s)){
      Integer result = Integer.valueOf(s);
      return result != 0;
    }
    return "true".equalsIgnoreCase(s);
  }
}
