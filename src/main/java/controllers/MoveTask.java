package controllers;

import javafx.concurrent.Task;


public class MoveTask extends Task<MoveTask.Decision> {

    public enum Decision {
        COMPLETED,
        FAILED_MOVING_INVALID_PIECE,
        FAILED_INVALID_DESTINATION,
        ADDITIONAL_MOVE,
        GAME_ENDED
    }

    public MoveTask(int x1, int y1, int x2, int y2){

    }

    @Override
    protected Decision call() throws Exception {
        return null;
    }

    @Override
    protected void cancelled(){
        super.cancelled();
        updateMessage("Task cancelled");
    }

    @Override
    protected void failed(){
        super.cancelled();
        updateMessage("Task Failed");
    }

    @Override
    protected void succeeded(){
        super.succeeded();
        updateMessage("Taks Succeeded");
    }
}
