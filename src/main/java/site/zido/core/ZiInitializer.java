package site.zido.core;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 初始化入口,继承子filter,从而达到初始化的效果.
 * <p>
 * Date:17-7-12 上午10:29
 *
 * @author <a href="site.zido.site">site.zido</a>
 * @version 1.0.0
 */
@WebFilter(urlPatterns = "/*")
public class ZiInitializer implements Filter {
  public void init(FilterConfig filterConfig) throws ServletException {
    ZiConfig.newInstance(filterConfig.getServletContext());
  }

  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    System.out.println("filter2");
  }

  public void destroy() {
    System.out.println("destroy");
  }
}
