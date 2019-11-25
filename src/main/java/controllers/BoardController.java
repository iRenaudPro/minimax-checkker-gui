package controllers;

import processing.*;
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
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * Controller class associated to board.fxml file
 */
public class BoardController implements Initializable {

    //Playable squares from 1 to 32 in checkers notation
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

    private Board previousBoard;

    //Tracking moves
    private Boolean engagedMove = false;
    private Pair<Integer, Integer> engagedSquare = new Pair<>(-1, -1);

    //Mapping each playable square to a point (x, y)
    private HashMap<BorderPane, Pair<Integer, Integer>> squares = new HashMap<>();

    //Players
    private Player one;
    private PlayerAI two;
    private Player current;

    private Board board;

    //Determine which player starts.
    private boolean turn = true;
    private boolean additional_move = false;
    private boolean game_ended = false;
    private boolean game_started = false;

    public void initialize(URL location, ResourceBundle resources) {
        initializeBoardSquares();
        setBoardSquaresListeners();
        hintBtn.setOnAction(e->showMovablePieces(board));
    }

    /**
     * Loads the board
     * @param b the board to load
     */
    private void loadBoard(Board b, Boolean isAI){

        if(b!=board){
            System.out.println("Everything is a lie...");
        }

        for(int i=0; i<b.SIZE; i++){
            for (int j=0; j<b.SIZE; j++){


                getSquare(j, i).getStyleClass().remove("movable");

                if(!isAI){
                    getSquare(j, i).getStyleClass().remove("trail");
                    getSquare(j, i).getStyleClass().remove("captured");
                }


                if(b.getBoard()[i][j] == Board.Type.EMPTY){
                    if(getSquare(j, i)!= null){
                        getSquare(j, i).setCenter(null);
                    }

                    if(isAI && (b.getBoard()[i][j] != previousBoard.getBoard()[i][j])){



                        if(previousBoard.getBoard()[i][j]== Board.Type.BLACK || previousBoard.getBoard()[i][j]== Board.Type.BLACK_KING){
                            if(current.getSide()== Player.Side.WHITE){
                                getSquare(j, i).getStyleClass().add("captured");
                            }else if(current.getSide()== Player.Side.BLACK){
                                getSquare(j, i).getStyleClass().add("trail");
                            }
                        }else if(previousBoard.getBoard()[i][j]== Board.Type.WHITE || previousBoard.getBoard()[i][j]== Board.Type.WHITE_KING){
                            if(current.getSide()== Player.Side.BLACK){
                                getSquare(j, i).getStyleClass().add("captured");
                            }else if(current.getSide()== Player.Side.WHITE){
                                getSquare(j, i).getStyleClass().add("trail");
                            }
                        }
                    }else if(!isAI){
                        getSquare(j, i).getStyleClass().remove("trail");
                        getSquare(j, i).getStyleClass().remove("captured");
                    }

                }else if(b.getBoard()[i][j] == Board.Type.BLACK){
                    getSquare(j, i).setCenter(getImage("BLACK", one.getSide()== Player.Side.WHITE));

                    if(isAI && b.getBoard()[i][j]!=previousBoard.getBoard()[i][j]){
                        getSquare(j, i).getStyleClass().add("trail");
                    }

                }else if(b.getBoard()[i][j] == Board.Type.WHITE){
                    getSquare(j, i).setCenter(getImage("WHITE", one.getSide()== Player.Side.WHITE));

                    if(isAI && b.getBoard()[i][j]!=previousBoard.getBoard()[i][j]){
                        getSquare(j, i).getStyleClass().add("trail");
                    }

                }else if(b.getBoard()[i][j] == Board.Type.BLACK_KING){
                    getSquare(j, i).setCenter(getImage("BLACK_KING", one.getSide()== Player.Side.WHITE));

                    if(isAI && b.getBoard()[i][j]!=previousBoard.getBoard()[i][j]){
                        getSquare(j, i).getStyleClass().add("trail");
                    }

                }else if(b.getBoard()[i][j] == Board.Type.WHITE_KING){
                    getSquare(j, i).setCenter(getImage("WHITE_KING", one.getSide()== Player.Side.WHITE));

                    if(isAI && b.getBoard()[i][j]!=previousBoard.getBoard()[i][j]){
                        getSquare(j, i).getStyleClass().add("trail");
                    }

                }
            }
        }
    }

    /**
     * Returns an ImageView corresponding to provided piece type and rotation state
     * @param pieceType the type of the piece (BLACK, WHITE, BLACK_KING, WHITE_KING)
     * @param rotate determine if the image should be rotated or not (When Human Player choose WHITE SIDE)
     * @return an ImageView
     */
    private ImageView getImage(String pieceType, Boolean rotate){
        ImageView temp;
        switch (pieceType){
            case "BLACK":{
                temp = new ImageView(new Image("images/black.png"));
            }
            break;
            case "WHITE":{
                temp = new ImageView(new Image("images/white.png"));
            }
            break;
            case "BLACK_KING":{
                temp = new ImageView(new Image("images/black_king.png"));
            }
            break;
            case "WHITE_KING":{
                temp = new ImageView(new Image("images/white_king.png"));
            }
            break;
            default:{
                temp = new ImageView();
            }
            break;
        }
        temp.setFitWidth(50);
        temp.setPreserveRatio(true);
        if(rotate){
            temp.setRotate(180);
        }
        return temp;
    }

    /**
     * Adding each square to the HashMap (cases)
     */
    private void initializeBoardSquares(){
        squares.put(pos1, new Pair<>(1, 0));
        squares.put(pos2, new Pair<>(3, 0));
        squares.put(pos3, new Pair<>(5, 0));
        squares.put(pos4, new Pair<>(7, 0));

        squares.put(pos5, new Pair<>(0, 1));
        squares.put(pos6, new Pair<>(2, 1));
        squares.put(pos7, new Pair<>(4, 1));
        squares.put(pos8, new Pair<>(6, 1));

        squares.put(pos9, new Pair<>(1, 2));
        squares.put(pos10, new Pair<>(3, 2));
        squares.put(pos11, new Pair<>(5, 2));
        squares.put(pos12, new Pair<>(7, 2));

        squares.put(pos13, new Pair<>(0, 3));
        squares.put(pos14, new Pair<>(2, 3));
        squares.put(pos15, new Pair<>(4, 3));
        squares.put(pos16, new Pair<>(6, 3));

        squares.put(pos17, new Pair<>(1, 4));
        squares.put(pos18, new Pair<>(3, 4));
        squares.put(pos19, new Pair<>(5, 4));
        squares.put(pos20, new Pair<>(7, 4));

        squares.put(pos21, new Pair<>(0, 5));
        squares.put(pos22, new Pair<>(2, 5));
        squares.put(pos23, new Pair<>(4, 5));
        squares.put(pos24, new Pair<>(6, 5));

        squares.put(pos25, new Pair<>(1, 6));
        squares.put(pos26, new Pair<>(3, 6));
        squares.put(pos27, new Pair<>(5, 6));
        squares.put(pos28, new Pair<>(7, 6));

        squares.put(pos29, new Pair<>(0, 7));
        squares.put(pos30, new Pair<>(2, 7));
        squares.put(pos31, new Pair<>(4, 7));
        squares.put(pos32, new Pair<>(6, 7));
    }

    /**
     * Adding listeners to each square to handle moves
     */
    private void setBoardSquaresListeners(){
        for (Map.Entry<BorderPane, Pair<Integer, Integer>> entry : squares.entrySet()) {
            entry.getKey().setOnMouseClicked(e->{

                if(current!=one || game_ended ){
                    return;
                }

                if(!engagedMove){
                    if(entry.getKey().getCenter()==null){
                        return;
                    }

                    //Ignore invalid clicks
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
                    engagedSquare = new Pair<>(entry.getValue().getKey(), entry.getValue().getValue());
                    engagedMove = true;
                }else{
                    getSquare(engagedSquare.getKey(), engagedSquare.getValue()).getStyleClass().remove("active");
                    engagedMove = false;

                    //Give the possibility to cancel a started move by clicking on the same square
                    if(engagedSquare.getKey().equals(entry.getValue().getKey()) && engagedSquare.getValue().equals(entry.getValue().getValue())){
                        return;
                    }

                    //Make the move
                    humanMove( engagedSquare.getValue(),engagedSquare.getKey(),  entry.getValue().getValue(), entry.getValue().getKey());

                    //If additional_move needed
                    if(additional_move){
                        entry.getKey().getStyleClass().add("active");
                        engagedSquare = new Pair<>(entry.getValue().getKey(), entry.getValue().getValue());
                        engagedMove=true;
                    }
                }

            });
        }
    }

    /**
     * Get the square at the provided position
     * @param x : position on x axis
     * @param y : position on y axis
     * @return the square at the provided position
     */
    private BorderPane getSquare(int x, int y){
        for (Map.Entry<BorderPane, Pair<Integer, Integer>> entry : squares.entrySet()) {
            if(entry.getValue().getKey()==x && entry.getValue().getValue()==y){
                return entry.getKey();
            }
        }
        return new BorderPane();
    }

    /**
     * Starts a new game
     * @param depth minimax algorithm depth [mapped to difficulty levels]
     * @param black determines if human is playing on black side or not
     */
    void startGame(int depth, Boolean black){

        if(black){
            //Human Player
            one = new Player(Player.Side.BLACK);
            //AI Player
            two = new PlayerAI(Player.Side.WHITE, depth);

            turn = true;
            thisVBOX.setRotate(0);
        }else{
            //Human Player
            one = new Player( Player.Side.WHITE);
            //AI Player
            two = new PlayerAI(Player.Side.BLACK, depth);
            turn = false;
            // Rotate the board to facilitate the gameplay
            thisVBOX.setRotate(180);
        }


        board = new Board();

        game_started = true;
        game_ended = false;

        current = one;
        if(!turn)
            current = two;

        loadBoard(board, false);


        if(current==two){
            message.setText("AI is playing...");

            //Use of tasks to avoid thread pauses
            Task<Void> task = new Task<Void>() {

                @Override protected Void call() {

                    int j=0;
                    for (int i=0; i<=1000; i++) {
                        j++;
                        int finalJ = j;
                        Platform.runLater(() -> {
                            if(finalJ ==1000){
                                AIMove(null);
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

    /**
     * Make a move [human]
     * @param x1 start point position on x axis
     * @param y1 start point position on y axis
     * @param x2 end point position on x axis
     * @param y2 end point position on y axis
     */
    private void humanMove(int x1, int y1, int x2, int y2){
        Move m = new Move(x1, y1, x2, y2);

        previousBoard = board.clone();

        //Perform the move
        Board.Decision decision = current.makeMove(m, board);

        //Check if there is any additional move (for gui)
        additional_move = decision == Board.Decision.ADDITIONAL_MOVE;

        if(decision == Board.Decision.FAILED_INVALID_DESTINATION)
        {
            showAlert("Incorrect", "You can not move the piece to that destination", true);
        }else if(decision == Board.Decision.FAILED_MOVING_INVALID_PIECE){
            showAlert("Incorrect", "You can not move that piece! [PRESS HINTS FOR HINTS]", true);
        } else if(decision == Board.Decision.JUMP_AVAILABLE){
            showAlert("Incorrect", "Capture move available, You must capture! [PRESS HINTS FOR HINTS]", true);
        }
        else if(decision == Board.Decision.COMPLETED)
        {
            loadBoard(board, false);
            if(board.getNumBlackPieces() == 0)
            {
                message.setText("GAME ENDED: White wins");
                showAlert("Information", "White wins with " + board.getNumWhitePieces() + " pieces left", false);
                game_ended = true;
                return;
            }
            if(board.getNumWhitePieces() == 0)
            {
                message.setText("GAME ENDED: Black wins");
                showAlert("Information", "Black wins with " + board.getNumBlackPieces() + " pieces left", false);
                game_ended = true;
                return;
            }

            if(turn)
                current = two;
            else
                current = one;
            turn = !turn;

            message.setText("AI is playing...");

            //Use of tasks to avoid thread pauses
            Task<Void> task = new Task<Void>() {

                @Override protected Void call() {

                    int j=0;
                    for (int i=0; i<=1000; i++) {
                        j++;
                        int finalJ = j;
                        Platform.runLater(() -> {
                            if(finalJ ==1000){
                                AIMove(null);
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
            loadBoard(board, false);
            message.setText("ADDITIONAL MOVE");
        }
        else if(decision == Board.Decision.GAME_ENDED)
        {
            //current player cannot move
            if(current.getSide() == Player.Side.BLACK)
            {
                message.setText("GAME ENDED: White wins");
                showAlert("Information", "White wins", false);
                game_ended = true;
            }
            else {
                message.setText("GAME ENDED: Black wins");
                showAlert("Information", "Black wins", false);
                game_ended = true;
            }
        }

    }

    /**
     * Give hand to AI to make a move
     * @param move : the move to make if move!=null
     */
    private void AIMove(Move move){
        if(current instanceof AI) {
            Board.Decision decision;

            //Get all capture moves available for the AI
            List<Move> jumpMoves = board.getJumpMoves(current.getSide());

            previousBoard = board.clone();

            if(move==null){
                decision = ((AI) current).makeMove(board);
            }else{
                decision = board.makeMove(move, current.getSide());
            }

            if(decision == Board.Decision.JUMP_AVAILABLE){
                //If capture move available and AI don't want to perform it, force him to do so.
                Move randomJumpMove = jumpMoves.get(new Random().nextInt(jumpMoves.size()));
                AIMove(randomJumpMove);
            }
            else if(decision == Board.Decision.COMPLETED){

                loadBoard(board, true);

                if(board.getNumBlackPieces() == 0)
                {
                    message.setText("GAME ENDED: White Wins");
                    showAlert("Information", "White wins with " + board.getNumWhitePieces() + " pieces left", false );
                    game_ended = true;
                    return;
                }
                if(board.getNumWhitePieces() == 0)
                {
                    message.setText("GAME ENDED: Black Wins");
                    showAlert("Information", "Black wins with " + board.getNumBlackPieces() + " pieces left", false);
                    game_ended = true;
                    return;
                }

                if(turn)
                    current = two;
                else
                    current = one;
                turn = !turn;

                message.setText("It is your turn...");

            }

            else if(decision == Board.Decision.ADDITIONAL_MOVE) {

                loadBoard(board, true);

                //Use of tasks to avoid thread pauses
                Task<Void> task = new Task<Void>() {

                    @Override protected Void call() {

                        int j=0;
                        for (int i=0; i<=1000; i++) {
                            j++;
                            int finalJ = j;
                            Platform.runLater(() -> {
                                if(finalJ ==1000){
                                    AIMove(null);
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
                    showAlert("Information", "White wins", false);
                    game_ended = true;
                }
                else {
                    message.setText("GAME ENDED: Black wins");
                    showAlert("Information", "Black wins", false);
                    game_ended = true;
                }
            }
        }
    }

    /**
     * Display Hints [Movable Pieces]
     * @param b used board
     */
    private void showMovablePieces(Board b){
        //Hints are not available in these situations
        if(current!=one || game_ended || engagedMove || !game_started){
            return;
        }

        //if capture move available show only these pieces
        List<Move> jumpMoves = board.getJumpMoves(current.getSide());

        for(int i=0; i<b.SIZE; i++){
            for (int j=0; j<b.SIZE; j++){
                if(b.getBoard()[i][j] == Board.Type.EMPTY){
                    if(getSquare(j, i)!= null){
                        getSquare(j, i).setCenter(null);
                    }
                }else if(b.getBoard()[i][j] == Board.Type.BLACK){
                    if(one.getSide()== Player.Side.BLACK){
                        if(jumpMoves.size()!=0){
                            if(board.jumpMoveAvailableForPiece(i, j, jumpMoves)){
                                getSquare(j, i).getStyleClass().add("movable");
                            }
                        }else{
                            List<Move> possibleMoves = board.getValidMoves(i, j, Player.Side.BLACK);
                            if(possibleMoves.size()!=0){
                                getSquare(j, i).getStyleClass().add("movable");
                            }
                        }
                    }
                }else if(b.getBoard()[i][j] == Board.Type.WHITE){
                    if(one.getSide()== Player.Side.WHITE){
                        if(jumpMoves.size()!=0){
                            if(board.jumpMoveAvailableForPiece(i, j, jumpMoves)){
                                getSquare(j, i).getStyleClass().add("movable");
                            }
                        }else{
                            List<Move> possibleMoves = board.getValidMoves(i, j, Player.Side.WHITE);
                            if(possibleMoves.size()!=0){
                                getSquare(j, i).getStyleClass().add("movable");
                            }
                        }
                    }
                }else if(b.getBoard()[i][j] == Board.Type.BLACK_KING){
                    if(one.getSide()== Player.Side.BLACK){
                        if(one.getSide()== Player.Side.BLACK){
                            if(jumpMoves.size()!=0){
                                if(board.jumpMoveAvailableForPiece(i, j, jumpMoves)){
                                    getSquare(j, i).getStyleClass().add("movable");
                                }
                            }else{
                                List<Move> possibleMoves = board.getValidMoves(i, j, Player.Side.BLACK);
                                if(possibleMoves.size()!=0){
                                    getSquare(j, i).getStyleClass().add("movable");
                                }
                            }
                        }
                    }
                }else if(b.getBoard()[i][j] == Board.Type.WHITE_KING){
                    if(one.getSide()== Player.Side.WHITE){
                        if(jumpMoves.size()!=0){
                            if(board.jumpMoveAvailableForPiece(i, j, jumpMoves)){
                                getSquare(j, i).getStyleClass().add("movable");
                            }
                        }else{
                            List<Move> possibleMoves = board.getValidMoves(i, j, Player.Side.WHITE);
                            if(possibleMoves.size()!=0){
                                getSquare(j, i).getStyleClass().add("movable");
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Display an alert box
     * @param title the title of the alert
     * @param content the content of the alert
     * @param error determines if its an error alert or not
     */
    private void showAlert(String title, String content, Boolean error){
        Alert alert = new Alert(error ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
