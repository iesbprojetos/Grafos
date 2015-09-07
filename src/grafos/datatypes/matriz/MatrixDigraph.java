package grafos.datatypes.matriz;

import grafos.datatypes.GraphBase;

import java.util.*;

/**
 * Representa um Digrafo usando o algoritmo Matriz de Adjacência
 * Created by dfcarvalho on 8/21/15.
 */
public class MatrixDigraph extends GraphBase {
    protected int[] parent;
    protected int time;
    protected int[] d;
    protected int[] f;
    protected int[] topologicalSort;
    protected int tsCount;
    protected int[] label;
    protected int countLabel;

    /**
     * Matriz de Adjacência
     * Inicializada no construtor com tamanho = vertices x vertices
     * Cada posição [V][W] indica se há um arco inciando no vértice V e terminando em W:
     *      1 = Arco existe
     *      0 = Arco não existe
     */
    protected int[][] adjMatrix;

    /**
     * Construtor
     * @param vertices: número de vértices do grafo
     */
    public MatrixDigraph(int vertices) {
        super(vertices);

        time = 0;
        adjMatrix = new int[vertices][vertices];
    }

    /**
     * Insere um arco que se inicia em V e termina em W
     * @param v: vértice inicial
     * @param w: vértice final
     */
    @Override
    public void insertArc(int v, int w) {
        if (v < vertices && w < vertices &&
                v != w && adjMatrix[v][w] == 0) {
            adjMatrix[v][w] = 1;
            arcs++;
        }
    }

    /**
     * Remove um arco que se inicia em V e termina em W
     * @param v: vértice inicial
     * @param w: vértice final
     */
    @Override
    public void removeArc(int v, int w) {
        if (v < vertices && w < vertices) {
            adjMatrix[v][w] = 0;
        }
    }

    /**
     * @return Retorna matriz de adjacência do grafo
     */
    public int[][] getAdjMatrix() {
        return adjMatrix;
    }

    public int[] getParent() {
        return parent;
    }

    public int[] getD() {
        return d;
    }

    public int[] getF() {
        return f;
    }

    public int[] getTopologicalSort() {
        return topologicalSort;
    }

    public int[] getLabel() {
        return label;
    }

    public boolean findPath(int s, int t) {
        time = 0;
        d = new int[vertices];
        f = new int[vertices];
        parent = new int[vertices];

        for (int v = 0; v < vertices; v++) {
            d[v] = -1;
            f[v] = -1;
            parent[v] = -1;
        }

        parent[s] = s;
        findPathR(s);

        return f[t] != -1;
    }

    private void findPathR(int v) {
        d[v] = time++;

        for (int w = 0; w < vertices; w++) {
            if (adjMatrix[v][w] != 0 && d[w] == -1) {
                parent[w] = v;
                findPathR(w);
            }
        }
        f[v] = time++;
    }

    public List<Integer> getArrayPath(int s, int t) {
        if (findPath(s, t)) {
            List<Integer> path = new ArrayList<>();

            for (int x = t; parent[x] != x; x = parent[x]) {
                path.add(x);
            }
            path.add(s);
            Collections.reverse(path);

            return path;
        }

        return null;
    }

    public boolean digraphCycle() {
        time = 0;
        tsCount = vertices-1;
        topologicalSort = new int[vertices];
        d = new int[vertices];
        f = new int[vertices];
        parent = new int[vertices];
        countLabel = 0;
        label = new int[vertices];

        for (int v = 0; v < vertices; v++) {
            d[v] = f[v] = parent[v] = -1;
        }

        for (int v = 0; v < vertices; v++) {
            if (d[v] == -1) {
                parent[v] = v;
                if (cycleR(v)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean cycleR(int v) {
        label[v] = countLabel++;
        d[v] = time++;

        boolean loop = false;

        for (int w = 0; w < vertices; w++) {
            if (adjMatrix[v][w] == 1) {
                if (d[w] == -1) {
                    parent[w] = v;

                    if (cycleR(w)) {
                        loop = true;
                    }
                } else if (f[w] == -1) {
                    loop = true;
                }
            }
        }

        f[v] = time++;
        topologicalSort[tsCount--] = v;
        return loop;
    }
}
