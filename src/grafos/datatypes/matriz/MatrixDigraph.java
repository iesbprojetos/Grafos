package grafos.datatypes.matriz;

import grafos.datatypes.GraphBase;

/**
 * Representa um Digrafo usando o algoritmo Matriz de Adjacência
 * Created by dfcarvalho on 8/21/15.
 */
public class MatrixDigraph extends GraphBase {
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
        this.vertices = vertices;
        this.arcs = 0;

        adjMatrix = new int[vertices][vertices];
    }

    /**
     * Insere um arco que se inicia em V e termina em W
     * @param v: vértice inicial
     * @param w: vértice final
     */
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
}
