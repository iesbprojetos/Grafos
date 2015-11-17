package grafos.datatypes.list;

import java.util.ArrayList;
import java.util.List;

import static grafos.Constants.*;

public class VectorDigraphLowerCost extends VectorDigraph {
    private static int INF = Integer.MAX_VALUE;

    int custo[];

    public VectorDigraphLowerCost(int vertices) {
        super(vertices);
    }

    public int insertArc(int v, int w, int cost) {
        // verifica se vértices são válidos
        if (v >= vertices || w >= vertices || v == w) {
            return RESULT_INVALID_VERTEX;
        }

        for (VectorElement adjVertex : adjVector.get(v)) {
            if (adjVertex.getW() == w) {
                // arco já existe
                return RESULT_ARC_ALREADY_EXISTS;
            }
        }

        // senão, insere arco
        VectorElement adjVertex = new VectorElement(w, cost);
        adjVector.get(v).push(adjVertex);
        arcs++;
        return RESULT_OK;
    }

    public void Bellman_ford(int v, int w) {


    }

    public void Dagmin(int s) {
        int i = 0;
        custo = new int[vertices];

        if (topologicalSort == null) {
            depthSearchComplete();
        }

        for (int v = 0; v < vertices; v++) {
            custo[v] = INF;
        }

        custo[s] = 0;

        for (int v = topologicalSort[i]; i < vertices; v = topologicalSort[i++]) {
            for (VectorElement adjVertex : adjVector.get(v)) {
                int w = adjVertex.getW();
                if (custo[w] > custo[v] + adjVertex.getCost()) {
                    custo[w] = custo[v] + adjVertex.getCost();
                }
            }
        }
    }

    public void BellmanFordSentinela(int s) {
        int sentinela = vertices;
        List<Integer> listSentinela = new ArrayList<Integer>();

        for (int v = 0; v < vertices; v++) {
            custo[v] = INF;
            parent[v] = -1;
        }

        custo[s] = 0;
        parent[s] = 0;

        listSentinela.add(s);
        listSentinela.add(sentinela);

        while (listSentinela.size() > 0) {
            int v = listSentinela.remove(0);

        }
    }

    public int[] getCusto() {
        return custo;
    }
}
 