package site.zido.bone.utils.graph;

import org.junit.Assert;
import org.junit.Test;
import site.zido.bone.core.utils.graph.OrthogonalArcGraph;

import java.util.Iterator;

/**
 * site.zido.test.site.zido.bone.utils.graph
 *
 * @author zido
 */
public class OrthogonalGraphTest {
    @Test
    public void testCreateGraph() {
        OrthogonalArcGraph<Integer> graph = new OrthogonalArcGraph<>();
        int v0 = graph.add(0);
        int v1 = graph.add(1);
        int v2 = graph.add(2);
        int v3 = graph.add(3);
        graph.connect(v0, v3);
        graph.connect(v1, v0);
        graph.connect(v1, v2);
        graph.connect(v2, v1);
        graph.connect(v2, v0);

        Assert.assertTrue(graph.size() == 4);

        Assert.assertTrue(graph.get(v0, v1) == null);
        Assert.assertTrue(graph.get(v1, v0) != null);
    }

    @Test
    public void testEachGraph() {
        OrthogonalArcGraph<Integer> graph = new OrthogonalArcGraph<>();
        int v0 = graph.add(0);
        int v1 = graph.add(1);
        int v2 = graph.add(2);
        int v3 = graph.add(3);
        graph.connect(v0, v3);
        graph.connect(v1, v0);
        graph.connect(v1, v2);
        graph.connect(v2, v0);
        for (Integer v : graph) {
            Assert.assertTrue((v == 0 || v == 3));
        }
        Iterator<Integer> iter = graph.iterator(v2);
        while (iter.hasNext()) {
            Integer v = iter.next();
            Assert.assertTrue((v != 1));
        }

        iter = graph.iterator(v0,false);
        while (iter.hasNext()) {
            Integer v = iter.next();
            Assert.assertTrue((v != 3));
        }
    }
}
