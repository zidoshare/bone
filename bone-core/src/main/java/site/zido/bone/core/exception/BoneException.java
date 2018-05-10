package site.zido.bone.core.exception;

/**
 * The type Bone exception.
 *
 * @author zido
 * @date 2018 /05/10
 */
public class BoneException extends Exception {
    /**
     * Instantiates a new Bone exception.
     *
     * @param msg the msg
     */
    public BoneException(String msg) {
        super(msg);
    }

    /**
     * Instantiates a new Bone exception.
     *
     * @param msg the msg
     * @param e   the e
     */
    public BoneException(String msg, Throwable e) {
        this(msg);
        this.initCause(e);
    }
}
