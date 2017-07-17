package site.zido.core;

import site.zido.utils.commons.PropertiesUtils;
import static site.zido.constants.ConfigConstants.*;

/**
 * 核心配置类
 * <p>
 * Date:17-7-12 上午11:06
 *
 * @author <a href="site.zido.site">site.zido</a>
 * @version 1.0.0
 */
public class ZiConfig {
  private ZiConfig() {
  }

  private static ZiConfig config = new ZiConfig();
  private static boolean isFirst = false;

  //是否处于开发模式
  private boolean mode;
  private String jdbcUrl;
  private String username;
  private String password;


  /**
   * 初始化实例
   * <p>本方法只能调用一次</p>
   *
   * @return 配置类
   */
  static ZiConfig newInstance() {
    if (ZiConfig.isFirst)
      throw new RuntimeException("初始化实例只能获取一次,如需获得实例可调用getInstance方法");
    PropertiesUtils.use("application.properties");
    //获取配置文件中的初始化变量
    Boolean mode = PropertiesUtils.valueToBool(MODE, true);
    String jdbcUrl = PropertiesUtils.value(JDBC_URL);
    String username = PropertiesUtils.value(USERNAME);
    String password = PropertiesUtils.value(PASSWORD);

    //设置初始化变量
    config.setMode(mode)
            .setJdbcUrl(jdbcUrl)
            .setUsername(username)
            .setPassword(password);


    String mainClassName = PropertiesUtils.value(MAIN_CLASS_NAME);
    if (mainClassName != null) {
      try {
        Class<?> classzz = Class.forName(mainClassName);
        Object o = classzz.newInstance();
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InstantiationException e) {
        e.printStackTrace();
      }
    }
    return config;
  }

  public static ZiConfig getInstance() {
    return config;
  }

  public boolean isMode() {
    return mode;
  }

  public ZiConfig setMode(boolean mode) {
    this.mode = mode;
    return this;
  }

  public String getJdbcUrl() {
    return jdbcUrl;
  }

  public ZiConfig setJdbcUrl(String jdbcUrl) {
    this.jdbcUrl = jdbcUrl;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public ZiConfig setUsername(String username) {
    this.username = username;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public ZiConfig setPassword(String password) {
    this.password = password;
    return this;
  }
}
