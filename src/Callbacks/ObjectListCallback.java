package callbacks;

import backEnd.Object;

public interface ObjectListCallback {
    void onAddObjectEvent();
    void onEditObjectEvent();
    void onDeleteObjectEvent();

    boolean isValidObject(int locationX, int locationY, int destinationX, int destinationY, Object object);
}