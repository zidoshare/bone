package site.zido.core;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Created by zido on 17-7-12.
 * <p>
 * Date:17-7-12 上午10:29
 *
 * @author <a href="zido.site">zido</a>
 * @version 1.0.0
 */
@WebFilter(urlPatterns = "/*")
public class ZiInitializer implements Filter {
  @Inject
  private ZiConfig config;
  public void init(FilterConfig filterConfig) throws ServletException {
    config.execute();
    System.out.println("init");
  }

  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    System.out.println("filter2");
  }

  public void destroy() {
    System.out.println("destroy");
  }
}
