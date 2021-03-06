package site.zido.bone.mvc.annotations;

import site.zido.bone.core.beans.annotation.Component;

import java.lang.annotation.*;

/**
 * 服务
 *
 * @author zido
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Service {
}
