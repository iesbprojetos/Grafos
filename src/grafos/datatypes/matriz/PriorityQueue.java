package grafos.datatypes.matriz;

/**
 * Created by dfcarvalho on 11/18/15.
 */
public class PriorityQueue {
    private int[] pq;
    private int zeroValue;

    /**
     * Cria fila de prioridades
     * @param size: tamanho da fial
     * @param zeroValue: valor inicial de cada elemento
     */
    public PriorityQueue(int size, int zeroValue) {
        pq = new int[size];
        this.zeroValue = zeroValue;

        for (int v = 0; v < size; v++) {
            pq[v] = zeroValue;
        }
    }

    /**
     * Insere elemento V com prioridade priority
     * @param v índice do elemento a ser inserido
     * @param priority prioridade do elemento
     */
    public void insert(int v, int priority) {
        pq[v] = priority;
    }

    /**
     * Remove elemento com maior prioridade (menor valor)
     * @return índice do elemento com maior prioridade (menor valor)
     */
    public int removeHighestPriority() {
        int vMin = -1;
        int min = zeroValue;

        for (int v = 0; v < pq.length; v++) {
            if (pq[v] < min) {
                min = pq[v];
                vMin = v;
            }
        }

        pq[vMin] = zeroValue;

        return vMin;
    }

    /**
     * Altera valor de prioridade de um elemento
     * @param v índice do elemento a ser alterado
     * @param newPriority novo valor de prioridade
     */
    public void setValue(int v, int newPriority) {
        pq[v] = newPriority;
    }

    /**
     * Verifica se fila está vazia
     * @return verdadeiro se estiver vazia, false se conter elementos
     */
    public boolean isEmpty() {
        for (int v = 0; v < pq.length; v++) {
            if (pq[v] != zeroValue) {
                return false;
            }
        }

        return true;
    }
}
