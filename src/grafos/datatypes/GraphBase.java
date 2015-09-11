package grafos.datatypes;

/**
 * Interface para servir de base para os Grafos/Digrafos
 */
public abstract class GraphBase {
    /**
     * Número de vértices
     * Inicializado no construtor através de argumento
     */
    protected int vertices;

    /**
     * Número de arcos
     * Inicializado no construtor com valor 0
     */
    protected int arcs;

    protected int[] parent;
    protected int time;
    protected int[] d;
    protected int[] f;
    protected int[] topologicalSort;
    protected int tsCount;
    protected int[] label;
    protected int countLabel;

    /**
     * Construtor
     * @param vertices: número de vértices do grafo
     */
    public GraphBase(int vertices) {
        this.vertices = vertices;
        this.arcs = 0;
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

    /**
     * Insere um arco que se inicia em V e termina em W
     * @param v: vértice inicial
     * @param w: vértice final
     */
    public abstract void insertArc(int v, int w);

    /**
     * Remove um arco que se inicia em V e termina em W
     * @param v: vértice inicial
     * @param w: vértice final
     */
    public abstract void removeArc(int v, int w);
}
