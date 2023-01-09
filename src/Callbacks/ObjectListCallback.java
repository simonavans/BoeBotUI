package Callbacks;

import BackEnd.Object;

public interface ObjectListCallback {
    void onAddObjectEvent();
    void onEditObjectEvent();
    void onDeleteObjectEvent();

    boolean isValidObject(int locationX, int locationY, int destinationX, int destinationY, Object object);
}