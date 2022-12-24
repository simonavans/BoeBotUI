package Application;

import javafx.geometry.VPos;
import javafx.scene.Group;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.HashMap;

// TODO make a legend (do when shape format is final)
// TODO improve format of: grid, untraversed path, traversed path, object location, object destination, obstruction
// TODO change the robot shape to an image of the robot.
// TODO add functionality: connect object list to visual representation.
// TODO add functionality: connect calculated path to visual representation.
//TODO add functionality: update robot location live.

/**
 * Class that controls layout and functionality of the grid view, like live location of the robot, location of objects,
 * destinations, and obstructions.
 */
public class GridView {

    private Pane mainLayout = new Pane();

    private HashMap<String, Line> lineSegments = new HashMap<>(); // Collection of all line segments of the grid
    private HashMap<String, Group> pointsOfInterestLocations = new HashMap<>(); // Collection of all PoI of the grid (objects, destinations obstructions)
    private Rectangle rectangle = new Rectangle(24, 24, Color.BLUE);

    private int gridWidth; // Number of nodes in the x-direction
    private int gridHeight; // Number of nodes in the y-direction

    private double gridStartPX; // Start location of the grid on the x-axis (in pixels)
    private double gridSquareSize; // size of each grid square (in pixels)
    private double gridEndPY; // End location of the grid on the y-axis (in pixels)

    /**
     * Generates a grid of a given size and initialises variables
     * @param resolutionPX resolution in the x-direction of the window
     * @param resolutionPY resolution in the y-direction of the window
     * @param gridWidth number of nodes in the x-direction
     * @param gridHeight number of nodes in the y-direction
     * @param gridStartPX // Start location of the grid on the x-axis (in pixels)
     * @param gridSize // size of the grid as a percentage of the x-resolution
     *
     * @author Kerr
     */
    GridView(int resolutionPX, int resolutionPY, int gridWidth, int gridHeight, int gridStartPX, int gridSize) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;

        this.gridStartPX = gridStartPX;
        this.gridSquareSize = ((gridSize * resolutionPX / 100.0) / (double) (gridWidth - 1));
        this.gridEndPY = (resolutionPY - 65) - ((resolutionPY - 65) - gridSquareSize * (gridHeight - 1)) / 2;

        generateGridView();
    }

    /**
     * Generate a grid of a given size and the given location.
     *
     * @author Kerr
     */
    private void generateGridView() {

        // Create the horizontal line segments
        for (int nodeY = 0; nodeY < gridHeight; nodeY++) {
            for (int nodeX = 0; nodeX < gridWidth - 1; nodeX++) {

                // Create a line segment and add the line segment to the layout
                Line line = new Line(convertXtoPX(nodeX), convertYtoPY(nodeY) , convertXtoPX(nodeX + 1), convertYtoPY(nodeY));
                mainLayout.getChildren().addAll(line);

                // Add the line segment to the collection of line segments for future reference
                lineSegments.put(nodeX + "-" + nodeY + " to " + (nodeX + 1) + "-" + nodeY, line);
            }
        }

        // Create the vertical line segments
        for (int nodeX = 0; nodeX < gridWidth; nodeX++) {
            for (int nodeY = 0; nodeY < gridHeight - 1; nodeY++) {

                // Create a line segment and add the line segment to the layout
                Line line = new Line(convertXtoPX(nodeX), convertYtoPY(nodeY) , convertXtoPX(nodeX), convertYtoPY(nodeY + 1));
                mainLayout.getChildren().addAll(line);

                // Add the line segment to the collection of line segments for future reference
                lineSegments.put(nodeX + "-" + nodeY + " to " + nodeX + "-" + (nodeY + 1), line);
            }
        }
    }

    //Getters

    /**
     * Getter method that returns the mainLayout, the layout containing the complete grid.
     * @return generated layout with the grid.
     *
     * @author Kerr
     */
    Pane getMainLayout() {
        return mainLayout;
    }

    /**
     * Helper getter method that returns a line segment of the grid
     * @param startX X location of the start node of the line segment.
     * @param startY Y location of the start node of the line segment.
     * @param endX X location of the end node of the line segment.
     * @param endY Y location of the end node of the line segment.
     * @return the line segment between the two nodes
     *
     * @author Kerr
     */
    private Line getLineSegment(int startX, int startY, int endX, int endY) {
        return lineSegments.get(startX + "-" + startY + " to " + endX + "-" + endY);
    }

    /**
     * Helper method that converts the x-location of an node into the x-location in pixels
     * @param x the x-location of the node
     * @return the x-location of the node in pixels
     *
     * @author Kerr
     */
    private double convertXtoPX(int x) {
       return (gridStartPX + x * gridSquareSize);
    }

    /**
     * Helper method that converts the y-location of an node into the y-location in pixels
     * @param y the x-location of the node
     * @return the x-location of the node in pixels
     *
     * @author Kerr
     */
    private double convertYtoPY(int y) {
        return (gridEndPY - y *gridSquareSize);
    }


    /**
     * Marks a given line segment as 'untraversed' (red), meaning the robot this not pass this path yet.
     * @param startX X location of the start node of the line segment.
     * @param startY Y location of the start node of the line segment.
     * @param endX X location of the end node of the line segment.
     * @param endY Y location of the end node of the line segment.
     *
     * @author Kerr
     */
    void markUntraversed(int startX, int startY, int endX, int endY) {
        getLineSegment(startX, startY, endX, endY).setStroke(Color.RED);
    }

    /**
     * Marks a given line segment as 'traversed' (green), meaning the robot passed this path.
     * @param startX X location of the start node of the line segment.
     * @param startY Y location of the start node of the line segment.
     * @param endX X location of the end node of the line segment.
     * @param endY Y location of the end node of the line segment.
     *
     * @author Kerr
     */
    void markTraversed(int startX, int startY, int endX, int endY) {
        getLineSegment(startX, startY, endX, endY).setStroke(Color.GREEN);
    }

    /**
     * Reset all line segments to normal (black)
     *
     * @author Kerr
     */
    public void resetLineSegments(){
        for (Line line : lineSegments.values()) {
            line.setStroke(Color.BLACK);
        }
    }


    /**
     * Helper method that creates a centered text label given a node.
     * @param x X location of the node.
     * @param y Y location of the node.
     * @param label text that should be displayed
     * @return Text object that is centered at (x,y)
     *
     * @author Kerr
     */
    private Text createCenterdText(int x, int y, String label) {
        Text text = new Text(label);
        text.setX(convertXtoPX(x) - text.prefWidth(-1) / 2);
        text.setY(convertYtoPY(y));
        text.setTextOrigin(VPos.CENTER);

        return text;
    }

    /**
     * Put an object on a given node, represented by a red circle.
     * @param x X location of the node.
     * @param y Y location of the node.
     * @param label name of the object
     *
     * @author Kerr
     */
    void markObjectLocation(int x, int y, String label) {
        Circle circle = new Circle(convertXtoPX(x), convertYtoPY(y), 15, Color.RED);

        Group group = new Group(circle, createCenterdText(x, y, label));
        mainLayout.getChildren().addAll(group);

        pointsOfInterestLocations.put(x + "-" + y, group);
    }

    /**
     * Mark a node as a destination of an object, represented by a white circle with a red outline.
     * @param x X location of the node.
     * @param y Y location of the node.
     * @param label name of the object
     *
     * @author Kerr
     */
    void markObjectDestination(int x, int y, String label) {
        Circle circle = new Circle(convertXtoPX(x), convertYtoPY(y), 15, Color.WHITE);
        circle.setStroke(Color.RED);

        Group group = new Group(circle, createCenterdText(x, y, label));
        mainLayout.getChildren().addAll(group);

        pointsOfInterestLocations.put(x + "-" + y, group);
    }

    /**
     * Put an obstruction on a given node, represented by a gray circle.
     * @param x X location of the node.
     * @param y Y location of the node.
     * @param label name of the object
     *
     * @author Kerr
     */
    void markObstructionLocation(int x, int y, String label) {
        Circle circle = new Circle(convertXtoPX(x), convertYtoPY(y), 15, Color.GRAY);

        Group group = new Group(circle, createCenterdText(x, y, label));
        mainLayout.getChildren().addAll(group);

        pointsOfInterestLocations.put(x + "-" + y, group);
    }

    /**
     * Remove any point of interest (object location, destination or obstruction) from the give node
     * @param x X location of the node.
     * @param y Y location of the node.
     *
     * @author Kerr
     */
    void deletePointOfInterest (int x, int y) {
        pointsOfInterestLocations.remove(x + "-" + y);
    }

    /**
     * Mark the location of the robot given a node, represented by a blue square
     * @param x X location of the node.
     * @param y Y location of the node.
     *
     * @author Kerr
     */
    void markBoeBotLocation (int x, int y) {
        rectangle.setX(convertXtoPX(x) - 12); // 12 is half its width
        rectangle.setY(convertYtoPY(y) - 12);

        mainLayout.getChildren().addAll(rectangle);
    }
}