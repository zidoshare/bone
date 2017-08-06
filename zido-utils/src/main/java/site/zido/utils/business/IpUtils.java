package site.zido.utils.business;

import site.zido.utils.commons.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * ip工具类
 *
 * @author zido
 * @since 2017/6/1 0001
 */
public class IpUtils {
  /**
   * 获取ip地址，已处理反向代理
   *
   * @param request request
   * @return ip
   */
  public static String getIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
      //多次反向代理后会有多个ip值，第一个ip才是真实ip
      int index = ip.indexOf(",");
      if (index != -1) {
        return ip.substring(0, index);
      } else {
        return ip;
      }
    }
    ip = request.getHeader("X-Real-IP");
    if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
      return ip;
    }
    return request.getRemoteAddr();
  }
}
