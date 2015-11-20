package grafos.datatypes.list;

import java.util.ArrayList;
import java.util.List;

import grafos.datatypes.matriz.PriorityQueue;

import static grafos.Constants.*;

public class VectorDigraphCost extends VectorDigraph {
    private static int INF = Integer.MAX_VALUE;

    int costFromS[];
    
    protected int startVertex;

    public VectorDigraphCost(int vertices) {
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

    public void DAGmin(int s) {
        int i = 0;
        costFromS = new int[vertices];

        if (topologicalSort == null) {
            depthSearchComplete();
        }

        parent = new int[vertices];

        for (int v = 0; v < vertices; v++) {
            costFromS[v] = INF;
            parent[v] = -1;
        }

        costFromS[s] = 0;

        for (int v = topologicalSort[i]; i < vertices; v = topologicalSort[i++]) {
            for (VectorElement adjVertex : adjVector.get(v)) {
                int w = adjVertex.getW();
                if (costFromS[w] > costFromS[v] + adjVertex.getCost()) {
                    costFromS[w] = costFromS[v] + adjVertex.getCost();
                    parent[w] = v;
                }
            }
        }
    }
    
    public void dijkstra(int s) {
        costFromS = new int[vertices];
        parent = new int[vertices];
        startVertex = s;

        for (int v = 0; v < vertices; v++) {
            costFromS[v] = INF;
            parent[v] = -1;
        }

        PriorityQueue pq = new PriorityQueue(vertices, INF);
        costFromS[s] = 0;
        parent[s] = s;
        pq.insert(s, costFromS[s]);

        while (!pq.isEmpty()) {
            int v = pq.removeHighestPriority();
            for (VectorElement adjVertex : adjVector.get(v)) {
            	int w = adjVertex.getW();
                if (costFromS[w] == INF) {
                        parent[w] = v;
                        costFromS[w] = costFromS[v] + adjVertex.getCost();
                        pq.insert(w, costFromS[w]);
                    } else if (costFromS[w] > costFromS[v] + adjVertex.getCost()) {
                        parent[w] = v;
                        costFromS[w] = costFromS[v] + adjVertex.getCost();
                        pq.setValue(w, costFromS[w]);
                    }
                }
            }
        }
    

    public int bellmanFordSentinel(int s) {
        costFromS = new int[vertices];
        parent = new int[vertices];

        int sentinel = vertices;
        int k = 0;

        // "zera" custos e pais
        for (int v = 0; v < vertices; v++) {
            costFromS[v] = INF;
            parent[v] = -1;
        }

        // Inicia fila
        List<Integer> listSentinel = new ArrayList<>();

        // cost e pai do vértice inicial
        costFromS[s] = 0;
        parent[s] = 0;

        // adiciona vértice inicial à fila
        listSentinel.add(s);
        // adiciona sentinela à fila
        listSentinel.add(sentinel);

        // Enquanto fila não estiver vazia
        while (listSentinel.size() > 0) {
            // retira primeiro elemento da fila
            int v = listSentinel.remove(0);
            // se for sentinela...
            if (v == sentinel) {
                if (k++ == vertices) {
                    if (!(listSentinel.isEmpty())) {
                        return 0;
                    }
                } else {
                    return 1;
                }

                // re-insere sentinela na fila
                listSentinel.add(sentinel);
            } else {
                for (VectorElement adjVertex : adjVector.get(v)) {
                    int w = adjVertex.getW();
                    int c = costFromS[v] != INF ? costFromS[v] + adjVertex.getCost() : INF;
                    if (costFromS[w] > c) {
                        costFromS[w] = c;
                        parent[w] = v;
                        listSentinel.add(w);
                    }
                }
            }
        }

        // TODO: 1?
        return 1;
    }



    public int[] getCostFromS() {
        return costFromS;
    }
}
 