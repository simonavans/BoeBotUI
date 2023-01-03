package Callbacks;

import BackEnd.Obstruction;

public interface addObstructionCallback {
    boolean onAddObstructionEvent(int locationX, int locationY, Obstruction obstruction);
}
