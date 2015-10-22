package grafos.datatypes.list;

import static grafos.Constants.*;
import grafos.datatypes.GraphBase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dfcarvalho on 9/10/15.
 */
public class VectorDigraph extends GraphBase {
    List<LinkedList<Integer>> adjVector = new ArrayList<LinkedList<Integer>>();

    public VectorDigraph(int vertices) {
        super(vertices);

        adjVector = new ArrayList<>(vertices);
        for (int i = 0; i < vertices; i++) {
            adjVector.add(new LinkedList<Integer>());
        }
    }

    public List<LinkedList<Integer>> getAdjVector() {
        return adjVector;
    }

    protected void setAdjVector(List<LinkedList<Integer>> adjVector) {
        this.adjVector = adjVector;
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
        if (adjVector.get(v) == null) {
            adjVector.add(v, new LinkedList<Integer>());
        }

        for (int adjVertex : adjVector.get(v)) {
            if (adjVertex == w) {
                // arco já existe
                return RESULT_ARC_ALREADY_EXISTS;
            }
        }

        // senão, insere arco
        adjVector.get(v).push(w);
        arcs++;
        return RESULT_OK;
    }

    @Override
    public int removeArc(int v, int w) {
        // verifica se vértices são válidos
        if (v >= vertices || w >= vertices || v == w) {
            return RESULT_INVALID_VERTEX;
        }

        // verifica se arco existe
        if (adjVector.get(v) == null) {
            return RESULT_ARC_NOT_FOUND;
        }

        // remove arco (se existir)
        if (!adjVector.get(v).remove(new Integer(w))) {
            // arco não existe
            return RESULT_ARC_NOT_FOUND;
        }

        // arco removido
        arcs--;
        return RESULT_OK;
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

        // percorre vetor de adjacência do vetor v
        LinkedList<Integer> vector = adjVector.get(v);
        for (Integer w : vector) {
            if (d[w] == -1) {
                parent[w] = v;
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
    @Override
    protected boolean depthSearchCompleteR(int v) {
        // marca ordem na qual o vértice v foi visitado
        label[v] = countLabel++;
        // marca inicio da visita ao vetor v
        d[v] = time++;

        boolean loop = false;

        // percorre o vetor de adjacência do v
        LinkedList<Integer> vector = adjVector.get(v);
        for (Integer w : vector) {
            // verifica se vértice w já foi visitado
            if (d[w] == -1) {
                // vértice w não foi visitado...
                // marca vértice v como pai do vértice w
                parent[w] = v;

                // visita vértice w recursivamente
                if (depthSearchCompleteR(w)) {
                    loop = true;
                }
            } else if (f[w] == -1) {
                // visita ao vértice w não finalizou, então existe um ciclo
                loop = true;
            }
        }

        // marca fim da visita ao vértice v
        f[v] = time++;
        // adiciona vértice v ao vetor de ordem topológica
        topologicalSort[tsCount--] = v;

        return loop;
    }

    @Override
    public void depthSearchCC() {
        VectorDigraph tGraph = this.getTransposedGraph();
        cc = tGraph.depthSearchTransposed(f);
    }

    protected VectorDigraph getTransposedGraph() {
        VectorDigraph tGraph = new VectorDigraph(vertices);

        List<LinkedList<Integer>> tAdjVector = tGraph.getAdjVector();
        for (int v = 0; v < vertices; v++) {
            for (Integer w : adjVector.get(v)) {
                tAdjVector.get(w).add(v);
            }
        }

        tGraph.setAdjVector(tAdjVector);

        return tGraph;
    }

    @Override
    protected void depthSearchCCR(int v) {
        cc[v] = countCC;

        for (Integer w : adjVector.get(v)) {
            if (cc[w] == -1) {
                depthSearchCCR(w);
            }
        }
    }
}
