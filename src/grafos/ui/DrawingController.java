package grafos.ui;

import static grafos.Constants.*;

import grafos.datatypes.CostSetListener;
import grafos.datatypes.GraphBase;
import grafos.datatypes.list.*;
import grafos.datatypes.matriz.MatrixDigraph;
import grafos.datatypes.matriz.MatrixDigraphCost;
import grafos.datatypes.matriz.MatrixGraph;
import grafos.datatypes.matriz.MatrixGraphCost;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
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
import java.util.*;

/**
 * Created by dfcarvalho on 8/21/15.
 */
public class DrawingController implements Initializable, CostSetListener {
    private static final int DIAMETER = 40;
    private static final int OFFSET_X = 5;
    private static final int OFFSET_Y = 5;
    private static final int ARROW_SIZE = 25;

    private static final int CANVAS_WIDTH = 500;
    private static final int CANVAS_HEIGHT = 500;

    private enum Mode {
        CREATE_GRAPH,
        INSERT_ARC,
        REMOVE_ARC,
        DEPTH_SEARCH,
        FIND_PATH_DFS,
        BREADTH_SEARCH,
        DAGMIN_SPT,
        DIJKSTRA_SPT,
        BELLMAN_FORD_SPT,
        BF_SENTINEL_SPT,
        FLOYD_WARSHAW
    }

    private Mode mode;

    @FXML
    private BorderPane root;
    private ResizableCanvas canvas;
    @FXML
    private Label labelMode;
    @FXML
    private Label labelInstruction;
    @FXML
    private Button btnCreate;
    @FXML
    private Button btnAddArc;
    @FXML
    private Button btnRemoveArc;
    @FXML
    private Button btnDepthSearch;
    @FXML
    private Button btnFindPath;
    @FXML
    private Button btnBreadthSearch;
    @FXML
    private Button btnDAGmin;
    @FXML
    private Button btnDijkstra;
    @FXML
    private Button btnBellmanFord2;
    @FXML
    private Button btnBellmanFordSentinel;
    @FXML
    private Button btnFloydWarshall;
    @FXML
    private TableView<VertexTableRow> tableView;
    @FXML
    private CheckBox cbTree;
    @FXML
    private CheckBox cbReturn;
    @FXML
    private CheckBox cbDescendant;
    @FXML
    private CheckBox cbCross;

    private List<Button> buttons = null;

    private double scaleX;
    private double scaleY;

    private int graphType;
    private int costType;
    private int algoType;

    private GraphBase graph;

    private Integer selectedVertex;
    private Point2D tempArcStart;
    private Point2D tempArcEnd;
    private List<Integer> path;

    private Color[] vertexColors = new Color[]{
            Color.DARKRED, Color.DARKGREEN, Color.DARKBLUE,
            Color.DARKVIOLET, Color.MEDIUMSPRINGGREEN, Color.BLUE,
            Color.BURLYWOOD, Color.MEDIUMVIOLETRED, Color.DARKORANGE
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createCanvas();
        switchMode(Mode.CREATE_GRAPH);
        selectedVertex = null;
        buttons = new ArrayList<>(
                Arrays.asList(btnCreate, btnAddArc, btnRemoveArc,
                        btnDepthSearch, btnFindPath, btnBreadthSearch,
                        btnDAGmin, btnDijkstra, btnBellmanFord2,
                        btnBellmanFordSentinel, btnFloydWarshall)
        );
    }

    private void switchMode(Mode newMode) {
        if (newMode != mode) {
            mode = newMode;
            selectedVertex = null;
            path = null;

            switch (newMode) {
                case CREATE_GRAPH:
                    labelMode.setText(MODE_NAME_CREATE_GRAPH);
                    labelInstruction.setText(MODE_INST_CREATE_GRAPH);
                    break;
                case INSERT_ARC:
                    labelMode.setText(MODE_NAME_INSERT_ARC);
                    labelInstruction.setText(MODE_INST_INSERT_ARC);
                    break;
                case REMOVE_ARC:
                    labelMode.setText(MODE_NAME_REMOVE_ARC);
                    labelInstruction.setText("");
                    break;
                case DEPTH_SEARCH:
                    labelMode.setText(MODE_NAME_DEPTH_SEARCH);
                    labelInstruction.setText(MODE_INST_DEPTH_SEARCH);
                    break;
                case FIND_PATH_DFS:
                    labelMode.setText(MODE_NAME_FIND_PATH);
                    labelInstruction.setText(MODE_INST_FIND_PATH);
                    break;
                case BREADTH_SEARCH:
                    labelMode.setText(MODE_NAME_BREADTH_SEARCH);
                    labelInstruction.setText(MODE_INST_BREADTH_SEARCH);
                    break;
                case DAGMIN_SPT:
                    labelMode.setText(MODE_NAME_DAGMIN);
                    labelInstruction.setText(MODE_INST_SPT);
                    break;
                case DIJKSTRA_SPT:
                    labelMode.setText(MODE_NAME_DIJKSTRA);
                    labelInstruction.setText(MODE_INST_SPT);
                    break;
                case BELLMAN_FORD_SPT:
                    labelMode.setText(MODE_NAME_BELLMANFORD);
                    labelInstruction.setText(MODE_INST_SPT);
                    break;
                case BF_SENTINEL_SPT:
                    labelMode.setText(MODE_NAME_BF_SENTINEL);
                    labelMode.setText(MODE_INST_SPT);
                    break;
                case FLOYD_WARSHAW:
                    labelMode.setText(MODE_NAME_FLOYDWARSHALL);
                    labelInstruction.setText(MODE_INST_FLOYDWARSHALL);
                    break;
            }
        }
    }

    public void createGraph(int v) {
        switch (algoType) {
            case ALGO_MATRIX:
                if (graphType == TYPE_DIGRAPH) {
                    if (costType == COST_NO) {
                        graph = new MatrixDigraph(v);
                    } else if (costType == COST_YES) {
                        graph = new MatrixDigraphCost(v);
                    }
                } else if (graphType == TYPE_GRAPH) {
                    if (costType == COST_NO) {
                        graph = new MatrixGraph(v);
                    } else if (costType == COST_YES) {
                        graph = new MatrixGraphCost(v);
                    }
                }
                break;
            case ALGO_LIST:
                if (graphType == TYPE_DIGRAPH) {
                    if (costType == COST_NO) {
                        graph = new VectorDigraph(v);
                    } else if (costType == COST_YES) {
                        graph = new VectorDigraphCost(v);
                    }
                } else if (graphType == TYPE_GRAPH) {
                    if (costType == COST_NO) {
                        graph = new VectorGraph(v);
                    } else if (costType == COST_YES) {
                        graph = new VectorGraphCost(v);
                    }
                }
                break;
        }

        tableView.setItems(null);

        // go to insert arc mode, if graph created
        if (graph != null) {
            selectButton(btnAddArc);
            switchMode(Mode.INSERT_ARC);
        }
    }

    public void insertArc(int v, int w, int cost) {
        int result;

        if (cost == 1) {
            result = graph.insertArc(v, w);
        } else {
            if (graph instanceof MatrixDigraphCost) {
                result = ((MatrixDigraphCost) graph).insertArc(v, w, cost);
            } else if (graph instanceof VectorDigraphCost) {
                result = ((VectorDigraphCost) graph).insertArc(v, w, cost);
            } else {
                result = RESULT_UNKNOWN;
            }
        }

        if (result != RESULT_OK) {
            String msg = "";

            switch (result) {
                case RESULT_UNKNOWN:
                    msg = ERROR_MSG_UNKNOWN;
                    break;
                case RESULT_INVALID_VERTEX:
                    msg = ERROR_MSG_INVALID_VERTEX;
                    break;
                case RESULT_ARC_ALREADY_EXISTS:
                    msg = ERROR_MSG_ARC_ALREADY_EXISTS;
                    break;
            }

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/alert_dialog.fxml"));
                Parent root = fxmlLoader.load();

                AlertDialogController controller = fxmlLoader.getController();
                controller.setMessage(msg);

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

    public void removeArc(int v, int w) {
        int result = graph.removeArc(v, w);

        if (result != RESULT_OK) {
            String msg = "";

            switch (result) {
                case RESULT_INVALID_VERTEX:
                    msg = ERROR_MSG_INVALID_VERTEX;
                    break;
                case RESULT_ARC_NOT_FOUND:
                    msg = ERROR_MSG_ARC_NOT_FOUND;
                    break;
            }

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/alert_dialog.fxml"));
                Parent root = (Parent) fxmlLoader.load();

                AlertDialogController controller = fxmlLoader.getController();
                controller.setMessage(msg);

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

    private void selectButton(Button source) {
        for (Button button : buttons) {
            if (button.equals(source)) {
                button.getStyleClass().clear();
                button.getStyleClass().add("selectedButton");
            } else {
                button.getStyleClass().clear();
                button.getStyleClass().add("button");
            }
        }
    }

    @FXML
    protected void handleCreateButtonAction(ActionEvent event) {
        selectButton((Button)event.getSource());

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/new_graph.fxml"));
            Parent root = fxmlLoader.load();

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

        switchMode(Mode.CREATE_GRAPH);
    }

    @FXML
    protected void handleInsertButtonAction(ActionEvent event) {
        selectButton((Button)event.getSource());
        switchMode(Mode.INSERT_ARC);
    }

    @FXML
    protected void handleRemoveButtonAction(ActionEvent event) {
        // TODO: using mouse
        selectButton((Button)event.getSource());

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

        switchMode(Mode.REMOVE_ARC);
    }

    @FXML
    protected void handleDepthSearchButtonAction(ActionEvent event) {
        selectButton((Button)event.getSource());

        if (graph != null) {

            boolean acyclic = !graph.depthSearchComplete();
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
                int cc = graph.getCc() != null ? graph.getCc()[v] : -1;

                VertexTableRow row = new VertexTableRow(v, label, parent, timeD, timeF, ts, cc);
                data.add(row);
            }

            tableView.setItems(data);
        }

        switchMode(Mode.DEPTH_SEARCH);
    }

    @FXML
    protected void handleFindPathButtonAction(ActionEvent event) {
        selectButton((Button)event.getSource());
        switchMode(Mode.FIND_PATH_DFS);
    }

    @FXML
    protected void handleBreadthSearchButtonAction(ActionEvent event) {
        selectButton((Button)event.getSource());
        switchMode(Mode.BREADTH_SEARCH);
    }

    @FXML
    protected void handleDAGMinButtonAction(ActionEvent event) {
        // TODO:
        if (graph != null) {
            boolean valid = false;

            if (graph instanceof MatrixDigraphCost) {
                valid = true;
            } else if (graph instanceof VectorDigraphCost) {
                valid = true;
            }

            if (!valid) {
                // TODO: não pode usar dijkstra em grafo sem custo - mostrar erro
                return;
            }

            // TODO: !!verificar é acíclico!!

            selectButton((Button)event.getSource());
            switchMode(Mode.DAGMIN_SPT);
        } else {
            // TODO: criar grafo primeiro - mostrar msg
        }
    }

    @FXML
    protected void handleDijkstraButtonAction(ActionEvent event) {
        // TODO:
        if (graph != null) {
            selectButton((Button) event.getSource());
            switchMode(Mode.DIJKSTRA_SPT);
        } else {
            // TODO: criar grafo primeiro - mostrar msg
        }
    }

    @FXML
    protected void handleBellmanFord2ButtonAction(ActionEvent event) {
        if (graph != null) {
            selectButton((Button)event.getSource());
            switchMode(Mode.BELLMAN_FORD_SPT);
        } else {
            // TODO: criar grafo primeiro - mostrar msg
        }
    }

    @FXML
    protected void handleBFSentinelaButtonAction(ActionEvent event) {
        if (graph != null) {
            selectButton((Button)event.getSource());
            switchMode(Mode.BF_SENTINEL_SPT);
        } else {
            // TODO: criar grafo primeiro - mostrar msg
        }
    }

    @FXML
    protected void handleFloydWarshallButtonAction(ActionEvent event) {
        if (graph != null) {
            selectButton((Button)event.getSource());
            switchMode(Mode.FLOYD_WARSHAW);

            try {
                MatrixDigraphCost costGraph = (MatrixDigraphCost)graph;
                costGraph.floydWarshaw();

                int[][] cost = costGraph.getCost();

                // mostrar na tela?

            } catch (ClassCastException e) {
                // TODO: not right type
                e.printStackTrace();
            }
        }
    }

    private void fillTable() {
        ObservableList<VertexTableRow> data = FXCollections.observableArrayList();

        for (int v = 0; v < graph.getVertices(); v++) {
            int parent = graph.getParent() != null ? graph.getParent()[v] : -1;
            int label = graph.getLabel() != null ? graph.getLabel()[v] : -1;
            int timeD = graph.getD() != null ? graph.getD()[v] : -1;
            int timeF = graph.getF() != null ? graph.getF()[v] : -1;
            int ts = graph.getTopologicalSort() != null ? graph.getTopologicalSort()[v] : -1;
            int cc = graph.getCc() != null ? graph.getCc()[v] : -1;

            VertexTableRow row = new VertexTableRow(v, label, parent, timeD, timeF, ts, cc);
            data.add(row);
        }

        tableView.setItems(data);
    }

    private void createCanvas() {
        canvas = new ResizableCanvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        canvas.addEventHandler(MouseEvent.ANY, new CanvasClickListener());

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(canvas);
        stackPane.setAlignment(Pos.CENTER);

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

        if (scaleX > scaleY) {
            //noinspection SuspiciousNameCombination
            scaleX = scaleY;
        } else if (scaleY > scaleX) {
            //noinspection SuspiciousNameCombination
            scaleY = scaleX;
        }

        gc.scale(scaleX, scaleY);

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        // draw temporary arc (mouse dragging)
        if (tempArcStart != null && tempArcEnd != null) {
            drawArc(gc, tempArcStart, tempArcEnd, 0, ARC_TYPE_TEMP);
        }

        if (graph != null) {
            if (algoType == ALGO_MATRIX) {
                int[][] adjMatrix = ((MatrixDigraph) graph).getAdjMatrix();

                for (int v = 0; v < graph.getVertices(); v++) {
                    // draw arcs from this vertex to all others
                    for (int w = 0; w < graph.getVertices(); w++) {
                        int cost = adjMatrix[v][w];
                        if (cost != 0) {
                            drawArc(gc, v, w, cost, graph.getVertices());
                        }
                    }

                    // draw vertex circle
                    boolean isSelected = (selectedVertex != null && v == selectedVertex);
                    drawVertex(gc, v, graph.getVertices(), isSelected);
                }
            } else if (algoType == ALGO_LIST) {
                List<LinkedList<VectorElement>> list = ((VectorDigraph) graph).getAdjVector();
                for (int v = 0; v < graph.getVertices(); v++) {
                    LinkedList<VectorElement> adjList = list.get(v);

                    for (VectorElement adjVertex : adjList) {
                        drawArc(gc, v, adjVertex.getW(), adjVertex.getCost(), graph.getVertices());
                    }

                    // draw vertex circle
                    boolean isSelected = (selectedVertex != null && v == selectedVertex);
                    drawVertex(gc, v, graph.getVertices(), isSelected);
                }
            }
        }

        gc.scale(1 / scaleX, 1 / scaleY);
    }

    private void drawVertex(GraphicsContext gc, int v, int numV, boolean isSelected) {
        double centerX = vertexCenterX(v, numV);
        double centerY = vertexCenterY(v, numV);
        double x = centerX - DIAMETER / scaleX / 2;
        double y = centerY - DIAMETER / scaleY / 2;

        Color colorFill = Color.BLACK;

        if (isSelected) {
            colorFill = Color.RED;
        } else {
            int[] cc = graph.getCc();
            if (cc != null) {
                int componentColor = cc[v] < vertexColors.length ? cc[v] : cc[v] % vertexColors.length;
                colorFill = vertexColors[componentColor];
            }
        }
        gc.setFill(colorFill);
        gc.setLineWidth(1);
        gc.fillOval(x, y, DIAMETER / scaleX, DIAMETER / scaleY);

        gc.setStroke(Color.WHITE);
        gc.strokeText(String.valueOf(v), centerX - OFFSET_X, centerY + OFFSET_Y);

        // desenha custo, se necessário
        if (costType == COST_YES) {
            int[] costVector = null;

            if (graph instanceof MatrixDigraphCost) {
                MatrixDigraphCost costGraph = (MatrixDigraphCost) graph;
                costVector = costGraph.getCostFromS();
            } else if (graph instanceof VectorDigraphCost) {
                VectorDigraphCost costGraph = (VectorDigraphCost) graph;
                costVector = costGraph.getCostFromS();
            }

            if (costVector != null) {
                int cost = costVector[v];
                double costX = costCenterX(v, numV);
                double costY = costCenterY(v, numV);

                gc.setStroke(Color.RED);
                gc.strokeText(String.valueOf(cost), costX, costY);
            }
        }
    }

    private void drawArc(GraphicsContext gc, int v, int w, int cost, int numV) {
        double startX = vertexCenterX(v, numV);
        double startY = vertexCenterY(v, numV);
        double endX = vertexCenterX(w, numV);
        double endY = vertexCenterY(w, numV);

        Point2D start = new Point2D(startX, startY);
        Point2D end = new Point2D(endX, endY);

        int arcType = ARC_TYPE_TREE;
        if (mode == Mode.DEPTH_SEARCH) {
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
        }

        if (path != null && mode == Mode.FIND_PATH_DFS) {
            int indexStart = path.indexOf(v);
            if (indexStart >= 0 && indexStart + 1 < path.size()) {
                int nextV = path.get(indexStart + 1);

                if (nextV == w) {
                    arcType = ARC_TYPE_PATH;
                }
            }
        }

        if (mode == Mode.BREADTH_SEARCH || mode == Mode.DIJKSTRA_SPT || mode == Mode.DAGMIN_SPT ||
                mode == Mode.BELLMAN_FORD_SPT || mode == Mode.BF_SENTINEL_SPT) {
            int[] parent = graph.getParent();
            if (parent != null) {
                if (parent[w] == v) {
                    arcType = ARC_TYPE_PATH;
                }
            }
        }

        drawArc(gc, start, end, cost, arcType);
    }

    private void drawArc(GraphicsContext gc, Point2D start, Point2D end, int cost, int arcType) {
        boolean shouldDraw = true;
        double angle = Math.toDegrees(Math.atan2(end.getY() - start.getY(), end.getX() - start.getX()));

        double arrowEndX = end.getX() + DIAMETER / 2 * Math.cos(Math.toRadians(180 + angle));
        double arrowEndY = end.getY() + DIAMETER / 2 * Math.sin(Math.toRadians(180 + angle));

        switch (arcType) {
            case ARC_TYPE_TEMP:
                gc.setStroke(Color.YELLOW);
                break;
            case ARC_TYPE_TREE:
                shouldDraw = cbTree.isSelected();
                gc.setStroke(Color.BLACK);
                break;
            case ARC_TYPE_DESC:
                shouldDraw = cbDescendant.isSelected();
                gc.setStroke(Color.PURPLE);
                break;
            case ARC_TYPE_CRSS:
                shouldDraw = cbCross.isSelected();
                gc.setStroke(Color.BLUE);
                break;
            case ARC_TYPE_RTRN:
                shouldDraw = cbReturn.isSelected();
                gc.setStroke(Color.GREEN);
                break;
            case ARC_TYPE_PATH:
                gc.setStroke(Color.RED);
                break;
        }

        if (shouldDraw) {
            gc.setLineWidth(3);
            gc.strokeLine(start.getX(), start.getY(), arrowEndX, arrowEndY);

            // desenhar custo do arco, se necessário
            if (costType == COST_YES && arcType != ARC_TYPE_TEMP) {
                double costX = (end.getX() - start.getX())/2 + start.getX();
                double costY = (end.getY() - start.getY())/2 + start.getY();
                gc.translate(costX, costY);
                gc.rotate(angle);
                gc.setLineWidth(1);
                gc.setStroke(Color.RED);
                gc.strokeText(String.valueOf(cost), -1, -1);
                gc.rotate(-angle);
                gc.translate(-costX, -costY);
            }

            // desenhar seta, se necessário
            if (graphType == TYPE_DIGRAPH) {
                Image arrow = new Image("/res/arrow_head.png", ARROW_SIZE, ARROW_SIZE, false, true);

                gc.translate(arrowEndX, arrowEndY);
                gc.rotate(angle);
                gc.drawImage(arrow, -ARROW_SIZE, -ARROW_SIZE / 2);
                gc.rotate(-angle);
                gc.translate(-arrowEndX, -arrowEndY);
            }
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

    private double costCenterX(int v, int numV) {
        double diffAngle = 360 / (double) numV;
        double angle = v * diffAngle - 90;

        double vertexCenterX = vertexCenterX(v, numV);

        return vertexCenterX + (DIAMETER * Math.cos(Math.toRadians(angle))) - 1;
    }

    private double costCenterY(int v, int numV) {
        double diffAngle = 360 / (double) numV;
        double angle = v * diffAngle - 90;

        double vertexCenterY = vertexCenterY(v, numV);

        return vertexCenterY + (DIAMETER * Math.sin(Math.toRadians(angle))) - 1;
    }

    public void setGraphType(int graphType) {
        this.graphType = graphType;
    }

    public void setCostType(int costType) {
        this.costType = costType;

        switch (costType) {
            case COST_NO:
                btnDAGmin.setDisable(true);
                btnDijkstra.setDisable(true);
                btnBellmanFord2.setDisable(true);
                btnFloydWarshall.setDisable(true);
                btnBellmanFordSentinel.setDisable(true);
                break;
            case COST_YES:
                break;
        }
    }

    public void setAlgoType(int algoType) {
        this.algoType = algoType;

        switch(algoType) {
            case ALGO_MATRIX:
                btnBellmanFordSentinel.setDisable(true);
                break;
            case ALGO_LIST:
                btnBellmanFord2.setDisable(true);
                btnFloydWarshall.setDisable(true);
                break;
        }

    }

    @Override
    public void costSet(int v, int w, int cost) {
        insertArc(v, w, cost);
    }

    private class CanvasClickListener implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            switch (mode) {
                case CREATE_GRAPH:
                    // do nothing
                    break;
                case INSERT_ARC:
                    handleInsertMode(mouseEvent);
                    break;
                case REMOVE_ARC:
                    handleRemoveMode(mouseEvent);
                    break;
                case DEPTH_SEARCH:
                    // do nothing
                    break;
                case FIND_PATH_DFS:
                    handleFindPath(mouseEvent);
                    break;
                case BREADTH_SEARCH:
                    handleBreadthSearch(mouseEvent);
                    break;
                case DAGMIN_SPT:
                    handleDAGmin(mouseEvent);
                    break;
                case DIJKSTRA_SPT:
                    handleDijkstra(mouseEvent);
                    break;
                case BELLMAN_FORD_SPT:
                    handleBellmanFord2(mouseEvent);
                    break;
                case BF_SENTINEL_SPT:
                    handleBFSentinel(mouseEvent);
                    break;
                case FLOYD_WARSHAW:
                    // do nothing
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
                        if (costType == COST_NO) {
                            insertArc(selectedVertex, releasedVertex, 1);
                        } else if (costType == COST_YES) {
                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/edit_arc.fxml"));
                                Parent root = fxmlLoader.load();

                                ArcCostController controller = fxmlLoader.getController();
                                controller.setV(selectedVertex);
                                controller.setW(releasedVertex);
                                controller.setListener(DrawingController.this);

                                Scene scene = new Scene(root);

                                Stage dialog = new Stage(StageStyle.UTILITY);
                                dialog.initModality(Modality.WINDOW_MODAL);
                                dialog.setTitle("Custo - Arco [" + selectedVertex + "," + releasedVertex + "]");
                                dialog.setScene(scene);
                                dialog.setWidth(200);
                                dialog.setHeight(100);
                                dialog.show();
                            } catch (IOException e) {
                                // TODO:
                            }
                        }
                    }

                    System.out.println("Released Vertex: " + releasedVertex);
                }

                tempArcStart = null;
                tempArcEnd = null;
                selectedVertex = null;
            }
        }

        private void handleRemoveMode(MouseEvent mouseEvent) {
            double mouseX = mouseEvent.getX() / scaleX;
            double mouseY = mouseEvent.getY() / scaleY;

            if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
                if (graph != null) {
                    if (selectedVertex != null) {
                        // TODO:
                    }
                }
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

        private void handleBreadthSearch(MouseEvent mouseEvent) {
            try {
                double mouseX  = mouseEvent.getX() / scaleX;
                double mouseY = mouseEvent.getY() / scaleY;

                if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
                    if (graph != null) {
                        Integer clickedVertex = vertexOnPosition(mouseX, mouseY);
                        if (clickedVertex != null) {
                            selectedVertex = clickedVertex;
                            graph.breadthSearch(selectedVertex);
                            System.out.println("Fim Dijkstra.");
                        }
                    }
                }
            } catch (ClassCastException e ) {
                // TODO: não pode usar dijkstra em grafo sem costFromS - mostrar erro
                e.printStackTrace();
            }
        }

        private void handleDAGmin(MouseEvent mouseEvent) {
            try {
                double mouseX  = mouseEvent.getX() / scaleX;
                double mouseY = mouseEvent.getY() / scaleY;

                if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
                    if (graph != null) {
                        Integer clickedVertex = vertexOnPosition(mouseX, mouseY);
                        if (clickedVertex != null) {
                            selectedVertex = clickedVertex;

                            if (graph instanceof MatrixDigraphCost) {
                                ((MatrixDigraphCost) graph).DAGmin(clickedVertex);
                            } else if (graph instanceof VectorDigraphCost) {
                                ((VectorDigraphCost)graph).DAGmin(clickedVertex);
                            } else {
                                // TODO: invalid
                            }

                            fillTable();
                            // TODO: update ui?
                            System.out.println("Fim Dijkstra.");
                        }
                    }
                }
            } catch (ClassCastException e ) {
                // TODO: não pode usar dijkstra em grafo sem costFromS - mostrar erro
                e.printStackTrace();
            }
        }

        private void handleDijkstra(MouseEvent mouseEvent) {
            try {
                MatrixDigraphCost costGraph = (MatrixDigraphCost)graph;

                if (costGraph.hasNegativeCosts()) {
                    // TODO: show msg
                    return;
                }

                double mouseX  = mouseEvent.getX() / scaleX;
                double mouseY = mouseEvent.getY() / scaleY;

                if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
                    if (graph != null) {
                        Integer clickedVertex = vertexOnPosition(mouseX, mouseY);
                        if (clickedVertex != null) {
                            selectedVertex = clickedVertex;
                            costGraph.dijkstra(clickedVertex);

                            // TODO: update ui?
                            fillTable();
                        }
                    }
                }
            } catch (ClassCastException e ) {
                // TODO: não pode usar dijkstra em grafo sem costFromS - mostrar erro
                e.printStackTrace();
            }
        }

        private void handleBellmanFord2(MouseEvent mouseEvent) {
            try {
                MatrixDigraphCost costGraph = (MatrixDigraphCost)graph;

                // TODO: restrições?

                double mouseX  = mouseEvent.getX() / scaleX;
                double mouseY = mouseEvent.getY() / scaleY;

                if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
                    if (graph != null) {
                        Integer clickedVertex = vertexOnPosition(mouseX, mouseY);
                        if (clickedVertex != null) {
                            selectedVertex = clickedVertex;
                            costGraph.bellmanFord2(clickedVertex);

                            // TODO: needed?
                            fillTable();
                        }
                    }
                }
            } catch (ClassCastException e ) {
                // TODO: não pode usar dijkstra em grafo sem costFromS - mostrar erro
                e.printStackTrace();
            }
        }

        private void handleBFSentinel(MouseEvent mouseEvent) {
            try {
                VectorDigraphCost costGraph = (VectorDigraphCost)graph;

                // TODO: restrições?

                double mouseX  = mouseEvent.getX() / scaleX;
                double mouseY = mouseEvent.getY() / scaleY;

                if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
                    if (graph != null) {
                        Integer clickedVertex = vertexOnPosition(mouseX, mouseY);
                        if (clickedVertex != null) {
                            selectedVertex = clickedVertex;
                            costGraph.bellmanFordSentinel(clickedVertex);

                            // TODO: needed?
                            fillTable();
                        }
                    }
                }
            } catch (ClassCastException e ) {
                // TODO: não pode usar dijkstra em grafo sem costFromS - mostrar erro
                e.printStackTrace();
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
