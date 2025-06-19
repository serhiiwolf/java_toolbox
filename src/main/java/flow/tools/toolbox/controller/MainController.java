package flow.tools.toolbox.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.io.File;
import javafx.scene.image.*;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javax.imageio.ImageIO;
import flow.tools.toolbox.model.ResizeOperation;
import flow.tools.toolbox.model.ConvertOperation;
import flow.tools.toolbox.model.ImageProcessingException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Desktop;
import flow.tools.toolbox.model.OperationHistory;


public class MainController {

    private Window primaryWindow;

    @FXML
    private GridPane gridPane;

    @FXML
    public void initialize() {
        setupGridPane();
    }
    public void setPrimaryWindow(Window window) {
        this.primaryWindow = window;
    }

    @FXML
    private void onOpenConvert(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.bmp")
        );
        File selectedFile = fileChooser.showOpenDialog(primaryWindow);

        if (selectedFile != null) {
            try {
                Image originalImage = new Image(selectedFile.toURI().toString());
                ImageView imageView = new ImageView(originalImage);

                double maxWidth = 600;
                double maxHeight = 300;
                double ratio = Math.min(maxWidth / originalImage.getWidth(),
                        maxHeight / originalImage.getHeight());
                imageView.setFitWidth(originalImage.getWidth() * ratio);
                imageView.setFitHeight(originalImage.getHeight() * ratio);

                String fileName = selectedFile.getName();
                String fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
                Label typeLabel = new Label("File type: " + fileType);
                typeLabel.setTextFill(javafx.scene.paint.Color.WHITE);

                Label convertLabel = new Label("Convert to:");
                convertLabel.setTextFill(javafx.scene.paint.Color.WHITE);

                ComboBox<String> conversionCombo = new ComboBox<>();
                conversionCombo.getItems().addAll("JPG", "PNG", "BMP", "TIFF", "GIF", "WEBP");
                conversionCombo.setPromptText("Choose a format");

                Button convertButton = new Button("Convert");
                convertButton.setOnAction(e -> {
                    String selectedFormat = conversionCombo.getValue();
                    if (selectedFormat != null) {
                        FileChooser saveChooser = new FileChooser();
                        saveChooser.setTitle("Save file as...");
                        String ext = selectedFormat.toLowerCase();
                        saveChooser.getExtensionFilters().add(
                                new FileChooser.ExtensionFilter(selectedFormat + " Image (*." + ext + ")", "*." + ext)
                        );
                        File saveFile = saveChooser.showSaveDialog(primaryWindow);

                        if (saveFile != null) {
                            try {
                                BufferedImage inputImage = ImageIO.read(selectedFile);
                                ConvertOperation convOp = new ConvertOperation(selectedFormat);
                                BufferedImage result = convOp.process(inputImage);

                                boolean success = ImageIO.write(result, selectedFormat.toLowerCase(), saveFile);
                                if (success) {
                                    OperationHistory.log("Convert", selectedFormat, saveFile.getAbsolutePath());
                                    showAlert(Alert.AlertType.INFORMATION, "Success", "The image is converted to the format " + selectedFormat);
                                } else {
                                    showAlert(Alert.AlertType.ERROR, "ERROR", "The format is not supported: " + selectedFormat);
                                }
                            } catch (ImageProcessingException ex) {
                                showAlert(Alert.AlertType.ERROR, "Processing error", ex.getMessage());
                            } catch (IOException ex) {
                                showAlert(Alert.AlertType.ERROR, "Saving error", ex.getMessage());
                            }
                        }
                    } else {
                        showAlert(Alert.AlertType.WARNING, "Warning", "Please select a format for conversion.");
                    }
                });


                gridPane.getChildren().clear();


                gridPane.add(imageView, 1, 0, 3, 2); // (col, row, colspan, rowspan)
                gridPane.add(typeLabel, 1, 3, 3, 1);
                gridPane.add(convertLabel, 1, 4);
                gridPane.add(conversionCombo, 2, 4);
                gridPane.add(convertButton, 3, 4);

                GridPane.setHalignment(typeLabel, HPos.CENTER);
                GridPane.setHalignment(convertLabel, HPos.RIGHT);
                GridPane.setHalignment(convertButton, HPos.LEFT);
                GridPane.setValignment(imageView, VPos.TOP);
                GridPane.setHalignment(imageView, HPos.CENTER);

            } catch (Exception e) {
                e.printStackTrace();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Can't get image");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }



    private void setupGridPane() {

        for (int i = 0; i < 5; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(20);
            gridPane.getColumnConstraints().add(col);
        }

        for (int i = 0; i < 5; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(20);
            gridPane.getRowConstraints().add(row);
        }

        Label titleLabel = new Label("Photo Tools");
        titleLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        titleLabel.setFont(Font.font("Arial Bold", 48));
        titleLabel.setAlignment(Pos.CENTER);
        GridPane.setHalignment(titleLabel, HPos.CENTER);
        GridPane.setValignment(titleLabel, VPos.TOP);



        Label subtitleLabel = new Label("by r0LF.tools 45308");
        subtitleLabel.setTextFill(javafx.scene.paint.Color.GRAY);
        subtitleLabel.setFont(Font.font("Arial", 16));
        subtitleLabel.setAlignment(Pos.CENTER);
        GridPane.setHalignment(subtitleLabel, HPos.CENTER);
        GridPane.setValignment(subtitleLabel, VPos.BOTTOM);


        gridPane.add(titleLabel,  1, 2, 3, 1);
        gridPane.add(subtitleLabel, 2, 2);



        GridPane.setFillWidth(titleLabel, true);
        GridPane.setFillHeight(titleLabel, true);
        GridPane.setHgrow(titleLabel, Priority.ALWAYS);
        GridPane.setVgrow(titleLabel, Priority.ALWAYS);
        GridPane.setHgrow(subtitleLabel, Priority.ALWAYS);
        GridPane.setVgrow(subtitleLabel, Priority.ALWAYS);




    }

    @FXML
    private void onOpenResize(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.bmp")
        );
        File selectedFile = fileChooser.showOpenDialog(primaryWindow);

        if (selectedFile != null) {
            try {
                Image originalImage = new Image(selectedFile.toURI().toString());
                ImageView imageView = new ImageView(originalImage);

                double maxWidth = 600;
                double maxHeight = 300;
                double ratio = Math.min(maxWidth / originalImage.getWidth(),
                        maxHeight / originalImage.getHeight());
                imageView.setFitWidth(originalImage.getWidth() * ratio);
                imageView.setFitHeight(originalImage.getHeight() * ratio);

                RadioButton manualRadio = new RadioButton("Manual");
                RadioButton scaleRadio = new RadioButton("Scale (%)");
                ToggleGroup modeGroup = new ToggleGroup();
                manualRadio.setToggleGroup(modeGroup);
                scaleRadio.setToggleGroup(modeGroup);
                manualRadio.setSelected(true);

                TextField widthField = new TextField();
                widthField.setPromptText("Width");
                TextField heightField = new TextField();
                heightField.setPromptText("Height");

                TextField scaleField = new TextField();
                scaleField.setPromptText("Scale (100%)");
                scaleField.setMaxWidth(100);

                widthField.setDisable(false);
                heightField.setDisable(false);
                scaleField.setDisable(true);

                modeGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal == manualRadio) {
                        widthField.setDisable(false);
                        heightField.setDisable(false);
                        scaleField.setDisable(true);
                    } else {
                        widthField.setDisable(true);
                        heightField.setDisable(true);
                        scaleField.setDisable(false);
                    }
                });

                Button resizeButton = new Button("Change size");
                resizeButton.setOnAction(e -> {
                    try {
                        BufferedImage inputImage = ImageIO.read(selectedFile);
                        BufferedImage resizedImage;

                        if (manualRadio.isSelected()) {
                            int newWidth = Integer.parseInt(widthField.getText());
                            int newHeight = Integer.parseInt(heightField.getText());
                            ResizeOperation resizeOp = new ResizeOperation(newWidth, newHeight);
                            resizedImage = resizeOp.process(inputImage);
                        } else {
                            double scale = Double.parseDouble(scaleField.getText()) / 100.0;
                            ResizeOperation resizeOp = new ResizeOperation(scale);
                            resizedImage = resizeOp.process(inputImage);
                        }

                        FileChooser saveChooser = new FileChooser();
                        saveChooser.setTitle("Save image");
                        saveChooser.getExtensionFilters().addAll(
                                new FileChooser.ExtensionFilter("PNG Image", "*.png"),
                                new FileChooser.ExtensionFilter("JPEG Image", "*.jpg"),
                                new FileChooser.ExtensionFilter("BMP Image", "*.bmp")
                        );
                        File saveFile = saveChooser.showSaveDialog(primaryWindow);
                        if (saveFile != null) {
                            String format = saveChooser.getSelectedExtensionFilter().getDescription().split(" ")[0].toLowerCase();
                            ImageIO.write(resizedImage, format, saveFile);
                            OperationHistory.log("Resize", format, saveFile.getAbsolutePath());
                            showAlert(Alert.AlertType.INFORMATION, "Success", "Image saved");
                        }
                    } catch (ImageProcessingException ex) {
                        showAlert(Alert.AlertType.ERROR, "Processing error", ex.getMessage());
                    } catch (NumberFormatException ex) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Give correct size");
                    } catch (Exception ex) {
                        showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
                    }
                });

                gridPane.getChildren().clear();
                gridPane.add(imageView, 1, 0, 3, 2);
                gridPane.add(manualRadio, 1, 3);
                gridPane.add(scaleRadio, 2, 3);
                gridPane.add(widthField, 1, 4);
                gridPane.add(heightField, 1, 4);
                gridPane.add(scaleField, 2, 4);
                gridPane.add(resizeButton, 3, 4);

                GridPane.setHalignment(resizeButton, HPos.LEFT);
                GridPane.setHalignment(imageView, HPos.CENTER);
                GridPane.setValignment(imageView, VPos.TOP);
                GridPane.setValignment(widthField, VPos.TOP );
                GridPane.setValignment(heightField, VPos.CENTER );
                GridPane.setValignment(manualRadio, VPos.BOTTOM );
                GridPane.setValignment(scaleRadio, VPos.BOTTOM );


            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Can't get image " + e.getMessage());
            }
        }
    }

    @FXML
    private void onShowLogs(ActionEvent event) {
        try {
            File logFile = new File(OperationHistory.getLogFilePath());
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            Desktop.getDesktop().open(logFile);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Can't open log file: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void onExit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void onAbout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Photo Tools");
        alert.setContentText("made by r0LF.tools (45308)\nVersion 0.1");
        alert.showAndWait();
    }
}
