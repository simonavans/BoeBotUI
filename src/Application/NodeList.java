package Application;

public class NodeList {

    private String label;
    private int locationX;
    private int locationY;
    private int destinationX;
    private int destinationY;

    public NodeList(String label, int locationX, int locationY, int destinationX, int destinationY) {
        this.label = label;
        this.locationX = locationX;
        this.locationY = locationY;
        this.destinationX = destinationX;
        this.destinationY = destinationY;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setLocationX(int locationX) {
        this.locationX = locationX;
    }

    public void setLocationY(int locationY) {
        this.locationY = locationY;
    }

    public void setDestinationX(int destinationX) {
        this.destinationX = destinationX;
    }

    public void setDestinationY(int destinationY) {
        this.destinationY = destinationY;
    }

    public String getLabel() {
        return label;
    }

    public int getLocationX() {
        return locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    public int getDestinationX() {
        return destinationX;
    }

    public int getDestinationY() {
        return destinationY;
    }
}
