package com.gnegdev.gpaint;

import com.gnegdev.gpaint.utils.DragContext;
import com.gnegdev.gpaint.utils.Shapes;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

public class AppController {
    private Color color = Color.BLACK;
    private Color fillColor = Color.WHITE;
    private double lineWidth = 5;

    @FXML
    private Canvas canvas;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private ColorPicker fillColorPicker;
    @FXML
    private ComboBox<Shapes> shapeSelector;
    @FXML
    private Spinner<Integer> lineWidhtSelector;
    @FXML
    private Spinner<Integer> canvasWidhtSelector;
    @FXML
    private Spinner<Integer> canvasHeightSelector;
    @FXML
    private CheckBox strokeMode;
    @FXML
    private CheckBox fillMode;

    @FXML
    protected void initialize() {
        shapeSelector.setItems(FXCollections.observableArrayList(Shapes.values()));
        shapeSelector.setValue(Shapes.Brush);

        lineWidhtSelector.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 5));
        lineWidhtSelector.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            if (!"".equals(newValue)) {
                lineWidth = lineWidhtSelector.getValue();
                initializeDrawingModes();
            }
        });
        canvasWidhtSelector.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(16, 4096, 1080));
        canvasHeightSelector.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(16, 3072, 720));

        initializeDrawingModes();
    }

    @FXML
    protected void initializeDrawingModes() {
        GraphicsContext gc = canvas.getGraphicsContext2D();


        gc.setStroke(color);
        gc.setFill(color);
        gc.setLineWidth(lineWidth);

        DragContext dragContext = new DragContext();

        canvas.setOnMousePressed(event -> {
            dragContext.setStartX(event.getX());
            dragContext.setStartY(event.getY());
        });

        canvas.setOnMouseReleased(event -> {
            dragContext.setEndX(event.getX());
            dragContext.setEndY(event.getY());
            System.out.println("Origin: " + dragContext.getStartX() + ", " + dragContext.getStartY());
            System.out.println("Final: " + dragContext.getEndX() + ", " + dragContext.getEndY());


            gc.setFill(fillColor);
            switch (shapeSelector.getValue()) {
                case Line -> {
                    double startX = dragContext.getStartX();
                    double startY = dragContext.getStartY();
                    double endX = dragContext.getEndX();
                    double endY = dragContext.getEndY();
                    gc.strokeLine(startX, startY, endX, endY);
                }
                case Rectangle -> {
                    double posX = Math.min(dragContext.getStartX(), dragContext.getEndX());
                    double posY = Math.min(dragContext.getStartY(), dragContext.getEndY());
                    double width = Math.abs(dragContext.getEndX() - dragContext.getStartX());
                    double height = Math.abs(dragContext.getEndY() - dragContext.getStartY());
                    if (strokeMode.isSelected() && fillMode.isSelected()) {
                        gc.fillRect(posX, posY, width, height);
                        gc.strokeRect(posX, posY, width, height);
                    } else if (fillMode.isSelected()) {
                        gc.fillRect(posX, posY, width, height);
                    } else {
                        gc.strokeRect(posX, posY, width, height);
                    }

                }
                case Sphere -> {
                    double radius = Math.sqrt(Math.pow(dragContext.getEndX() - dragContext.getStartX(), 2) + Math.pow(dragContext.getEndY() - dragContext.getStartY(), 2)) * 2;

                    double posX = dragContext.getStartX() - (radius / 2);
                    double posY = dragContext.getStartY() - (radius / 2);


                    if (strokeMode.isSelected() && fillMode.isSelected()) {
                        gc.fillOval(posX, posY, radius, radius);
                        gc.strokeOval(posX, posY, radius, radius);
                    } else if (fillMode.isSelected()) {
                        gc.fillOval(posX, posY, radius, radius);
                    } else {
                        gc.strokeOval(posX, posY, radius, radius);
                    }
                }
                case Ellipse -> {
                    double posX = Math.min(dragContext.getStartX(), dragContext.getEndX());
                    double posY = Math.min(dragContext.getStartY(), dragContext.getEndY());
                    double width = Math.abs(dragContext.getEndX() - dragContext.getStartX());
                    double height = Math.abs(dragContext.getEndY() - dragContext.getStartY());
                    if (strokeMode.isSelected() && fillMode.isSelected()) {
                        gc.fillOval(posX, posY, width, height);
                        gc.strokeOval(posX, posY, width, height);
                    } else if (fillMode.isSelected()) {
                        gc.fillOval(posX, posY, width, height);
                    } else {
                        gc.strokeOval(posX, posY, width, height);
                    }
                }
            }
            gc.setFill(color);
        });

        canvas.setOnMouseDragged(event -> {
            switch (shapeSelector.getValue()) {
                case Brush -> {
                    gc.fillOval(event.getX() - (lineWidth / 2), event.getY() - (lineWidth / 2), lineWidth, lineWidth);
                }
                case Eraser -> {
                    gc.clearRect(event.getX() - (lineWidth / 2), event.getY() - (lineWidth / 2), lineWidth, lineWidth);
                }
            }
        });
    }

    @FXML
    protected void saveCanvasImage() {
        FileChooser saveFileDialog = new FileChooser();

        saveFileDialog.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG image", "*.png"),
                new FileChooser.ExtensionFilter("JPEG image", "*.jpeg", "*.jpg")
        );

        saveFileDialog.setTitle("Save drawing");
        saveFileDialog.setInitialFileName("Drawing");

        File file = saveFileDialog.showSaveDialog(null);
        if (file != null) {
            try {
                WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                canvas.snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException error) {
                System.out.println("File saving error!: " + error.getMessage());
            }
        }
    }

    @FXML
    protected void setColor() {
        color = colorPicker.getValue();
        initializeDrawingModes();
    }
    @FXML
    protected void setFillColor() {
        fillColor = fillColorPicker.getValue();
        initializeDrawingModes();
    }

    @FXML
    protected void clearCanvas() {
        canvas.setWidth(1080);
        canvas.setHeight(720);
        canvas.getGraphicsContext2D().clearRect(0, 0, 1080, 720);
    }
    @FXML
    protected void resizeCanvas() {
        canvas.setWidth(canvasWidhtSelector.getValue());
        canvas.setHeight(canvasHeightSelector.getValue());
    }

    @FXML
    protected void showInfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Gpaint");
        alert.setHeaderText("Gpaint v1.0.0");
        alert.setContentText("By Ivan Valtuille\n\n2024 GnegDev");

        alert.showAndWait();
    }
}