package Callbacks;

import BackEnd.Object;
import BackEnd.Obstruction;

public interface addObjectCallback {
    boolean onAddObjectEvent(int locationX, int locationY, int destinationX, int destinationY, Object object);
    boolean onAddObjectEvent(int locationX, int locationY, int destinationX, int destinationY, Obstruction obstruction);
}
