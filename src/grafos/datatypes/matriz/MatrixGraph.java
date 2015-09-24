package grafos.datatypes.matriz;

import static grafos.Constants.*;

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
    public int insertArc(int v, int w) {
        // insere ida
        int result = super.insertArc(v, w);

        if (result == RESULT_OK) {
            // insere volta
            result = super.insertArc(w, v);

            if (result != RESULT_OK) {
                // remove ida em caso de erro
                super.removeArc(v, w);
            }
        }

        return result;
    }

    /**
     * Remove um arco que se inicia em V e termina em W
     * @param v: vértice inicial
     * @param w: vértice final
     */
    @Override
    public int removeArc(int v, int w) {
        // remove ida
        int result = super.removeArc(v, w);

        if (result == RESULT_OK) {
            result = super.removeArc(w, v);

            if (result != RESULT_OK) {
                // re-insere ida em caso de erro
                super.insertArc(v, w);
            }
        }

        return result;
    }
}
