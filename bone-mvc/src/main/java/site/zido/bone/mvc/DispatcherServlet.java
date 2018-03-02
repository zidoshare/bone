package site.zido.bone.mvc;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

/**
 * site.zido.bone.mvc
 *
 * @author zido
 */
@WebServlet(urlPatterns = "/*",loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet{
}
