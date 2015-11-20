package grafos.datatypes.matriz;

import static grafos.Constants.*;

/**
 * Created by dfcarvalho on 11/19/15.
 */
public class MatrixGraphCost extends MatrixDigraphCost {
    public MatrixGraphCost(int vertices) {
        super(vertices);
    }

    @Override
    public int insertArc(int v, int w, int cost) {
        // insere ida
        int result = super.insertArc(v, w, cost);

        if (result == RESULT_OK) {
            // insere volta
            result = super.insertArc(w, v, cost);

            if (result != RESULT_OK) {
                // remove ida em caso de erro
                super.removeArc(v, w);
            }
        }

        return result;
    }

    @Override
    public int removeArc(int v, int w) {
        int cost = adjMatrix[v][w];

        // remove ida
        int result = super.removeArc(v, w);

        if (result == RESULT_OK) {
            // remove volta
            result = super.removeArc(w, v);

            if (result != RESULT_OK) {
                // re-insere ida em caso de erro
                super.insertArc(v, w, cost);
            }
        }

        return result;
    }
}
