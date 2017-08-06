package site.zido.utils.commons;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * 文件操作工具类
 *
 * @author zido
 * @since 2017/6/27 0027
 */
public class FileUtils {
  /**
   * 通过时间分目录，使用uuid作为文件名
   *
   * @param filename
   * @return
   */
  public static String confuseFileName(String filename) {
    StringBuilder result = new StringBuilder();
    Calendar calendar = Calendar.getInstance();
    result.append(calendar.get(Calendar.YEAR)).append("/");
    String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
    if (month.length() < 2)
      month = "0" + month;
    result.append(month).append("/");
    String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    if (day.length() < 2)
      day = "0" + day;
    result.append(day).append("/");
    result.append(UUID.randomUUID());
    String[] split = filename.split("\\.");
    if (split.length > 0) {
      result.append(".").append(split[split.length - 1]);
    }
    return result.toString();
  }
}
