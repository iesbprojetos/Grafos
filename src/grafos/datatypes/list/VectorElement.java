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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VectorElement that = (VectorElement) o;

        return w == that.w;

    }

    @Override
    public int hashCode() {
        return w;
    }
}
