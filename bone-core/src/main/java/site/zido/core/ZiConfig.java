package site.zido.core;

import site.zido.utils.commons.PropertiesUtils;
import site.zido.constants.ConfigConstants;

import javax.servlet.ServletContext;

/**
 * 核心配置类
 * <p>
 * Date:17-7-12 上午11:06
 *
 * @author <a href="site.site.zido.site">site.site.zido</a>
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
  //环境配置
  private Environment environment;

  /**
   * 初始化实例
   * <p>本方法只能调用一次</p>
   *
   * @return 配置类
   */
  static ZiConfig newInstance(ServletContext context) {
    if (ZiConfig.isFirst)
      throw new RuntimeException("初始化实例只能获取一次,如需获得实例可调用getInstance方法");
    PropertiesUtils.use("application.properties");
    //获取配置文件中的初始化变量
    Boolean mode = PropertiesUtils.valueToBool(ConfigConstants.MODE, true);
    String jdbcUrl = PropertiesUtils.value(ConfigConstants.JDBC_URL);
    String username = PropertiesUtils.value(ConfigConstants.USERNAME);
    String password = PropertiesUtils.value(ConfigConstants.PASSWORD);

    //设置初始化变量
    config.setMode(mode)
            .setJdbcUrl(jdbcUrl)
            .setUsername(username)
            .setPassword(password);


    String mainClassName = PropertiesUtils.value(ConfigConstants.MAIN_CLASS_NAME);
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
