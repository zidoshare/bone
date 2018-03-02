package site.zido.bone.core.beans.annotation;

import java.lang.annotation.*;

/**
 * 此注解和被此注解标记的注解所标记的类会被自动实例化并注入到容器中
 *
 * @author zido
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {
    String id() default "";
}
