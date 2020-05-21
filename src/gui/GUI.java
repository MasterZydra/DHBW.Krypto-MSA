package gui;


import configuration.Configuration;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUI extends Application {

    TextArea outputArea;
    TextArea commandLineArea;

    private GuiController guiController;

    public void start(Stage primaryStage) {

        guiController = new GuiController(this);


        Configuration.runtimeStorage.instance.guiController = guiController;


        primaryStage.setTitle("MSA | Mosbach Security Agency");

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(15, 12, 15, 12));
        hBox.setSpacing(10);
        hBox.setStyle("-fx-background-color: #336699;");

        Button executeButton = new Button("Execute");
        executeButton.setPrefSize(100, 20);

        Button closeButton = new Button("Close");
        closeButton.setPrefSize(100, 20);

        executeButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                guiController.executeCommand(commandLineArea.getText());
            }
        });

        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {

                guiController.close();
            }
        });

        commandLineArea = new TextArea();
        commandLineArea.setWrapText(true);

        outputArea = new TextArea();

        outputArea.setWrapText(true);
        outputArea.setEditable(false);

        hBox.getChildren().addAll(executeButton, closeButton);

        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(25, 25, 25, 25));
        vbox.getChildren().addAll(hBox, commandLineArea, outputArea);

        Scene scene = new Scene(vbox, 950, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.addEventHandler(KeyEvent.KEY_PRESSED, (keyInput) -> keyPressed(keyInput.getCode()));
    }

    private void keyPressed(KeyCode keyCode) {
        switch (keyCode) {
            case F8:
                guiController.displayLog();
                break;
            case F5:
                guiController.executeCommand(commandLineArea.getText());
                break;
            case F3:
                if (guiController.isLoggingEnabled()) {
                    guiController.disableLogging();
                } else {
                    guiController.enableLogging();
                }
                break;
        }
    }

    public void displayText(String text) {
        outputArea.setText(text);
    }
}