package com.gnegdev.gpaint;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.getIcons().add(new Image(getClass().getResource("/icons/icon.png").toString()));
        stage.setTitle("Gpaint");
        stage.setScene(scene);
        stage.show();

        stage.setMinWidth(480);
        stage.setMinHeight(480);
    }

    public static void main(String[] args) {
        launch();
    }
}