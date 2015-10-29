package grafos.datatypes;

import java.util.*;

/**
 * GraphBase - classe abstrata
 * Classe base para os Grafos/Digrafos.
 * Possui todos o membros e métodos comuns a todos os tipos de grafos utilizados
 * no programa.
 * Os métodos de busca de profundidade (depthSearchCompleteR) e de busca de
 * caminho (findPathR) precisam ser implementados pelas classes filhas,
 * pois suas implementações dependem do algoritmo utilizado para representar
 * o grafo (matriz ou vetor de adjacência)
 */
public abstract class GraphBase {
    /**
     * Número de vértices
     * Inicializado no construtor através de argumento
     */
    protected int vertices;

    /**
     * Número de arcos
     */
    protected int arcs;

    /**
     * Vetor de pais de cada vértice.
     * O valor 'w' do vetor no índice 'v' indica que o vértice 'w' é pai de 'v'
     * Preenchido ao realizar uma busca por profundidade ou busca de caminho
     * O pai de cada vértice dependerá das árvores criadas pela busca.
     */
    protected int[] parent;

    /**
     * Variável auxiliar utilizada para contar o "tempo" de inicio e
     * fim da visitação de cada vértice durante uma busca.
     */
    protected int time;

    /**
     * Vetor de tempo inicial da busca.
     * O valor 'x' do vetor no índice 'v' indica que a visita ao vértice 'v'
     * iniciou no tempo 'x'.
     * Se 'x' for -1, significa que vetor não foi visitado ainda .
     */
    protected int[] d;

    /**
     * Vetor de tempo inicial da busca.
     * O valor 'x' do vetor no índice 'v' indica que a visita ao vértice 'v'
     * terminou no tempo 'x'.
     * Se 'x' for -1, significa que a visita ao vetor v ainda não finalizou.
     */
    protected int[] f;

    /**
     * Vetor de ordem topológica do grafo
     * Os vértices são inseridos no vetor na ordem topológicaL:
     * índice 0 é o primeiro vértice
     * É preenchido durante busca de profundidade, somente se grafo não
     * possuir ciclo.
     */
    protected int[] topologicalSort;

    /**
     * Variável auxiliar utilizada para contar a ordem topológica
     */
    protected int tsCount;

    /**
     * Vetor da ordem de visita dos vértices
     * O valor 'x' do vetor no índice 'v' indica que o vértice v foi o
     */
    protected int[] label;

    /**
     * Variável auxiliar utilizada para contar a ordem de visita dos vértices
     */
    protected int countLabel;

    /**
     * Vetor que armazena a qual componente conexo cada vértice pertence
     * O valor 'x' do vetor no índice 'v' indica que o vértice v pertence ao componente x
     */
    protected int[] cc;

    /**
     * Variável auxiliar utilizada para contar os componentes conexos
     */
    protected int countCC;

    /**
     * Construtor
     *
     * @param vertices: número de vértices do grafo
     */
    public GraphBase(int vertices) {
        this.vertices = vertices;
    }

    /**
     * Retorna o número de vértices do grafo
     */
    public int getVertices() {
        return vertices;
    }

    /**
     * @return Retorna o número de arcos existentes no grafo
     */
    public int getArcs() {
        return arcs;
    }

    /**
     * @return Retorna o vetor de pais
     */
    public int[] getParent() {
        return parent;
    }

    /**
     * @return Retorna o vetor de tempo incial
     */
    public int[] getD() {
        return d;
    }

    /**
     * @return Retorna o vetor de tempo final
     */
    public int[] getF() {
        return f;
    }

    /**
     * @return Retorna o vetor de ordem topológica
     */
    public int[] getTopologicalSort() {
        return topologicalSort;
    }

    /**
     * @return Retorna o vetor de ordem de visita
     */
    public int[] getLabel() {
        return label;
    }

    /**
     * @return Retorna o vetor de componentes conexos
     */
    public int[] getCc() {
        return cc;
    }

    /**
     * Insere um arco que se inicia em V e termina em W
     *
     * @param v: vértice inicial
     * @param w: vértice final
     */
    public abstract int insertArc(int v, int w);

    /**
     * Remove um arco que se inicia em V e termina em W
     *
     * @param v: vértice inicial
     * @param w: vértice final
     */
    public abstract int removeArc(int v, int w);

    /**
     * Busca caminho entre dois vértices
     *
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
        // iniciar vetor de pais
        parent = new int[vertices];

        // inicializa vetores com -1
        for (int v = 0; v < vertices; v++) {
            d[v] = -1;
            f[v] = -1;
            parent[v] = -1;
        }

        parent[s] = s;

        findPathR(s);

        return f[t] != -1;
    }

    /**
     * Visita recursivamente v e todos os vértices adjacentes a ele
     *
     * @param v: vértice a ser visitado
     *           Seta os vetores d, f e parent em cada visita
     */
    protected abstract void findPathR(int v);

    /**
     * Retorna caminho entre os vértices s e t (na ordem correta)
     *
     * @param s: vetor inicial
     * @param t: vetor final
     * @return Uma List contendo os vértices na ordem do caminho entre s e t ou null caso o caminho não exista
     */
    public List<Integer> getArrayPath(int s, int t) {
        // Calcula o caminho entre s e t
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
     * - ordem de visita (label)
     * - tempo de visita (inicial e final - d e f)
     * - pai (parent)
     * - ordem topológica (se grafo não possuir ciclo)
     *
     * @return true, se grafo possui ciclo; false, se não possui ciclo.
     */
    public boolean depthSearchComplete() {
        // inicializa todas as variáveis e vetores
        time = 0;
        tsCount = vertices - 1;
        topologicalSort = new int[vertices];
        d = new int[vertices];
        f = new int[vertices];
        parent = new int[vertices];
        countLabel = 0;
        label = new int[vertices];
        cc = new int[vertices];
        countCC = 0;
        boolean loop = false;

        for (int v = 0; v < vertices; v++) {
            d[v] = f[v] = parent[v] = -1;
        }

        // percorre todos os vértices do grafo
        for (int v = 0; v < vertices; v++) {
            // verifica se vértice v já foi visitado
            if (d[v] == -1) {
                // se não foi visitado...
                // marca pai como ele mesmo (inicio de uma árvore)
                parent[v] = v;
                // faz a busca por profundidade partindo do vértice v
                if (depthSearchCompleteR(v)) {
                    loop = true;
                }
                countCC++;
            }
        }

        depthSearchCC();

        return loop;
    }

    /**
     * Faz a busca por profundidade recursivamente do grafo inteiro partindo do vértice v
     *
     * @param v: vértice a ser visitado
     * @return true, se houver ciclo; false, se não houver
     */
    protected abstract boolean depthSearchCompleteR(int v);

    public abstract void depthSearchCC();

    protected abstract void depthSearchCCR(int v);

    protected int[] depthSearchTransposed(int[] normalF) {
        // inicializa todas as variáveis e vetores
        cc = new int[vertices];
        countCC = 0;

        for (int v = 0; v < vertices; v++) {
            cc[v] = -1;
        }

        SortedMap<Integer, Integer> sortedMap = new TreeMap<>(Collections.reverseOrder());
        for (int v = 0; v < vertices; v++) {
            sortedMap.put(normalF[v], v);
        }

        // percorre todos os vértices do grafo, em ordem decrescente fo seu respectivo normalF
        for (Map.Entry<Integer, Integer> entry : sortedMap.entrySet()) {
            int v = entry.getValue();

            // verifica se vértice v já foi visitado
            if (cc[v] == -1) {
                // se não foi visitado...
                // faz a busca por profundidade partindo do vértice v
                depthSearchCCR(v);
                countCC++;
            }
        }

        return cc;
    }
}
