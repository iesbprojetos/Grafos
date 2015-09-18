package grafos.datatypes.matriz;

import static grafos.Constants.*;
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
        super(vertices);

        adjMatrix = new int[vertices][vertices];
    }

    /**
     * Insere um arco que se inicia em V e termina em W
     * @param v: vértice inicial
     * @param w: vértice final
     */
    @Override
    public int insertArc(int v, int w) {
        // verifica se vértices são válidos
        if (v >= vertices || w >= vertices || v == w) {
            return RESULT_INVALID_VERTEX;
        }

        // verifica se arco existe
        if (adjMatrix[v][w] != 0) {
            return RESULT_ARC_ALREADY_EXISTS;
        }

        // insere arco
        adjMatrix[v][w] = 1;
        arcs++;

        return RESULT_OK;
    }

    /**
     * Remove um arco que se inicia em V e termina em W
     * @param v: vértice inicial
     * @param w: vértice final
     */
    @Override
    public int removeArc(int v, int w) {
        // verifica se vértices são válidos
        if (v >= vertices || w >= vertices || v == w) {
            return RESULT_INVALID_VERTEX;
        }

        // verifica se arco existe
        if (adjMatrix[v][w] == 0) {
            return RESULT_ARC_NOT_FOUND;
        }

        // remove arco;
        adjMatrix[v][w] = 0;
        return RESULT_OK;
    }

    /**
     * @return Retorna matriz de adjacência do grafo
     */
    public int[][] getAdjMatrix() {
        return adjMatrix;
    }

    /**
     * Visita recursivamente v e todos os vértices adjacentes a ele
     * @param v: vértice a ser visitado
     * Seta os vetores d, f e parent em cada visita
     */
    @Override
    protected void findPathR(int v) {
        // marca inicio da visita ao vetor v
        d[v] = time++;

        // percorre matriz de adjacência do vetor v
        for (int w = 0; w < vertices; w++) {
            // verifica se w é adjacente a v e se w já foi visitado
            if (adjMatrix[v][w] != 0 && d[w] == -1) {
                // é adjacente e ainda não foi visitado, então
                // marca v como pai de w
                parent[w] = v;
                // visita w
                findPathR(w);
            }
        }
        // marca fim da visita ao vetor v
        f[v] = time++;
    }

    /**
     * Faz a busca por profundidade recursivamente do grafo inteiro partindo do vértice v
     * @param v: vértice a ser visitado
     * @return true, se houver ciclo; false, se não houver
     */
    protected boolean depthSearchCompleteR(int v) {
        // marca a ordem na qual o vértice v foi visitado
        label[v] = countLabel++;
        // marca o início da visita ao vetor v
        d[v] = time++;

        boolean loop = false;

        // percorre os vértices do grafo
        for (int w = 0; w < vertices; w++) {
            // verifica se o vértice w é adjacente ao vértice v
            if (adjMatrix[v][w] == 1) {
                // se for, verifica se vértice w já foi visitado
                if (d[w] == -1) {
                    // vértice w não foi visitado...
                    // marca vértice v como pai do vértice w
                    parent[w] = v;

                    // visita vértice w
                    if (depthSearchCompleteR(w)) {
                        loop = true;
                    }
                } else if (f[w] == -1) {
                    // vértice w já foi visitado, existe ciclo
                    loop = true;
                }
            }
        }

        // marca o fim da visita ao vértice v
        f[v] = time++;
        // adiciona vértice v ao vetor de ordem topológica
        topologicalSort[tsCount--] = v;

        return loop;
    }
}
