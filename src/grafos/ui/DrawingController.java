package grafos.ui;

import static grafos.Constants.*;
import grafos.datatypes.matriz.MatrixDigraph;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by dfcarvalho on 8/21/15.
 */
public class DrawingController implements Initializable {
    private static final int DIAMETER = 40;
    private static final int OFFSET_X = 5;
    private static final int OFFSET_Y = 5;
    private static final int ARROW_SIZE = 25;

    private static final int CANVAS_WIDTH = 500;
    private static final int CANVAS_HEIGHT = 500;

    private int mode;

    @FXML
    private BorderPane root;
    private ResizableCanvas canvas;
    @FXML
    private Button btnCreate;
    @FXML
    private Button btnAddArc;
    @FXML
    private Button btnRemoveArc;
    @FXML
    private Button btnFindPath;
    @FXML
    private Button btnDepthSearch;
    @FXML
    private TableView<VertexTableRow> tableView;

    private List<Button> buttons = null;

    private double scaleX;
    private double scaleY;

    private int graphType;
    private int algoType;

    private MatrixDigraph graph;

    private Integer selectedVertex;
    private Point2D tempArcStart;
    private Point2D tempArcEnd;
    private List<Integer> path;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createCanvas();
        mode = 0;
        selectedVertex = null;
        buttons = new ArrayList<>(Arrays.asList(btnCreate, btnAddArc, btnRemoveArc, btnFindPath, btnDepthSearch));
    }

    public void createGraph(int v) {
        switch (algoType) {
            case ALGO_MATRIX:
                graph = new MatrixDigraph(v);
                break;
            case ALGO_LIST:
                // TODO:
                break;
        }

        tableView.setItems(null);
    }

    public void insertArc(int v, int w) {
        graph.insertArc(v, w);
    }

    public void removeArc(int v, int w) {
        graph.removeArc(v, w);
    }

    @FXML
    protected void handleCreateButtonAction(ActionEvent event) {
        Button source = (Button) event.getSource();
        for (Button button : buttons) {
            if (button.equals(source)) {
                button.getStyleClass().clear();
                button.getStyleClass().add("selectedButton");
            } else {
                button.getStyleClass().clear();
                button.getStyleClass().add("button");
            }
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/new_graph.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            NewGraphController controller = fxmlLoader.getController();
            controller.setDrawingController(this);

            Scene scene = new Scene(root);

            Stage dialog = new Stage(StageStyle.UTILITY);
            dialog.setTitle("Novo Grafo");
            dialog.setScene(scene);
            dialog.sizeToScene();
            dialog.show();
        } catch (IOException e) {
            // TODO:
        }
    }

    @FXML
    protected void handleInsertButtonAction(ActionEvent event) {
        Button source = (Button) event.getSource();
        for (Button button : buttons) {
            if (button.equals(source)) {
                button.getStyleClass().clear();
                button.getStyleClass().add("selectedButton");
            } else {
                button.getStyleClass().clear();
                button.getStyleClass().add("button");
            }
        }

        mode = MODE_INSERT_ARC;
    }

    @FXML
    protected void handleRemoveButtonAction(ActionEvent event) {
        // TODO: using mouse
        Button source = (Button) event.getSource();
        for (Button button : buttons) {
            if (button.equals(source)) {
                button.getStyleClass().clear();
                button.getStyleClass().add("selectedButton");
            } else {
                button.getStyleClass().clear();
                button.getStyleClass().add("button");
            }
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/edit_arc.fxml"));
            RemoveArcController controller = new RemoveArcController();
            controller.setDrawingController(this);
            fxmlLoader.setController(controller);

            Parent root = (Parent) fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage dialog = new Stage(StageStyle.UTILITY);
            dialog.setTitle("Remover Arco");
            dialog.setScene(scene);
            dialog.sizeToScene();
            dialog.show();
        } catch (IOException e) {
            // TODO:
        }
    }

    @FXML
    protected void handleFindPathButtonAction(ActionEvent event) {
        Button source = (Button) event.getSource();
        for (Button button : buttons) {
            if (button.equals(source)) {
                button.getStyleClass().clear();
                button.getStyleClass().add("selectedButton");
            } else {
                button.getStyleClass().clear();
                button.getStyleClass().add("button");
            }
        }

        mode = MODE_FIND_PATH;
    }

    @FXML
    protected void handleDepthSearchButtonAction(ActionEvent event) {
        /*
        Button source = (Button) event.getSource();
        for (Button button : buttons) {
            if (button.equals(source)) {
                button.getStyleClass().clear();
                button.getStyleClass().add("selectedButton");
            } else {
                button.getStyleClass().clear();
                button.getStyleClass().add("button");
            }
        }
        */

        if (graph != null) {
            boolean acyclic = !graph.digraphCycle();
            if (acyclic) {
                // TODO: no cycle, show topological sort result
            } else {
                // TODO: has cycle, show msg
            }

            // TODO: fill table view

            ObservableList<VertexTableRow> data = FXCollections.observableArrayList();

            for (int v = 0; v < graph.getVertices(); v++) {
                int parent = graph.getParent() != null ? graph.getParent()[v] : -1;
                int label = graph.getLabel() != null ? graph.getLabel()[v] : -1;
                int timeD = graph.getD() != null ? graph.getD()[v] : -1;
                int timeF = graph.getF() != null ? graph.getF()[v] : -1;
                int ts = -1;
                if (acyclic) {
                   ts = graph.getTopologicalSort() != null ? graph.getTopologicalSort()[v] : -1;
                }

                VertexTableRow row = new VertexTableRow(v, label, parent, timeD, timeF, ts);
                data.add(row);
            }

            tableView.setItems(data);
        }
    }

    private void createCanvas() {
        canvas = new ResizableCanvas(CANVAS_WIDTH, CANVAS_HEIGHT, listenerCanvasSize);
        canvas.addEventHandler(MouseEvent.ANY, new CanvasClickListener());

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(canvas);

        canvas.widthProperty().bind(stackPane.widthProperty());
        canvas.heightProperty().bind(stackPane.heightProperty());

        root.setCenter(stackPane);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                drawCanvas();
            }
        };
        timer.start();
    }

    public void drawCanvas() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setTransform(new Affine());

        scaleX = width / CANVAS_WIDTH;
        scaleY = height / CANVAS_HEIGHT;

        gc.scale(scaleX, scaleY);

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        // draw temporary arc (mouse dragging)
        if (tempArcStart != null && tempArcEnd != null) {
            drawArc(gc, tempArcStart, tempArcEnd, ARC_TYPE_TREE);
        }

        if (graph != null) {
            int[][] adjMatrix = graph.getAdjMatrix();

            for (int v = 0; v < graph.getVertices(); v++) {
                // draw arcs from this vertex to all others
                for (int w = 0; w < graph.getVertices(); w++) {
                    if (adjMatrix[v][w] == 1) {
                        drawArc(gc, v, w, graph.getVertices());
                    }
                }
                // draw vertex circle
                boolean isSelected = (selectedVertex != null && v == selectedVertex);
                drawVertex(gc, v, graph.getVertices(), isSelected);
            }
        }

        gc.scale(1 / scaleX, 1 / scaleY);
    }

    private void drawVertex(GraphicsContext gc, int v, int numV, boolean isSelected) {
        double centerX = vertexCenterX(v, numV);
        double centerY = vertexCenterY(v, numV);
        double x = centerX - DIAMETER / scaleX / 2;
        double y = centerY - DIAMETER / scaleY / 2;

        if (isSelected) {
            gc.setFill(Color.RED);
        } else {
            gc.setFill(Color.GREEN);
        }
        gc.setLineWidth(1);
        gc.fillOval(x, y, DIAMETER / scaleX, DIAMETER / scaleY);

        gc.setStroke(Color.WHITE);
        gc.strokeText(String.valueOf(v), centerX - OFFSET_X, centerY + OFFSET_Y);
    }

    private void drawArc(GraphicsContext gc, int v, int w, int numV) {
        double startX = vertexCenterX(v, numV);
        double startY = vertexCenterY(v, numV);
        double endX = vertexCenterX(w, numV);
        double endY = vertexCenterY(w, numV);

        Point2D start = new Point2D(startX, startY);
        Point2D end = new Point2D(endX, endY);

        int arcType =  ARC_TYPE_TREE;
        int[] parent = graph.getParent();
        int[] d = graph.getD();
        int[] f = graph.getF();
        if (parent != null && d != null && f != null) {
            if (d[v] < d[w] && d[w] < f[w] && f[w] < f[v]) {
                if (parent[w] == v) {
                    arcType = ARC_TYPE_TREE;
                } else {
                    arcType = ARC_TYPE_DESC;
                }
            } else if (d[w] < d[v] && d[v] < f[v] && f[v] < f[w]) {
                arcType = ARC_TYPE_RTRN;
            } else if (d[w] < f[w] && f[w] < d[v] && d[v] < f[v]) {
                arcType = ARC_TYPE_CRSS;
            }
        }

        if (path != null) {
            int indexStart = path.indexOf(v);
            if (indexStart >= 0 && indexStart+1 < path.size()) {
                int nextV = path.get(indexStart+1);

                if (nextV == w) {
                    arcType = ARC_TYPE_TEMP;
                }
            }
        }

        drawArc(gc, start, end, arcType);
    }

    private void drawArc(GraphicsContext gc, Point2D start, Point2D end, int arcType) {
        double angle = Math.toDegrees(Math.atan2(end.getY() - start.getY(), end.getX() - start.getX()));

        double arrowEndX = end.getX() + DIAMETER / 2 * Math.cos(Math.toRadians(180 + angle));
        double arrowEndY = end.getY() + DIAMETER / 2 * Math.sin(Math.toRadians(180 + angle));

        switch (arcType) {
            case ARC_TYPE_TEMP:
                gc.setStroke(Color.BEIGE);
                break;
            case ARC_TYPE_TREE:
                gc.setStroke(Color.BLACK);
                break;
            case ARC_TYPE_DESC:
                gc.setStroke(Color.PURPLE);
                break;
            case ARC_TYPE_CRSS:
                gc.setStroke(Color.BLUE);
                break;
            case ARC_TYPE_RTRN:
                gc.setStroke(Color.GREEN);
                break;
            case ARC_TYPE_PATH:
                gc.setStroke(Color.RED);
                break;
        }

        gc.setLineWidth(3);
        gc.strokeLine(start.getX(), start.getY(), arrowEndX, arrowEndY);

        // arrow
        if (graphType == TYPE_DIGRAPH) {
            Image arrow = new Image("/res/arrow_head.png", ARROW_SIZE, ARROW_SIZE, false, true);

            gc.translate(arrowEndX, arrowEndY);
            gc.rotate(angle);
            gc.drawImage(arrow, -ARROW_SIZE, -ARROW_SIZE / 2);
            gc.rotate(-angle);
            gc.translate(-arrowEndX, -arrowEndY);
        }
    }

    private double vertexCenterX(int v, int numV) {
        double diffAngle = 360 / (double) numV;
        double angle = v * diffAngle - 90;

        return CANVAS_WIDTH / 2 + (CANVAS_WIDTH / 2 - DIAMETER * 2) * Math.cos(Math.toRadians(angle));
    }

    private double vertexCenterY(int v, int numV) {
        double diffAngle = 360 / (double) numV;
        double angle = v * diffAngle - 90;

        return CANVAS_HEIGHT / 2 + (CANVAS_HEIGHT / 2 - DIAMETER * 2) * Math.sin(Math.toRadians(angle));
    }

    public void setGraphType(int graphType) {
        this.graphType = graphType;
    }

    public void setAlgoType(int algoType) {
        this.algoType = algoType;
    }

    private ChangeListener<Number> listenerCanvasSize = new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
            // drawCanvas();
        }
    };

    private class CanvasClickListener implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            switch (mode) {
                case MODE_INSERT_ARC:
                    handleInsertMode(mouseEvent);
                    break;
                case MODE_REMOVE_ARC:
                    // TODO:
                case MODE_FIND_PATH:
                    handleFindPath(mouseEvent);
                    break;
            }
        }

        private void handleInsertMode(MouseEvent mouseEvent) {
            double mouseX = mouseEvent.getX() / scaleX;
            double mouseY = mouseEvent.getY() / scaleY;

            // Coordinates mouseEvent.getX() and mouseEvent.getY() are relative to canvas.
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
                Integer clickedVertex = vertexOnPosition(mouseX, mouseY);
                if (clickedVertex != null) {
                    System.out.println("Clicked Vertex: " + clickedVertex);
                }
            } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
                Integer clickedVertex = vertexOnPosition(mouseX, mouseY);
                if (clickedVertex != null) {
                    if (graph != null) {
                        int numV = graph.getVertices();
                        tempArcStart = new Point2D(vertexCenterX(clickedVertex, numV),
                                vertexCenterY(clickedVertex, numV));
                        selectedVertex = clickedVertex;
                    }
                    System.out.println("Pressed Vertex: " + clickedVertex);
                }
            } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                if (graph != null) {
                    tempArcEnd = new Point2D(mouseX, mouseY);
                }
            } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
                Integer releasedVertex = vertexOnPosition(mouseX, mouseY);
                if (releasedVertex != null && selectedVertex != releasedVertex) {
                    if (graph != null) {
                        insertArc(selectedVertex, releasedVertex);
                    }

                    System.out.println("Released Vertex: " + releasedVertex);
                }

                tempArcStart = null;
                tempArcEnd = null;
                selectedVertex = null;
            }
        }

        private void handleFindPath(MouseEvent mouseEvent) {
            double mouseX = mouseEvent.getX() / scaleX;
            double mouseY = mouseEvent.getY() / scaleY;

            if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
                if (graph != null) {
                    if (selectedVertex == null) {
                        // selecionar primeiro vértice
                        path = null;
                        selectedVertex = vertexOnPosition(mouseX, mouseY);
                    } else {
                        Integer secondVertex = vertexOnPosition(mouseX, mouseY);
                        if (secondVertex != null) {
                            path = graph.getArrayPath(selectedVertex, secondVertex);
                        } else {
                            path = null;
                        }

                        selectedVertex = null;

                        if (path == null) {
                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/alert_dialog.fxml"));
                                Parent root = (Parent) fxmlLoader.load();

                                AlertDialogController controller = fxmlLoader.getController();
                                controller.setMessage(ERROR_MSG_NO_PATH);

                                Scene scene = new Scene(root);

                                Stage dialog = new Stage(StageStyle.UTILITY);
                                dialog.initModality(Modality.WINDOW_MODAL);
                                dialog.setTitle("Alerta");
                                dialog.setScene(scene);
                                dialog.sizeToScene();
                                dialog.show();
                            } catch (IOException e) {
                                // TODO:
                            }
                        }
                    }
                }
            }
        }

        private Integer vertexOnPosition(double clickX, double clickY) {
            if (graph != null) {
                int numV = graph.getVertices();
                for (int v = 0; v < graph.getVertices(); v++) {
                    double centerX = vertexCenterX(v, numV);
                    double centerY = vertexCenterY(v, numV);
                    double startX = centerX - DIAMETER / scaleX / 2;
                    double startY = centerY - DIAMETER / scaleY / 2;
                    double endX = centerX + DIAMETER / scaleX / 2;
                    double endY = centerY + DIAMETER / scaleY / 2;

                    if (clickX >= startX && clickX <= endX &&
                            clickY >= startY && clickY <= endY) {
                        return v;
                    }
                }
            }

            return null;
        }
    }
}
