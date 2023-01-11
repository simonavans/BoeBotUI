package callbacks;

import backEnd.Obstruction;

public interface ObstructionListCallback {
    void onAddObstructionEvent();
    void onEditObstructionEvent();
    void convertObstruction(int locationX, int locationY, int destinationX, int destinationY, Obstruction obstruction);
    void onDeleteObstructionEvent();

    boolean isValidObstruction(int locationX, int locationY, Obstruction obstruction);
    boolean isValidConversion(int locationX, int locationY, int destinationX, int destinationY, Obstruction obstruction);
}