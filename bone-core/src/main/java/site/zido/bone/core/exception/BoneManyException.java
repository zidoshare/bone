package site.zido.bone.core.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * 本异常没有特殊意义，仅用来封装多个异常
 *
 * @author zido
 * @date 2018 /05/10
 * @since 2017 /31/21 下午2:31
 */
public class BoneManyException extends Exception {
    /**
     * The Causes.
     */
    private List<Throwable> causes = new ArrayList<>();

    /**
     * Instantiates a new Bone many exception.
     *
     * @param causes the causes
     */
    public BoneManyException(List<? extends Throwable> causes) {
        this.causes.addAll(causes);
    }

    /**
     * Add exception boolean.
     *
     * @param <T>   the type parameter
     * @param cause the cause
     * @return the boolean
     */
    public <T extends Throwable> boolean addException(T cause) {
        return causes.add(cause);
    }

    /**
     * Gets exceptions.
     *
     * @return the exceptions
     */
    public List<Throwable> getExceptions() {
        return causes;
    }
}
