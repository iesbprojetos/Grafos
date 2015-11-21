package grafos.datatypes.list;

import java.util.ArrayList;
import java.util.List;

import grafos.datatypes.matriz.PriorityQueue;

import static grafos.Constants.*;

public class VectorDigraphCost extends VectorDigraph {
    private static int INF = Integer.MAX_VALUE;

    int costFromS[];
    int franja[];

    protected int startVertex;

    /**
     * Conta quantos arcos com custos negativos o grafo possui
     */
    protected int negativeCosts;

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
        if (cost < 0) {
            negativeCosts++;
        }
        arcs++;
        return RESULT_OK;
    }

    @Override
    public int removeArc(int v, int w) {
        boolean negative = false;

        for (VectorElement adjVertex : adjVector.get(v)) {
            if (adjVertex.getW() == w && adjVertex.getCost() < 0) {
                negative = true;
            }
        }

        int result = super.removeArc(v, w);

        if (result == RESULT_OK && negative) {
            negativeCosts--;
        }

        return result;
    }

    public boolean DAGmin(int s) {
        int i = 0;
        costFromS = new int[vertices];

        if (topologicalSort == null) {
            if (depthSearchComplete()) {
                return false;
            }
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

        return true;
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

    public void dijkstraHeap(int s) {
        costFromS = new int[vertices];
        parent = new int[vertices];

        for (int v = 0; v < vertices; v++) {
            costFromS[v] = INF;
            parent[v] = -1;
        }

        costFromS[s] = 0;
        parent[s] = s;

        Heap heap = new Heap(vertices);

        heap.push(s, 0);
        while (!heap.isEmpty()) {
            int v = heap.pop();

            for (VectorElement adjVertex : adjVector.get(v)) {
                int w = adjVertex.getW();
                int costW = heap.cost[w] != INF ? heap.cost[w] + adjVertex.getCost() : INF;
                costFromS[w] = costW;
                parent[w] = v;
                heap.push(w, costW);
            }
        }
    }

    private static class Heap {
        private int[] data;
        private int[] index;
        public int[] cost;
        private int size;

        public Heap(int vertices) {
            data = new int[vertices];
            index = new int[vertices];
            cost = new int[vertices];

            for (int i = 0; i < vertices; i++) {
                index[i] = -1;
                cost[i] = INF;
            }

            size = 0;
        }

        public boolean isEmpty() {
            return (size == 0);
        }

        private void shiftUp(int i) {
            int j;
            while (i > 0) {
                j = (i - 1) / 2;
                if (cost[data[i]] < cost[data[j]]) {
                    // swap here
                    int temp = index[data[i]];
                    index[data[i]] = index[data[j]];
                    index[data[j]] = temp;
                    // swap here
                    temp = data[i];
                    data[i] = data[j];
                    data[j] = temp;
                    i = j;
                } else
                    break;
            }
        }

        private void shiftDown(int i) {
            int j, k;
            while (2 * i + 1 < size) {
                j = 2 * i + 1;
                k = j + 1;
                if (k < size && cost[data[k]] < cost[data[j]]
                        && cost[data[k]] < cost[data[i]]) {
                    // swap here
                    int temp = index[data[k]];
                    index[data[k]] = index[data[i]];
                    index[data[i]] = temp;
                    // swap here
                    temp = data[k];
                    data[k] = data[i];
                    data[i] = temp;

                    i = k;
                } else if (cost[data[j]] < cost[data[i]]) {
                    // swap here
                    int temp = index[data[j]];
                    index[data[j]] = index[data[i]];
                    index[data[i]] = temp;
                    // swap here
                    temp = data[j];
                    data[j] = data[i];
                    data[i] = temp;

                    i = j;
                } else
                    break;
            }
        }

        public int pop() {
            int res = data[0];
            data[0] = data[size - 1];
            index[data[0]] = 0;
            size--;
            shiftDown(0);
            return res;
        }

        public void push(int x, int c) {
            if (index[x] == -1) {
                cost[x] = c;
                data[size] = x;
                index[x] = size;
                size++;
                shiftUp(index[x]);
            } else {
                if (c < cost[x]) {
                    cost[x] = c;
                    shiftUp(index[x]);
                    shiftDown(index[x]);
                }
            }
        }
    }

    public void primDenso() {
        franja = new int[vertices];
        costFromS = new int[vertices];
        parent = new int[vertices];

        for (int v = 0; v < vertices; v++) {
            costFromS[v] = INF;
            parent[v] = -1;
            franja[v] = -1;
        }

        int v = 0;
        costFromS[v] = 0;
        franja[v] = v;

        while (true) {
            int minCost = INF;

            for (int w = 0; w < vertices; w++) {
                if (parent[w] == -1 && minCost > costFromS[w]) {
                    minCost = costFromS[v=w];
                }
            }

            if (minCost == INF) {
                break;
            }

            parent[v] = franja[v];

            for (VectorElement adjVertex : adjVector.get(v)) {
                int w = adjVertex.getW();
                if ((parent[w] == -1) && (costFromS[w] > adjVertex.getCost())) {
                    costFromS[w] = adjVertex.getCost();
                    franja[w] = v;
                }
            }
        }
    }

    public void primEsparso() {
        franja = new int[vertices];
        costFromS = new int[vertices];
        parent = new int[vertices];
        PriorityQueue pq = new PriorityQueue(vertices, INF);

        for (int v = 0; v < vertices; v++) {
            franja[v] = -1;
            parent[v] = -1;
        }

        int v = 0;
        costFromS[v] = 0;
        franja[v] = v;
        pq.insert(v, 0);

        while (!pq.isEmpty()) {
            v = pq.removeHighestPriority();
            parent[v] = franja[v];
            for (VectorElement adjVertex : adjVector.get(v)) {
                int w = adjVertex.getW();
                if (parent[w] == -1) {
                    if (franja[w] == -1) {
                        costFromS[w] = adjVertex.getCost();
                        franja[w] = v;
                        pq.insert(w, costFromS[w]);
                    } else {
                        if (costFromS[w] > adjVertex.getCost()) {
                            franja[w] = v;
                            costFromS[w] = adjVertex.getCost();
                            pq.setValue(w, costFromS[w]);
                        }
                    }
                }
            }
        }
    }

    public int[] getCostFromS() {
        return costFromS;
    }

    public boolean hasNegativeCosts() {
        return negativeCosts > 0;
    }
}
 