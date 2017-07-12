package site.zido.core;

import site.zido.utils.PropertiesUtils;

import javax.inject.Inject;

/**
 * Created by zido on 17-7-12.
 * <p>
 * Date:17-7-12 上午11:06
 *
 * @author <a href="zido.site">zido</a>
 * @version 1.0.0
 */
public class ZiConfig {
  @Inject
  private One one;
  public void execute(){
    System.out.printf("--------------%s",one);
   /* PropertiesUtils.use("application.properties");
    String mainName = PropertiesUtils.value("main");
    try {
      Object o = Class.forName(mainName).newInstance();
    } catch (Exception e){
      System.err.println("初始化出错,未找到初始化类或非法访问");
    }*/
  }
}
