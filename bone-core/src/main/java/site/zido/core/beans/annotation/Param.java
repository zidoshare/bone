package site.zido.core.beans.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Param {
    String type() default "java.lang.Object";
    String value() default "";
    String ref() default "";
}
