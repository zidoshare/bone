package site.zido.bone.core.beans.annotation;

import java.lang.annotation.*;

/**
 * 用于标记类中包含bean
 *
 * @author zido
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Beans {
}
