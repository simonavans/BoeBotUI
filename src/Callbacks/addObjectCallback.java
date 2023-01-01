package Callbacks;

import BackEnd.Object;

public interface addObjectCallback {
    void onAddObjectEvent(int locationX, int locationY, int destinationX, int destinationY, Object object);
}
