package Graph;

import Graph.Graph.Edge;

import java.util.Comparator;

/**
 * Compare 2 edges.
 *
 * @author Yan Khonskiy.
 */
public class EdgeComparator implements Comparator {

    private int min(int a, int b) {
        if (a < b)
            return a;
        else
            return b;
    }

    private int max(int a, int b) {
        if (a > b)
            return a;
        else
            return b;
    }

    @Override
    public int compare(Object t, Object t1) {
        Edge e1 = (Edge) t;
        Edge e2 = (Edge) t1;
        if (min(e1.id1, e1.id2) > min(e2.id1, e2.id2))
            return 1;
        else if (min(e1.id1, e1.id2) < min(e2.id1, e2.id2))
            return -1;
        else if (max(e1.id1, e1.id2) > max(e2.id1, e2.id2))
            return 1;
        else
            return -1;
    }
}