package grafos.datatypes.list;

/**
 * Created by dfcarvalho on 11/16/15.
 */
public class VectorElement {
    private int w;
    private int cost;

    public VectorElement(int w, int cost) {
        this.w = w;
        this.cost = cost;
    }

    public int getW() {
        return w;
    }

    public int getCost() {
        return cost;
    }
}
