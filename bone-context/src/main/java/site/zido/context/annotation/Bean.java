package site.zido.context.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {
    String id() default "";

    String initMethod() default "";

    String destroyMethod() default "";
}
