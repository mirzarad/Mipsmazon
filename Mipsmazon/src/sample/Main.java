package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        AnchorPane pane = loader.load();

        Scene scene = new Scene(pane);
        scene.getStylesheets().addAll(getClass().getResource("style.css").toExternalForm());

        primaryStage.setTitle("Mipsmazon");
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public static Connection conn;

    public static void main(String[] args) {

        // String url = "jdbc:mysql://" + "localhost:3306" + "/" + "mipsmazon";


        launch(args);
    }

}

