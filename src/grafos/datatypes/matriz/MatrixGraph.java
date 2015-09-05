package grafos.datatypes.matriz;

/**
 * Representa um Grafo usando Matriz de Adjacência
 * Created by dfcarvalho on 9/5/15.
 */
public class MatrixGraph extends MatrixDigraph {
    public MatrixGraph(int vertices) {
        super(vertices);
    }

    /**
     * Insere um arco que se inicia em V e termina em W
     * @param v: vértice inicial
     * @param w: vértice final
     */
    @Override
    public void insertArc(int v, int w) {
        super.insertArc(v, w);
        super.insertArc(w, v);
    }

    /**
     * Remove um arco que se inicia em V e termina em W
     * @param v: vértice inicial
     * @param w: vértice final
     */
    @Override
    public void removeArc(int v, int w) {
        super.removeArc(v, w);
        super.removeArc(w, v);
    }
}
