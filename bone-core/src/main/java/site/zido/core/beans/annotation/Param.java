package site.zido.core.beans.annotation;

import java.lang.annotation.*;
/**
 * Param 标记方法参数注入
 *
 * @author zido
 * @since 2017/15/21 下午2:15
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Param {
    String type() default "java.lang.Object";
    String value() default "";
    String ref() default "";
}
