package grafos.datatypes.matriz;

import java.util.PriorityQueue;

import static grafos.Constants.*;

/**
 * Created by dfcarvalho on 10/29/15.
 */
public class MatrixDrigraphCost extends MatrixDigraph {
    private static int INF = Integer.MAX_VALUE;

    /**
     * Vetor que armazena o custo
     */
    protected int[] custo;

    /**
     * Fila de prioridades
     */
    protected int[] pq;
    
    protected int[] ts;


    public MatrixDrigraphCost(int vertices) {
        super(vertices);
    }

    public int insertArc(int v, int w, int custo) {
        int result = super.insertArc(v, w);

        if (result == RESULT_OK) {
            adjMatrix[v][w] = custo;
        }

        return result;
    }

    public void dijkstra(int s) {
        custo = new int[vertices];
        parent = new int[vertices];

        for (int v = 0; v < vertices; v++) {
            custo[v] = INF;
            parent[v] = -1;
        }

        PQinit();
        custo[s] = 0;
        parent[s] = s;
        PQinsert(s, custo[s]);

        while(!PQempty()) {
            int v = PQdelmin();
            for (int w = 0; w < vertices; w++) {
                if (adjMatrix[v][w] != 0) {
                    if (custo[w] == INF) {
                        parent[w] = v;
                        custo[w] = custo[v] + adjMatrix[v][w];
                        PQinsert(w, custo[w]);
                    } else if (custo[w] > custo[v] + adjMatrix[v][w]) {
                        parent[w] = v;
                        custo[w] = custo[v] + adjMatrix[v][w];
                        PQdec(w, custo[w]);
                    }
                }
            }
        }
    }

    private void PQinit() {
        pq = new int[vertices];

        for (int v = 0; v < vertices; v++) {
            pq[v] = INF;
        }
    }

    private void PQinsert(int v, int custo) {
        pq[v] = custo;
    }

    private int PQdelmin() {
        int vMin = -1;
        int min = INF;

        for (int v = 0; v < vertices; v++) {
            if (pq[v] < min) {
                min = pq[v];
                vMin = v;
            }
        }

        pq[vMin] = INF;

        return vMin;
    }

    private void PQdec(int v, int novoCusto) {
        pq[v] = novoCusto;
    }

    private boolean PQempty() {
        for (int v = 0; v < vertices; v++) {
            if (pq[v] != INF) {
                return false;
            }
        }

        return true;
    }
    
    public void Dagmin (int s){
    	int i = 0;
	for (int v  = 0;v< vertices ;v++){
		custo[v] = INF;
		                 }
		custo[s] = 0;
		
		for (int v = ts[i];i < vertices;v = ts[i++]){
		for (int w = 0;w < vertices;w++){
			if( adjMatrix[v][w] != 0){
		       if (custo[w] > custo[v]+adjMatrix[v][w]){
		           custo[w] = custo[i]+adjMatrix[v][w];
		       }
		    }
		  }
	    }
      }
	}
  

