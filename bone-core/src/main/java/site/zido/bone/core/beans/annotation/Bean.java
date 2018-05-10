package site.zido.bone.core.beans.annotation;

import java.lang.annotation.*;

/**
 * Bean 用于标记实体
 *
 * @author zido
 * @date 2018 /05/10
 * @since 2017 /15/21 下午2:15
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {
    /**
     * Id string.
     *
     * @return the string
     */
    String id() default "";

    /**
     * Init method string.
     *
     * @return the string
     */
    String initMethod() default "";

    /**
     * Destroy method string.
     *
     * @return the string
     */
    String destroyMethod() default "";
}
