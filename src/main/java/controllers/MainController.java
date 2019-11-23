package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Controller class associated to main.fxml
 */
public class MainController implements Initializable {

    @FXML
    private VBox contentVBOX;
    @FXML
    private VBox menuVBOX;

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

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/board.fxml"));
        try {
            VBox gameBoard = fxmlLoader.load();
            contentVBOX.getChildren().add(gameBoard);
            boardController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Launches a modal to let the player choose difficulty level and side
     */
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

        ChoiceBox<DIFFICULTY_LEVELS> difficultyChoice = new ChoiceBox<>();
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
                return new Pair<>(difficultyChoice.getValue(), sideChoice.getValue().equals("BLACK"));
            }else{
                return null;
            }
        });

        Optional<Pair<DIFFICULTY_LEVELS, Boolean>> result =  startDialog.showAndWait();

        result.ifPresent(value->boardController.startGame(mapDifficultyToDepth(value.getKey()), value.getValue()));

    }

    /**
     * Displays rules for the player
     */
    private void showRules(){
        try {
            BorderPane rulesPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/rules.fxml")));
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

    /**
     * Maps difficulty levels to MiniMax algorithm depth [You may modify it to fit your needs]
     * Note that More the depth is big, more the AI is clever and more time will be needed for the AI to make a move.
     * And Less the depth is big, Less the AI is clever and less time will be needed for the AI to make a move
     * @param dl selected difficulty level
     * @return return the corresponding depth for a difficulty level
     */
    private int mapDifficultyToDepth(DIFFICULTY_LEVELS dl){
        switch (dl){
            case EASY:
                return 3;
            case MEDIUM:
                return 6;
            case EXPERT:
                return 7;
            default:
                return 1;
        }
    }
}