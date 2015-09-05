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
}
