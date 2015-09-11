package grafos.datatypes.matriz;

import grafos.datatypes.GraphBase;

import java.util.*;

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

    /**
     * Busca
     * @param s: vértice inicial
     * @param t: vértice final
     * @return true, se houver caminho; false, se não houver. Também seta os vetores de tempo (d e f)
     * e o de pais de cada vértice (parent)
     */
    public boolean findPath(int s, int t) {
        // zera o tempo
        time = 0;
        // iniciar vetores de tempo: d (inicio) e f (fim)
        d = new int[vertices];
        f = new int[vertices];
        // inciar vertor de pais
        parent = new int[vertices];

        // inicializa vetores com -1
        for (int v = 0; v < vertices; v++) {
            d[v] = -1;
            f[v] = -1;
            parent[v] = -1;
        }

        // vértice raiz do caminho tem a si mesmo como pai
        parent[s] = s;

        // visita vértices recursivamente em busca do caminho entre s e t
        findPathR(s);

        // se f[t] for -1, então não existe caminho entre os vértices s e t
        return f[t] != -1;
    }

    /**
     * Visita recursivamente v e todos os vértices adjacentes a ele
     * @param v: vértice a ser visitado
     * Seta os vetores d, f e parent em cada visita
     */
    private void findPathR(int v) {
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
     * Retorna caminho entre os vértices s e t (na ordem correta)
     * @param s: vetor inicial
     * @param t: vetor final
     * @return Uma List contendo os vértices na ordem do caminho entre s e t ou null caso o caminho não exista
     */
    public List<Integer> getArrayPath(int s, int t) {
        // Verifica e calcula o caminho entre s e t
        if (findPath(s, t)) {
            // Se houver caminho...
            List<Integer> path = new ArrayList<>();

            // percorre vetor de pais do vértice final t até encontrar o vértice raiz do caminho (cujo pai é ele mesmo)
            // e adiciona cada vértice à lista
            for (int x = t; parent[x] != x; x = parent[x]) {
                path.add(x);
            }
            // inclui o vértice inicial
            path.add(s);
            // inverte a ordem dos vetores
            Collections.reverse(path);

            return path;
        }

        return null;
    }

    /**
     * Faz a busca por profundidade do grafo inteiro, marcando para cada vértice:
     *  - ordem de visita (label)
     *  - tempo de visita (inicial e final - d e f)
     *  - pai (parent)
     *  - ordem topológica (se grafo não possuir ciclo)
     * @return true, se grafo possui ciclo; false, se não possui ciclo.
     */
    public boolean depthSearchComplete() {
        // inicializa todas as variáveis e vetores
        time = 0;
        tsCount = vertices-1;
        topologicalSort = new int[vertices];
        d = new int[vertices];
        f = new int[vertices];
        parent = new int[vertices];
        countLabel = 0;
        label = new int[vertices];
        boolean loop = false;

        for (int v = 0; v < vertices; v++) {
            d[v] = f[v] = parent[v] = -1;
        }

        // percorre todos os vértices do grafo
        for (int v = 0; v < vertices; v++) {
            // verifica se vértice v já foi visitado
            if (d[v] == -1) {
                // se não foi visitado...
                // marca pai como eles mesmo (inicio de uma árvore)
                parent[v] = v;
                // faz a busca por profundidade partindo do vértice v
                if (depthSearchCompleteR(v)) {
                    loop = true;
                }
            }
        }

        return loop;
    }

    /**
     * Faz a busca por profundidade recursivamente do grafo inteiro partindo do vértice v
     * @param v: vértice a ser visitado
     * @return true, se houver ciclo; false, se não houver
     */
    private boolean depthSearchCompleteR(int v) {
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
