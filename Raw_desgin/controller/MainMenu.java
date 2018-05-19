package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Node;


import java.awt.Button;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenu implements Initializable {


    @FXML private Button mainBoard = new Button("mainBoard");


    @FXML private Button menu = new Button("menu");


    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

    @FXML public void mainBoard(javafx.event.ActionEvent actionEvent) throws IOException {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        Parent options_FXML = FXMLLoader.load(getClass().getResource("../view/MainBoard.fxml"));
        stage.setScene(new Scene(options_FXML, stage.getWidth(), stage.getHeight()));
    }

    @FXML public void neMu(javafx.event.ActionEvent actionEvent) throws IOException {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        Parent options_FXML = FXMLLoader.load(getClass().getResource("../view/menu.fxml"));
        stage.setScene(new Scene(options_FXML, stage.getWidth(), stage.getHeight()));
    }
    @FXML public void exit(javafx.event.ActionEvent actionEvent) throws IOException {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        Parent options_FXML = FXMLLoader.load(getClass().getResource("../view/exit.fxml"));
        stage.setScene(new Scene(options_FXML, stage.getWidth(), stage.getHeight()));
    }
    @FXML public void swithToStart_1(javafx.event.ActionEvent actionEvent) throws IOException {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        Parent options_FXML = FXMLLoader.load(getClass().getResource("../view/exit.fxml"));
        stage.setScene(new Scene(options_FXML, stage.getWidth(), stage.getHeight()));
    }



}
