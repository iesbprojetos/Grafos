package grafos.datatypes.list;

import grafos.datatypes.GraphBase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dfcarvalho on 9/10/15.
 */
public class ListDigraph extends GraphBase {
    List<LinkedList<Integer>> list = new ArrayList<LinkedList<Integer>>();

    public ListDigraph(int vertices) {
        super(vertices);
    }

    @Override
    public void insertArc(int v, int w) {
        list.get(v).push(w);
    }

    @Override
    public void removeArc(int v, int w) {
        for (int i = 0; i < vertices; i++) {
            int vertice = list.get(v).get(i);
            if (vertice == w) {
                list.get(v).remove(w);
                break;
            }
        }
    }
}
