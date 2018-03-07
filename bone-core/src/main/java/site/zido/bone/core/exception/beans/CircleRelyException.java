package site.zido.bone.core.exception.beans;

import site.zido.bone.core.beans.structure.Definition;

public class CircleRelyException extends FatalBeansException {
    public CircleRelyException(Iterable<Definition> stack) {
        super("");
    }
}
