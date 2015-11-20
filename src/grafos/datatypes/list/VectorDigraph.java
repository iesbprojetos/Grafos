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
    protected List<LinkedList<VectorElement>> adjVector;

    public VectorDigraph(int vertices) {
        super(vertices);

        adjVector = new ArrayList<>(vertices);
        for (int i = 0; i < vertices; i++) {
            adjVector.add(new LinkedList<>());
        }
    }

    public List<LinkedList<VectorElement>> getAdjVector() {
        return adjVector;
    }

    protected void setAdjVector(List<LinkedList<VectorElement>> adjVector) {
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

        for (VectorElement adjVertex : adjVector.get(v)) {
            if (adjVertex.getW() == w) {
                // arco já existe
                return RESULT_ARC_ALREADY_EXISTS;
            }
        }

        // senão, insere arco
        VectorElement adjVertex = new VectorElement(w, 1);
        adjVector.get(v).push(adjVertex);
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
        VectorElement adjVertex = new VectorElement(w, 1);
        if (!adjVector.get(v).remove(adjVertex)) {
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
        LinkedList<VectorElement> vector = adjVector.get(v);
        for (VectorElement adjVertex : vector) {
            int w = adjVertex.getW();
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
        LinkedList<VectorElement> vector = adjVector.get(v);
        for (VectorElement adjVertex : vector) {
            int w = adjVertex.getW();
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

        List<LinkedList<VectorElement>> tAdjVector = tGraph.getAdjVector();
        for (int v = 0; v < vertices; v++) {
            for (VectorElement adjVertex : adjVector.get(v)) {
                int w = adjVertex.getW();
                VectorElement adjVertexV = new VectorElement(v, adjVertex.getCost());
                tAdjVector.get(w).add(adjVertexV);
            }
        }

        tGraph.setAdjVector(tAdjVector);

        return tGraph;
    }

    @Override
    protected void depthSearchCCR(int v) {
        cc[v] = countCC;

        for (VectorElement adjVertex : adjVector.get(v)) {
            int w = adjVertex.getW();
            if (cc[w] == -1) {
                depthSearchCCR(w);
            }
        }
    }

    @Override
    public void breadthSearch(int s) {
        label = new int[vertices];
        countLabel = 0;
        parent = new int[vertices];

        for (int v = 0; v < vertices; v++) {
            label[v] = -1;
            parent[v] = -1;
        }

        List<Integer> queue = new ArrayList<>();

        label[s] = countLabel++;
        parent[s] = s;
        queue.add(s);

        while (queue.size() > 0) {
            int v = queue.remove(0);

            for (VectorElement adjVertex : adjVector.get(v)) {
                int w = adjVertex.getW();

                if (label[w] == -1) {
                    label[w] = countLabel++;
                    parent[w] = v;
                    queue.add(w);
                }
            }
        }
    }
}
