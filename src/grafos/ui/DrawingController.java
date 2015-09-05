package grafos.ui;

import grafos.Constants;
import grafos.datatypes.matriz.MatrixDigraph;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by dfcarvalho on 8/21/15.
 */
public class DrawingController implements Initializable {
    private static final int DIAMETER = 40;
    private static final int OFFSET_X = 5;
    private static final int OFFSET_Y = 5;
    private static final int ARROW_SIZE = 20;

    private static final int CANVAS_WIDTH = 500;
    private static final int CANVAS_HEIGHT = 500;

    @FXML
    private BorderPane root;
    private ResizableCanvas canvas;

    private double scaleX;
    private double scaleY;

    private int graphType;
    private int algoType;

    private MatrixDigraph graph;

    private Point2D tempArcStart;
    private Point2D tempArcEnd;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createCanvas();
    }

    public void createGraph(int v) {
        graph = new MatrixDigraph(v);
        // drawCanvas();
    }

    public void insertArc(int v, int w) {
        graph.insertArc(v, w);
        // drawCanvas();
    }

    public void removeArc(int v, int w) {
        graph.removeArc(v, w);
        // drawCanvas();
    }

    @FXML
    protected void handleCreateButtonAction(ActionEvent event) {
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
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/edit_arc.fxml"));
            NewArcController controller = new NewArcController();
            controller.setDrawingController(this);
            fxmlLoader.setController(controller);

            Parent root = (Parent) fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage dialog = new Stage(StageStyle.UTILITY);
            dialog.setTitle("Novo Arco");
            dialog.setScene(scene);
            dialog.sizeToScene();
            dialog.show();
        } catch (IOException e) {
            // TODO:
        }
    }

    @FXML
    protected void handleRemoveButtonAction(ActionEvent event) {
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
            drawArc(gc, tempArcStart, tempArcEnd);
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
                drawVertex(gc, v, graph.getVertices());
            }
        }

        gc.scale(1 / scaleX, 1 / scaleY);
    }

    private void drawVertex(GraphicsContext gc, int v, int numV) {
        double centerX = vertexCenterX(v, numV);
        double centerY = vertexCenterY(v, numV);
        double x = centerX - DIAMETER / scaleX / 2;
        double y = centerY - DIAMETER / scaleY / 2;

        gc.setFill(Color.GREEN);
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
        drawArc(gc, start, end);
    }

    private void drawArc(GraphicsContext gc, Point2D start, Point2D end) {
        double angle = Math.toDegrees(Math.atan2(end.getY() - start.getY(), end.getX() - start.getX()));

        double arrowEndX = end.getX() + DIAMETER / 2 * Math.cos(Math.toRadians(180 + angle));
        double arrowEndY = end.getY() + DIAMETER / 2 * Math.sin(Math.toRadians(180 + angle));

        gc.setStroke(Color.BLACK);
        gc.strokeLine(start.getX(), start.getY(), arrowEndX, arrowEndY);

        // arrow
        if (graphType == Constants.TYPE_DIGRAPH) {
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
        private int startVertex = -1;

        @Override
        public void handle(MouseEvent mouseEvent) {
            double mouseX = mouseEvent.getX() / scaleX;
            double mouseY = mouseEvent.getY() / scaleY;

            // Coordinates mouseEvent.getX() and mouseEvent.getY() are relative to canvas.
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
                int clickedVertex = vertexOnPosition(mouseX, mouseY);
                if (clickedVertex >= 0) {
                    System.out.println("Clicked Vertex: " + clickedVertex);
                }
            } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
                int clickedVertex = vertexOnPosition(mouseX, mouseY);
                if (clickedVertex >= 0) {
                    if (graph != null) {
                        int numV = graph.getVertices();
                        tempArcStart = new Point2D(vertexCenterX(clickedVertex, numV),
                                vertexCenterY(clickedVertex, numV));
                        startVertex = clickedVertex;
                    }
                    System.out.println("Pressed Vertex: " + clickedVertex);
                }
            } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                if (graph != null) {
                    tempArcEnd = new Point2D(mouseX, mouseY);
                }
            } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
                int releasedVertex = vertexOnPosition(mouseX, mouseY);
                if (releasedVertex >= 0 && startVertex != releasedVertex) {
                    if (graph != null) {
                        insertArc(startVertex, releasedVertex);
                    }

                    System.out.println("Released Vertex: " + releasedVertex);
                }

                tempArcStart = null;
                tempArcEnd = null;
                startVertex = -1;
            }
        }

        private int vertexOnPosition(double clickX, double clickY) {
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

            return -1;
        }
    }
}
