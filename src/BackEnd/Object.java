package BackEnd;

/**
 * Data model class for the table view of the list of objects and their location destination.
 */
public class Object {

    private String label;
    private int locationX;
    private int locationY;
    private int destinationX;
    private int destinationY;

    /**
     * Constructor of the data model class Obstruction, used for the table view containing the list of objects or
     * obstructions
     * @param label The label of the given object, displayed in the GUI.
     * @param locationX The X coordinate of the location of the object.
     * @param locationY The Y coordinate of the location of the object.
     * @param destinationX The X coordinate of the destination of the object.
     * @param destinationY The Y coordinate of the destination of the object.
     *
     * @author Kerr
     */
     public Object(String label, int locationX, int locationY, int destinationX, int destinationY) {
        this.label = label;
        this.locationX = locationX;
        this.locationY = locationY;
        this.destinationX = destinationX;
        this.destinationY = destinationY;
    }

    // Setters

    /**
     * Set the X coordinate of object location
     * @param locationX The X coordinate of the location of the object.
     *
     * @author Kerr
     */
    public void setLocationX(int locationX) {
        this.locationX = locationX;
    }

    /**
     * Set the Y coordinate of object location
     * @param locationY The Y coordinate of the location of the object.
     *
     * @author Kerr
     */
    public void setLocationY(int locationY) {
        this.locationY = locationY;
    }

    /**
     * Set the Y coordinate of object destination
     * @param destinationX The X coordinate of the destination of the object.
     *
     * @author Kerr
     */
    public void setDestinationX(int destinationX) {
        this.destinationX = destinationX;
    }

    /**
     * Set the Y coordinate of object destination
     * @param destinationY The Y coordinate of the destination of the object.
     *
     * @author Kerr
     */
    public void setDestinationY(int destinationY) {
        this.destinationY = destinationY;
    }

    // Getters

    /**
     * Get the label of object
     * @return The label of the given object, displayed in the GUI.
     *
     * @author Kerr
     */
    public String getLabel() {
        return label;
    }

    /**
     * Get the X coordinate of object location
     * @return destinationY The X coordinate of the location of the object.
     *
     * @author Kerr
     */
    public int getLocationX() {
        return locationX;
    }

    /**
     * Get the Y coordinate of object location
     * @return destinationY The Y coordinate of the location of the object.
     *
     * @author Kerr
     */
    public int getLocationY() {
        return locationY;
    }

    /**
     * Get the X coordinate of object destination
     * @return destinationY The X coordinate of the destination of the object.
     *
     * @author Kerr
     */
    public int getDestinationX() {
        return destinationX;
    }

    /**
     * Get the Y coordinate of object destination
     * @return destinationY The Y coordinate of the destination of the object.
     *
     * @author Kerr
     */
    public int getDestinationY() {
        return destinationY;
    }
}