package site.zido.core;

import javax.servlet.ServletContext;

/**
 * 环境变量.
 * <p>
 * Date:17-7-18 下午7:33
 *
 * @author <a href="zido.site">zido</a>
 * @version 1.0.0
 */
public class Environment {
  private ServletContext context;
  private String contextPath;

  public ServletContext getContext() {
    return context;
  }

  public void setContext(ServletContext context) {
    this.context = context;
  }

  public String getContextPath() {
    return contextPath;
  }

  public void setContextPath(String contextPath) {
    this.contextPath = contextPath;
  }
}
