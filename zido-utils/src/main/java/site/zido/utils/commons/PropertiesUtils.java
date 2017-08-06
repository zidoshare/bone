package site.zido.utils.commons;

import java.io.*;
import java.util.*;

/**
 * 属性处理工具类
 * <p>用于处理各种properties文件</p>
 *
 * @author zido
 * @since 2017/6/2 0002
 */
public class PropertiesUtils {
  private static String fileName;
  private static Properties properties;

  /**
   * 使用properties文件 （自动缓存）
   *
   * @param fileName 文件名
   * @return 属性
   */
  public static Properties use(String fileName) {
    if (Objects.equals(fileName, PropertiesUtils.fileName))
      return properties;
    properties = read(fileName);
    if (properties != null)
      PropertiesUtils.fileName = fileName;
    return properties;
  }

  /**
   * 获取String值
   *
   * @param key 键名
   * @return String类型
   */
  public static String value(String key) {
    return value(key,null);
  }

  public static String value(String key,String defaultValue){
    if (properties == null) {
      return null;
    }
    return properties.getProperty(key,defaultValue);
  }

  /**
   * 获取Integer值
   *
   * @param key 键名
   * @return Integer类型
   */
  public static Integer valueToInt(String key) {
    return valueToInt(key,null);
  }

  public static Integer valueToInt(String key,Integer defaultValue){
    if (properties == null) {
      return null;
    }
    String property = properties.getProperty(key);
    if(ValiDateUtils.isEmpty(property))
      return defaultValue;
    return Integer.valueOf(property);
  }

  /**
   * 获取Long值
   *
   * @param key 键名
   * @return Long类型
   */
  public static Long valueToLong(String key) {
    return valueToLong(key,null);
  }

  public static Long valueToLong(String key,Long defaultValue){
    if (properties == null) {
      return null;
    }
    String property = properties.getProperty(key);
    if(ValiDateUtils.isEmpty(property))
      return defaultValue;
    return Long.valueOf(property);
  }

  /**
   * 获取Boolean值
   *
   * @param key 键名
   * @return Boolean类型
   */
  public static Boolean valueToBool(String key) {
    return valueToBool(key,null);
  }

  /**
   * 获取bool值
   * <p>
   *   除了针对true/false外,还增加了,例如非0值为true等规则,详细查看 {@link StringUtils#isTrue}
   * </p>
   * @param key 键名
   * @param defaultValue 默认值
   * @return true/false
   */
  public static Boolean valueToBool(String key,Boolean defaultValue){
    if (properties == null) {
      return null;
    }
    String property = properties.getProperty(key);
    if(StringUtils.isEmpty(property))
      return defaultValue;
    return StringUtils.isTrue(property);
  }


  /**
   * 根据文件名获取Properties对象
   *
   * @param fileName 文件名
   * @return properties对象
   */
  private static Properties read(String fileName) {
    InputStream in = null;
    try {
      Properties prop = new Properties();
      //InputStream in = Object.class.getResourceAsStream("/"+fileName);
      in = PropertiesUtils.class.getClassLoader().getResourceAsStream(fileName);
      Reader reader = new InputStreamReader(in, "UTF-8");
      prop.load(reader);
      return prop;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (in != null) {
          in.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  /**
   * 根据文件名和键名获取值
   *
   * @param fileName 文件名
   * @param key      键名
   * @return 值
   */
  public static String readValue(String fileName, String key) {
    Properties prop = read(fileName);
    if (prop != null) {
      return prop.getProperty(key);
    }
    return null;
  }

  /**
   * 根据键名获取值
   *
   * @param prop 属性
   * @param key  键名
   * @return 值
   */
  public static String readValue(Properties prop, String key) {
    if (prop != null) {
      return prop.getProperty(key);
    }
    return null;
  }

  /**
   * 写入
   *
   * @param fileName 文件名
   * @param key      键名
   * @param value    值
   */
  public static void writeValueByKey(String fileName, String key, String value) {
    Map<String, String> properties = new HashMap<String, String>();
    properties.put(key, value);
    writeValues(fileName, properties);
  }

  /**
   * 写入
   *
   * @param fileName   文件名
   * @param properties 属性
   */
  public static void writeValues(String fileName, Map<String, String> properties) {
    InputStream in = null;
    OutputStream out = null;
    try {
      in = PropertiesUtils.class.getClassLoader().getResourceAsStream(fileName);
      Properties prop = new Properties();
      prop.load(in);
      String path = PropertiesUtils.class.getResource("/" + fileName).getPath();
      out = new FileOutputStream(path);
      if (properties != null) {
        Set<String> set = properties.keySet();
        for (String string : set) {
          prop.setProperty(string, properties.get(string));
        }
      }
      prop.store(out, "update properties");
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (in != null) {
          in.close();
        }
        if (out != null) {
          out.flush();
          out.close();
        }
      } catch (Exception e2) {
        e2.printStackTrace();
      }
    }
  }
}
