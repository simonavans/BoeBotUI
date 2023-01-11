package backEnd;

/**
 * Data model class for the table view of the list of obstructions
 */
public class Obstruction {

    private String label;
    private int locationX;
    private int locationY;

    /**
     * Constructor of the data model class Obstruction, used for the table view containing the list of obstructions or
     * obstructions
     *
     * @param label     The label of the given obstruction, displayed in the GUI.
     * @param locationX The X coordinate of the location of the obstruction.
     * @param locationY The Y coordinate of the location of the obstruction.
     * @author Kerr
     */
    public Obstruction(String label, int locationX, int locationY) {
        this.label = label;
        this.locationX = locationX;
        this.locationY = locationY;

    }

    // Setters

    /**
     * Set the X coordinate of obstruction location
     *
     * @param locationX The X coordinate of the location of the obstruction.
     * @author Kerr
     */
    public void setLocationX(int locationX) {
        this.locationX = locationX;
    }

    /**
     * Set the Y coordinate of obstruction location
     *
     * @param locationY The Y coordinate of the location of the obstruction.
     * @author Kerr
     */
    public void setLocationY(int locationY) {
        this.locationY = locationY;
    }

    // Getters

    /**
     * Get the label of obstruction
     *
     * @return The label of the given obstruction, displayed in the GUI.
     * @author Kerr
     */
    public String getLabel() {
        return label;
    }

    /**
     * Get the X coordinate of obstruction location
     *
     * @return destinationY The X coordinate of the location of the obstruction.
     * @author Kerr
     */
    public int getLocationX() {
        return locationX;
    }

    /**
     * Get the Y coordinate of obstruction location
     *
     * @return destinationY The Y coordinate of the location of the obstruction.
     * @author Kerr
     */
    public int getLocationY() {
        return locationY;
    }
}