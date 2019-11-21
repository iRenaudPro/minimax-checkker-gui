package controllers;

import data.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.awt.*;
import java.net.URL;
import java.util.*;
import java.util.List;

public class BoardController implements Initializable {

    @FXML
    private BorderPane pos1;
    @FXML
    private BorderPane pos2;
    @FXML
    private BorderPane pos3;
    @FXML
    private BorderPane pos4;
    @FXML
    private BorderPane pos5;
    @FXML
    private BorderPane pos6;
    @FXML
    private BorderPane pos7;
    @FXML
    private BorderPane pos8;
    @FXML
    private BorderPane pos9;
    @FXML
    private BorderPane pos10;
    @FXML
    private BorderPane pos11;
    @FXML
    private BorderPane pos12;
    @FXML
    private BorderPane pos13;
    @FXML
    private BorderPane pos14;
    @FXML
    private BorderPane pos15;
    @FXML
    private BorderPane pos16;
    @FXML
    private BorderPane pos17;
    @FXML
    private BorderPane pos18;
    @FXML
    private BorderPane pos19;
    @FXML
    private BorderPane pos20;
    @FXML
    private BorderPane pos21;
    @FXML
    private BorderPane pos22;
    @FXML
    private BorderPane pos23;
    @FXML
    private BorderPane pos24;
    @FXML
    private BorderPane pos25;
    @FXML
    private BorderPane pos26;
    @FXML
    private BorderPane pos27;
    @FXML
    private BorderPane pos28;
    @FXML
    private BorderPane pos29;
    @FXML
    private BorderPane pos30;
    @FXML
    private BorderPane pos31;
    @FXML
    private BorderPane pos32;

    @FXML
    private javafx.scene.control.Label message;

    @FXML
    private VBox thisVBOX;

    @FXML
    private Button hintBtn;

    private Boolean engagedMove = false;
    private Pair<Integer, Integer> engagedCase = new Pair<>(-1, -1);

    private final int SIZE = 8;

    HashMap<BorderPane, Pair<Integer, Integer>> cases = new HashMap<>();

    Player one;
    MinimaxAI two;
    Player current;
    Board board;
    private int c = 0;
    boolean turn = true;
    boolean additional_move = false;
    boolean game_ended = false;
    boolean game_started = false;

    Task<Void> task;

    public void initialize(URL location, ResourceBundle resources) {
        initializeBoardCases();
        setBoardCasesListeners();
        hintBtn.setOnAction(e->showMovablePieces(board));
    }

    private void loadBoard(Board b){
        for(int i=0; i<SIZE; i++){
            for (int j=0; j<SIZE; j++){
                if(b.getBoard()[i][j] == Board.Type.EMPTY){
                    if(getCase(j, i)!= null){
                        getCase(j, i).setCenter(null);
                        if(getCase(j, i).getStyleClass().contains("movable")){
                            getCase(j, i).getStyleClass().remove("movable");
                        }
                    }
                    continue;
                }else if(b.getBoard()[i][j] == Board.Type.BLACK){

                    if(getCase(j, i).getStyleClass().contains("movable")){
                        getCase(j, i).getStyleClass().remove("movable");
                    }


                    ImageView temp = new ImageView(new Image("images/black.png"));
                    temp.setFitWidth(50);
                    temp.setPreserveRatio(true);

                    if(one.getSide()== Player.Side.WHITE){
                        temp.setRotate(180);
                    }

                    getCase(j, i).setCenter(temp);
                }else if(b.getBoard()[i][j] == Board.Type.WHITE){

                    if(getCase(j, i).getStyleClass().contains("movable")){
                        getCase(j, i).getStyleClass().remove("movable");
                    }

                    ImageView temp = new ImageView(new Image("images/white.png"));
                    temp.setFitWidth(50);
                    temp.setPreserveRatio(true);

                    if(one.getSide()== Player.Side.WHITE){
                        temp.setRotate(180);
                    }

                    getCase(j, i).setCenter(temp);
                }else if(b.getBoard()[i][j] == Board.Type.BLACK_KING){

                    if(getCase(j, i).getStyleClass().contains("movable")){
                        getCase(j, i).getStyleClass().remove("movable");
                    }

                    ImageView temp = new ImageView(new Image("images/black_king.png"));
                    temp.setFitWidth(50);
                    temp.setPreserveRatio(true);

                    if(one.getSide()== Player.Side.WHITE){
                        temp.setRotate(180);
                    }

                    getCase(j, i).setCenter(temp);
                }else if(b.getBoard()[i][j] == Board.Type.WHITE_KING){

                    if(getCase(j, i).getStyleClass().contains("movable")){
                        getCase(j, i).getStyleClass().remove("movable");
                    }

                    ImageView temp = new ImageView(new Image("images/white_king.png"));
                    temp.setFitWidth(50);
                    temp.setPreserveRatio(true);

                    if(one.getSide()== Player.Side.WHITE){
                        temp.setRotate(180);
                    }

                    getCase(j, i).setCenter(temp);
                }
            }
        }

    }

    private void initializeBoardCases(){
        cases.put(pos1, new Pair<>(1, 0));
        cases.put(pos2, new Pair<>(3, 0));
        cases.put(pos3, new Pair<>(5, 0));
        cases.put(pos4, new Pair<>(7, 0));

        cases.put(pos5, new Pair<>(0, 1));
        cases.put(pos6, new Pair<>(2, 1));
        cases.put(pos7, new Pair<>(4, 1));
        cases.put(pos8, new Pair<>(6, 1));

        cases.put(pos9, new Pair<>(1, 2));
        cases.put(pos10, new Pair<>(3, 2));
        cases.put(pos11, new Pair<>(5, 2));
        cases.put(pos12, new Pair<>(7, 2));

        cases.put(pos13, new Pair<>(0, 3));
        cases.put(pos14, new Pair<>(2, 3));
        cases.put(pos15, new Pair<>(4, 3));
        cases.put(pos16, new Pair<>(6, 3));

        cases.put(pos17, new Pair<>(1, 4));
        cases.put(pos18, new Pair<>(3, 4));
        cases.put(pos19, new Pair<>(5, 4));
        cases.put(pos20, new Pair<>(7, 4));

        cases.put(pos21, new Pair<>(0, 5));
        cases.put(pos22, new Pair<>(2, 5));
        cases.put(pos23, new Pair<>(4, 5));
        cases.put(pos24, new Pair<>(6, 5));

        cases.put(pos25, new Pair<>(1, 6));
        cases.put(pos26, new Pair<>(3, 6));
        cases.put(pos27, new Pair<>(5, 6));
        cases.put(pos28, new Pair<>(7, 6));

        cases.put(pos29, new Pair<>(0, 7));
        cases.put(pos30, new Pair<>(2, 7));
        cases.put(pos31, new Pair<>(4, 7));
        cases.put(pos32, new Pair<>(6, 7));



    }

    private void setBoardCasesListeners(){
        for (Map.Entry<BorderPane, Pair<Integer, Integer>> entry : cases.entrySet()) {

            entry.getKey().setOnMouseClicked(e->{

                if(current!=one){
                    return;
                }

                if(game_ended){
                    return;
                }

                if(!engagedMove){
                    if(entry.getKey().getCenter()==null){
                        return;
                    }

                    if(one.getSide()== Player.Side.BLACK){
                        if(board.getBoard()[entry.getValue().getValue()][ entry.getValue().getKey()] != Board.Type.BLACK){
                            if(board.getBoard()[entry.getValue().getValue()][ entry.getValue().getKey()] != Board.Type.BLACK_KING){
                                return;
                            }
                        }
                    }else{
                        if(board.getBoard()[entry.getValue().getValue()][ entry.getValue().getKey()] != Board.Type.WHITE){
                            if(board.getBoard()[entry.getValue().getValue()][ entry.getValue().getKey()] != Board.Type.WHITE_KING){
                                return;
                            }
                        }
                    }


                    entry.getKey().getStyleClass().add("active");
                    engagedCase = new Pair<>(entry.getValue().getKey(), entry.getValue().getValue());
                    engagedMove = true;
                }else{
                    getCase(engagedCase.getKey(), engagedCase.getValue()).getStyleClass().remove("active");
                    engagedMove = false;


                    if(engagedCase.getKey()==entry.getValue().getKey() && engagedCase.getValue()==entry.getValue().getValue()){
                        return;
                    }

                    gameServe( engagedCase.getValue(),engagedCase.getKey(),  entry.getValue().getValue(), entry.getValue().getKey());

                    if(additional_move){
                        entry.getKey().getStyleClass().add("active");
                        engagedCase = new Pair<>(entry.getValue().getKey(), entry.getValue().getValue());
                        engagedMove=true;
                    }


                }

            });

        }
    }

    private BorderPane getCase(int x, int y){
        for (Map.Entry<BorderPane, Pair<Integer, Integer>> entry : cases.entrySet()) {
            if(entry.getValue().getKey()==x && entry.getValue().getValue()==y){
                return entry.getKey();
            }
        }
        return new BorderPane();
    }

    public void startGame(int depth, Boolean black){

        if(black){
            one = new Player("HUMAN", Player.Side.BLACK);
            two = new MinimaxAI(Player.Side.WHITE, depth);
            turn = true;
            thisVBOX.setRotate(0);
        }else{
            one = new Player("HUMAN", Player.Side.WHITE);
            two = new MinimaxAI(Player.Side.BLACK, depth);
            turn = false;
            thisVBOX.setRotate(180);
        }


        board = new Board();

        game_started = true;
        game_ended = false;

        current = one;
        if(!turn)
            current = two;

        loadBoard(board);

        if(current==two){
            message.setText("AI is playing...");
            Task<Void> task = new Task<Void>() {

                @Override protected Void call() throws Exception {

                    int j=0;
                    for (int i=0; i<=1000; i++) {
                        j++;
                        int finalJ = j;
                        Platform.runLater(new Runnable() {
                            @Override public void run() {
                                if(finalJ ==1000){
                                    AIMove(null);
                                }
                            }
                        });
                    }
                    return null;
                }
            };

            new Thread(task).start();
        }else{
            message.setText("It's your turn...");
        }
    }

    private void gameServe(int x1, int y1, int x2, int y2){

        //message.setText(current.toString() + "'s turn: ");

        Board.Decision decision = null;

        Move m;
        m = new Move(x1, y1, x2, y2);
        decision = current.makeMove(m, board);

        additional_move = decision==Board.Decision.ADDITIONAL_MOVE;


        if(decision == Board.Decision.FAILED_INVALID_DESTINATION)
        {
            //println("Move Failed");
            // message.setText("Move Failed");

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText(null);
            alert.setContentText("INVALID DESTINATION");
            alert.showAndWait();

            // don't update anything
        }else if(decision == Board.Decision.FAILED_MOVING_INVALID_PIECE){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText(null);
            alert.setContentText("YOU CAN NOT MOVE THAT PIECE");
            alert.showAndWait();
        } else if(decision == Board.Decision.JUMP_AVAILABLE){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText(null);
            alert.setContentText("CAPTURE MOVE AVAILABLE, YOU MUST CAPTURE!");
            alert.showAndWait();
        }
        else if(decision == Board.Decision.COMPLETED)
        {
            //loadBoard(board);

            loadBoard(board);

            if(board.getNumBlackPieces() == 0)
            {

                message.setText("GAME ENDED: White wins");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("White wins with " + board.getNumWhitePieces() + " pieces left");
                alert.showAndWait();

                game_ended = true;
                return;
            }
            if(board.getNumWhitePieces() == 0)
            {
                message.setText("GAME ENDED: Black wins");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Black wins with " + board.getNumBlackPieces() + " pieces left");
                alert.showAndWait();

                game_ended = true;
                return;
            }


            if(turn)
                current = two;
            else
                current = one;
            turn = !turn;

            message.setText("AI is playing...");

            Task<Void> task = new Task<Void>() {

                @Override protected Void call() throws Exception {

                    int j=0;
                    for (int i=0; i<=1000; i++) {
                        j++;
                        int finalJ = j;
                        Platform.runLater(new Runnable() {
                            @Override public void run() {
                                if(finalJ ==1000){
                                    AIMove(null);
                                }
                            }
                        });
                    }
                    return null;
                }
            };

            new Thread(task).start();



        }
        else if(decision == Board.Decision.ADDITIONAL_MOVE)
        {
            //loadBoard(board);

            loadBoard(board);

            message.setText("Additional Move");
        }
        else if(decision == Board.Decision.GAME_ENDED)
        {
            //current player cannot move
            if(current.getSide() == Player.Side.BLACK)
            {
                message.setText("GAME ENDED: White wins");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("White wins");
                alert.showAndWait();

                game_ended = true;
                return;
            }
            else {
                message.setText("GAME ENDED: Black wins");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Black wins");
                alert.showAndWait();

                game_ended = true;
                return;
            }
        }

    }

    private void AIMove(Move move){
        if(current instanceof AI) {
            Board.Decision decision = null;

            List<Move> jumpMoves = new ArrayList<>();

            //if capture move available force AI to perform it
            for(Move m: board.getAllValidMoves(current.getSide())){
                if(m.getStart().x+1 != m.getEnd().x && m.getStart().x-1 != m.getEnd().x){
                    jumpMoves.add(m);
                }
            }

            if(move==null){
                decision = ((AI) current).makeMove(board);
            }else{
                //decision = ((AI) current).makeMove(move, board);
                decision = board.makeMove(move, current.getSide());
            }


            if(decision == Board.Decision.JUMP_AVAILABLE){
                Move randomJumpMove = jumpMoves.get(new Random().nextInt(jumpMoves.size()));

                AIMove(randomJumpMove);
            }
            else if(decision == Board.Decision.COMPLETED){

                loadBoard(board);

                if(board.getNumBlackPieces() == 0)
                {

                    message.setText("GAME ENDED: White Wins");

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("White wins with " + board.getNumWhitePieces() + " pieces left");
                    alert.showAndWait();

                    game_ended = true;
                    return;
                }
                if(board.getNumWhitePieces() == 0)
                {
                    message.setText("GAME ENDED: Black Wins");

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("Black wins with " + board.getNumBlackPieces() + " pieces left");
                    alert.showAndWait();

                    game_ended = true;
                    return;
                }

                if(turn)
                    current = two;
                else
                    current = one;
                turn = !turn;

                message.setText("It is your turn");

            }

            else if(decision == Board.Decision.ADDITIONAL_MOVE) {
                //loadBoard(board);


                loadBoard(board);

                //message.setText("Additional Move");

                Task<Void> task = new Task<Void>() {

                    @Override protected Void call() throws Exception {

                        int j=0;
                        for (int i=0; i<=1000; i++) {
                            j++;
                            int finalJ = j;
                            Platform.runLater(new Runnable() {
                                @Override public void run() {
                                    if(finalJ ==1000){
                                        AIMove(null);
                                    }
                                }
                            });
                        }
                        return null;
                    }
                };

                new Thread(task).start();
            }


            else if(decision == Board.Decision.GAME_ENDED){

                //current player cannot move
                if(current.getSide() == Player.Side.BLACK)
                {
                    message.setText("GAME ENDED: White wins");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("White wins");
                    alert.showAndWait();

                    game_ended = true;
                    return;
                }
                else {
                    message.setText("GAME ENDED: Black wins");

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("Black wins");
                    alert.showAndWait();

                    game_ended = true;
                    return;
                }
            }
        }
    }

    private void showMovablePieces(Board b){
        if(game_ended){
            return;
        }

        if(current!=one){
            return;
        }

        if(engagedMove){
            return;
        }

        if(!game_started){
            return;
        }

        System.out.println("HINT.....");

        for(int i=0; i<SIZE; i++){
            for (int j=0; j<SIZE; j++){

                if(b.getBoard()[i][j] == Board.Type.EMPTY){
                    if(getCase(j, i)!= null){
                        getCase(j, i).setCenter(null);
                    }
                    continue;
                }else if(b.getBoard()[i][j] == Board.Type.BLACK){

                    if(one.getSide()== Player.Side.BLACK){

                        List<Move> possibleMoves = board.getValidMoves(i, j, Player.Side.BLACK);
                        if(possibleMoves.size()!=0){
                            getCase(j, i).getStyleClass().add("movable");
                        }
                    }

                //Change color
                }else if(b.getBoard()[i][j] == Board.Type.WHITE){

                    if(one.getSide()== Player.Side.WHITE){

                        List<Move> possibleMoves = board.getValidMoves(i, j, Player.Side.WHITE);
                        if(possibleMoves.size()!=0){
                            getCase(j, i).getStyleClass().add("movable");
                        }

                    }

                    //Change color
                }else if(b.getBoard()[i][j] == Board.Type.BLACK_KING){

                    if(one.getSide()== Player.Side.BLACK){

                        List<Move> possibleMoves = board.getValidMoves(i, j, Player.Side.BLACK);
                        if(possibleMoves.size()!=0){
                            getCase(j, i).getStyleClass().add("movable");
                        }

                    }

                    //Change color
                }else if(b.getBoard()[i][j] == Board.Type.WHITE_KING){

                    if(one.getSide()== Player.Side.WHITE){

                        List<Move> possibleMoves = board.getValidMoves(i, j, Player.Side.WHITE);
                        if(possibleMoves.size()!=0){
                            getCase(j, i).getStyleClass().add("movable");
                        }

                    }

                    //Change color
                }
            }
        }
    }

}
