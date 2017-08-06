package site.zido.utils.business;

import site.zido.utils.commons.PropertiesUtils;

import java.util.Properties;

/**
 * 语言工具类
 * <p>fill description</p>
 *
 * @author zido
 * @since 2017/6/2 0002
 */
public class LangUtils {
  //默认采用lang.properties文件
  private static final Properties prop;

  static {
    prop = PropertiesUtils.use("lang.properties");
  }

  /**
   * 获取语言
   *
   * @param keys 关键字参数，当有多个参数是，得到的字段会以逗号分隔符隔开
   * @return 语言信息
   */
  public static String lang(String... keys) {
    StringBuilder stringBuilder = new StringBuilder();
    for (String key : keys) {
      stringBuilder.append(",");
      stringBuilder.append(PropertiesUtils.value(key));
    }
    if (keys.length > 0)
      stringBuilder.deleteCharAt(0);
    return stringBuilder.toString();
  }
}
