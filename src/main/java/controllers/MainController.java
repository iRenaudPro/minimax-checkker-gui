package controllers;

import data.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

    @FXML
    private Button startBtn;

    @FXML
    private VBox contentVBOX;

    private VBox gameBoard = new VBox();

    private BoardController boardController = new BoardController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Board.fxml"));
        try {
            gameBoard = fxmlLoader.load();
            contentVBOX.getChildren().add(gameBoard);

            boardController = fxmlLoader.<BoardController>getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        startBtn.setOnAction(e->startNewGame());
    }

    private void startNewGame(){
        boardController.startGame();
    }
}
