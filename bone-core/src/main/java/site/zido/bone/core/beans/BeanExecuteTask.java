package site.zido.bone.core.beans;

import site.zido.bone.core.beans.structure.DefProperty;
import site.zido.bone.core.beans.structure.Definition;
import site.zido.bone.core.utils.ValiDateUtils;
import site.zido.bone.core.utils.graph.GraphNode;

public abstract class BeanExecuteTask extends PostTask {
    private DefProperty[] properties;
    private Definition definition;

    public abstract void run(Object[] params);

    @Override
    public boolean run() {
        DefProperty[] properties = getProperties();
        if (properties == null || properties.length == 0) {
            run(null);
            return true;
        }
        Object[] params = new Object[properties.length];
        for (int i = 0; i < properties.length; i++) {
            DefProperty defProperty = properties[i];
            if (!ValiDateUtils.isEmpty(defProperty.getValue())) {
                params[i] = defProperty.getValue();
            } else if (!ValiDateUtils.isEmpty(defProperty.getRef())) {
                params[i] = BoneContext.getInstance().getBean(defProperty.getRef());
            } else if (defProperty.getType() != null) {
                params[i] = BoneContext.getInstance().getBean(defProperty.getType());
            }
            if (params[i] == null) {
                return false;
            }
        }
        run(params);
        return true;
    }

    public void addToGraph(PostGraph graph) {
        for (GraphNode node : graph) {
            if (node instanceof BeanExecuteTask) {
                //TODO 添加到图中
            }
        }
    }

    public DefProperty[] getProperties() {
        return properties;
    }

    public BeanExecuteTask need(DefProperty[] properties) {
        this.properties = properties;
        return this;
    }

    public BeanExecuteTask produce(Definition definition) {
        this.definition = definition;
        return this;
    }

    public Definition getDefinition() {
        return definition;
    }
}
