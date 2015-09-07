package grafos.ui;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * Created by dfcarvalho on 9/6/15.
 */
public class VertexTableRow {
    private SimpleIntegerProperty vertex;
    private SimpleIntegerProperty label;
    private SimpleIntegerProperty parent;
    private SimpleIntegerProperty timeD;
    private SimpleIntegerProperty timeF;
    private SimpleIntegerProperty ts;

    public VertexTableRow() {
    }

    public VertexTableRow(int vertex, int label, int parent, int timeD, int timeF, int ts) {
        this.vertex = new SimpleIntegerProperty(vertex);
        this.label = new SimpleIntegerProperty(label);
        this.parent = new SimpleIntegerProperty(parent);
        this.timeD = new SimpleIntegerProperty(timeD);
        this.timeF = new SimpleIntegerProperty(timeF);
        this.ts = new SimpleIntegerProperty(ts);
    }

    public int getVertex() {
        return vertex.get();
    }

    public SimpleIntegerProperty vertexProperty() {
        return vertex;
    }

    public void setVertex(int vertex) {
        this.vertex.set(vertex);
    }

    public int getLabel() {
        return label.get();
    }

    public SimpleIntegerProperty labelProperty() {
        return label;
    }

    public void setLabel(int label) {
        this.label.set(label);
    }

    public int getParent() {
        return parent.get();
    }

    public SimpleIntegerProperty parentProperty() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent.set(parent);
    }

    public int getTimeD() {
        return timeD.get();
    }

    public SimpleIntegerProperty timeDProperty() {
        return timeD;
    }

    public void setTimeD(int timeD) {
        this.timeD.set(timeD);
    }

    public int getTimeF() {
        return timeF.get();
    }

    public SimpleIntegerProperty timeFProperty() {
        return timeF;
    }

    public void setTimeF(int timeF) {
        this.timeF.set(timeF);
    }

    public int getTs() {
        return ts.get();
    }

    public SimpleIntegerProperty tsProperty() {
        return ts;
    }

    public void setTs(int ts) {
        this.ts.set(ts);
    }
}
