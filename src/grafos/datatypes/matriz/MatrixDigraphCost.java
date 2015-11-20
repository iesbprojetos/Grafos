package grafos.datatypes.matriz;

import static grafos.Constants.*;

/**
 * Created by dfcarvalho on 10/29/15.
 */
public class MatrixDigraphCost extends MatrixDigraph {
    private static int INF = Integer.MAX_VALUE;

    /**
     * Vetor que armazena a matriz de custos de um vértice 's' a um vértice 't'
     * Primeiro índice (s) = vértice inicial
     * Segundo índice (t) = vértice final
     */
    protected int[][] cost;

    /**
     * Vetor que armazena o custo a partir de um vértice S
     * this.s = vérice inicial
     * Índice (t) = vértice final
     */
    protected int[] costFromS;

    /**
     * Vértice "selecionado" a partir do qual são calculados os custos em "costFromS"
     */
    protected int startVertex;

    /**
     * Conta quantos arcos com custos negativos o grafo possui
     */
    protected int negativeCosts;

    public MatrixDigraphCost(int vertices) {
        super(vertices);
    }

    /**
     * Insere arco de v a w com custo
     * @param v vértice inicial
     * @param w vértice final
     * @param custo custo do arco
     * @return RESULT_OK para sucesso ou código de erro
     */
    public int insertArc(int v, int w, int custo) {
        int result = super.insertArc(v, w);

        if (result == RESULT_OK) {
            adjMatrix[v][w] = custo;
            if (custo < 0) {
                negativeCosts++;
            }
        }

        return result;
    }

    @Override
    public int removeArc(int v, int w) {
        boolean negative = false;

        if (adjMatrix[v][w] < 0) {
            negative = true;
        }

        int result = super.removeArc(v, w);

        if (result == RESULT_OK && negative) {
            negativeCosts--;
        }

        return result;
    }

    /**
     * Algoritmo de Dijkstra: define a árvore de caminhos mínimos do grafo partindo de um vértice s,
     * fazendo uma busca por largura e utilizando uma fila de prioridades.
     * A árvore é representada pelo vetor parent
     * Restrições:
     *  -> Não pode haver custos negativos no grafo
     * @param s vértice inicial
     */
    public void dijkstra(int s) {
        startVertex = s;
        costFromS = new int[vertices];
        parent = new int[vertices];

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
            for (int w = 0; w < vertices; w++) {
                if (adjMatrix[v][w] != 0) {
                    if (costFromS[w] == INF) {
                        parent[w] = v;
                        costFromS[w] = costFromS[v] + adjMatrix[v][w];
                        pq.insert(w, costFromS[w]);
                    } else if (costFromS[w] > costFromS[v] + adjMatrix[v][w]) {
                        parent[w] = v;
                        costFromS[w] = costFromS[v] + adjMatrix[v][w];
                        pq.setValue(w, costFromS[w]);
                    }
                }
            }
        }
    }

    /**
     * Algoritmo DAGmin: define a árvore de caminhos mínimos do grafo partindo de um vértice s,
     * fazendo uma busca por largura e utilizando a ordem topológica (obtida pela busca por profundidade).
     * A árvore é representada pelo vetor parent
     * Restrições:
     *  -> Não pode haver ciclos alcançáveis a partir do vetor S
     *  -> Precisa da ordem topológica.
     * @param s vértice inicial
     */
    public void DAGmin(int s) {
        costFromS = new int[vertices];
        parent = new int[vertices];

        if (topologicalSort == null) {
            depthSearchComplete();
        }

        for (int v = 0; v < vertices; v++) {
            costFromS[v] = INF;
        }

        costFromS[s] = 0;

        int i = 0;
        for (int v = topologicalSort[i]; i < vertices; v = topologicalSort[i++]) {
            for (int w = 0; w < vertices; w++) {
                if (adjMatrix[v][w] != 0) {
                    int cost = costFromS[v] != INF ? costFromS[v] + adjMatrix[v][w] : INF;
                    if (costFromS[w] > cost) {
                        costFromS[w] = cost;
                        parent[w] = v; // ?
                    }
                }
            }
        }
    }

    /**
     *
     * @param s
     */
    public void bellmanFord2(int s) {
        costFromS = new int[vertices];
        parent = new int[vertices];

        for (int v = 0; v < vertices; v++) {
            costFromS[v] = INF;
            parent[v] = -1;
        }

        costFromS[s] = 0;
        parent[s] = s;

        for (int k = 1; k < vertices; k++) {
            for (int w = 0; w < vertices; w++) {
                for (int v = 0; v < vertices; v++) {
                    if (adjMatrix[v][w] != 0) {
                        int d = costFromS[v] != INF ? costFromS[v] + adjMatrix[v][w] : INF;
                        if (costFromS[w] > d) {
                            costFromS[w] = d;
                            parent[w] = v;
                        }
                    }
                }
            }
        }
    }

    public void floydWarshaw() {
        cost = new int[vertices][vertices];

        for (int s = 0; s < vertices; s++) {
            for (int t = 0; t < vertices; t++) {
                if (s == t) {
                    cost[s][t] = 0;
                } else if (adjMatrix[s][t] != 0){
                    cost[s][t] = adjMatrix[s][t];
                } else {
                    cost[s][t] = INF;
                }
            }
        }

        for (int k = 1; k < vertices+1; k++) {
            for (int s = 0; s < vertices; s++) {
                for (int t = 0; t < vertices; t++) {
                    int d;

                    try {
                        d = Math.addExact(cost[s][k-1], cost[k-1][t]);
                    } catch (ArithmeticException e) {
                        d = INF;
                    }

                    if (cost[s][t] > d) {
                        cost[s][t] = d;
                    }
                }
            }
        }
    }

    public int[] getCostFromS() {
        return costFromS;
    }

    public int[][] getCost() {
        return cost;
    }

    public boolean hasNegativeCosts() {
        return negativeCosts > 0;
    }
}
  

