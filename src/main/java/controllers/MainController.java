package controllers;

import data.Board;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableListBase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

    @FXML
    private VBox contentVBOX;
    @FXML
    private VBox menuVBOX;

    private VBox gameBoard = new VBox();

    private BoardController boardController = new BoardController();

    private enum DIFFICULTY_LEVELS{
        EASY, MEDIUM, EXPERT
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        MenuBar menuBar = new MenuBar();

        Menu gameMenu = new Menu("Game");
        MenuItem newGame = new MenuItem("New Game");
        MenuItem exitGame  = new MenuItem("Exit Game");
        gameMenu.getItems().add(newGame);
        gameMenu.getItems().add(exitGame);

        Menu help = new Menu("Help");
        MenuItem rules = new MenuItem("Rules");
        help.getItems().add(rules);



        menuBar.getMenus().add(gameMenu);
        menuBar.getMenus().add(help);

        menuVBOX.getChildren().add(menuBar);

        exitGame.setOnAction(e-> Platform.exit());
        newGame.setOnAction(e->startNewGame());
        rules.setOnAction(e->showRules());


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Board.fxml"));
        try {
            gameBoard = fxmlLoader.load();
            contentVBOX.getChildren().add(gameBoard);

            boardController = fxmlLoader.<BoardController>getController();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void startNewGame(){

        Dialog<Pair<DIFFICULTY_LEVELS, Boolean>> startDialog = new Dialog<>();
        startDialog.setTitle("NEW GAME");
        startDialog.setHeaderText(null);

        ButtonType startButtonType = new ButtonType("START", ButtonBar.ButtonData.OK_DONE);
        startDialog.getDialogPane().getButtonTypes().addAll(startButtonType, ButtonType.CANCEL);

        GridPane grid  =  new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.setPadding(new Insets(10, 30, 10 ,30));

        ChoiceBox<DIFFICULTY_LEVELS> difficultyChoice = new ChoiceBox<DIFFICULTY_LEVELS>();
        difficultyChoice.getItems().addAll(DIFFICULTY_LEVELS.values());
        difficultyChoice.setValue(DIFFICULTY_LEVELS.EASY);

        ChoiceBox<String> sideChoice = new ChoiceBox<>();
        sideChoice.getItems().addAll("BLACK", "WHITE");
        sideChoice.setValue("BLACK");

        grid.add(new Label("DIFFICULTY:"), 1, 1);
        grid.add(difficultyChoice, 2, 1);

        grid.add(new Label("SIDE:"), 1, 2);
        grid.add(sideChoice, 2, 2);

        startDialog.getDialogPane().setContent(grid);

        startDialog.setResultConverter(dialogButton->{
            if(dialogButton==startButtonType){
                return new Pair<DIFFICULTY_LEVELS,Boolean> (difficultyChoice.getValue(), sideChoice.getValue().equals("BLACK"));
            }else{
                return null;
            }
        });

        Optional<Pair<DIFFICULTY_LEVELS, Boolean>> result =  startDialog.showAndWait();

        if(result.isPresent()){
            boardController.startGame(mapDifficultyToDepht(result.get().getKey()), result.get().getValue());
        }
    }
    private void showRules(){
        try {
            BorderPane rulesPane = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/rules.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(contentVBOX.getScene().getWindow());
            Scene scene = new Scene(rulesPane);

            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("RULES");
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int mapDifficultyToDepht(DIFFICULTY_LEVELS dl){
        switch (dl){
            case EASY:
                return 3;
            case MEDIUM:
                return 6;
            case EXPERT:
                return 7;
            default:
                return 3;
        }
    }
}