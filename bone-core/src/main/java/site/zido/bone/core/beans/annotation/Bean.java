package site.zido.bone.core.beans.annotation;

import java.lang.annotation.*;

/**
 * Bean 用于标记实体
 *
 * @author zido
 * @since 2017/15/21 下午2:15
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {
    String id() default "";

    String initMethod() default "";

    String destroyMethod() default "";
}
