package grafos.datatypes.list;

import static grafos.Constants.*;

/**
 * Created by dfcarvalho on 9/18/15.
 */
public class VectorGraph extends VectorDigraph {
    public VectorGraph(int vertices) {
        super(vertices);
    }

    @Override
    public int insertArc(int v, int w) {
        // insere ida
        int result = super.insertArc(v, w);

        if (result == RESULT_OK) {
            // insere volta
            result = super.insertArc(v, w);

            if (result != RESULT_OK) {
                // remove ida em caso de erro
                super.removeArc(v, w);
            }
        }

        return result;
    }

    @Override
    public int removeArc(int v, int w) {
        // remove ida
        int result = super.removeArc(v, w);

        if (result == RESULT_OK) {
            // remove volta
            result = super.removeArc(w, v);

            if (result != RESULT_OK) {
                // re-insere ida em caso de erro
                super.insertArc(v, w);
            }
        }

        return result;
    }
}
