package FrontEnd.MainViewElements;


import FrontEnd.MainView;
import javafx.geometry.VPos;
import javafx.scene.Group;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.List;

/**
 * Class that controls layout and functionality of the grid view, like live location of the robot, location of objects,
 * destinations, and obstructions.
 */
public class GridView {

    private Pane mainLayout = new Pane();
    private MainView callback;

    private HashMap<String, Line> lineSegments = new HashMap<>(); // Collection of all line segments of the grid
    private HashMap<String, Group> pointsOfInterestLocations = new HashMap<>(); // Collection of all PoI of the grid (objects, destinations obstructions)

    private int gridWidth; // Number of nodes in the x-direction
    private int gridHeight; // Number of nodes in the y-direction

    private double gridSquareSize; // size of each grid square (in pixels)
    private double gridEndPY; // End location of the grid on the y-axis (in pixels)
    private double gridStartPX; // Start location of the grid on the x-axis (in pixels)

    private ImageView boebotImage = new ImageView(new Image("file:boebotOpen.png"));

    /**
     * Generates a grid of a given size and initialises variables
     *
     * @param callback class to which the method should callback
     * @author Kerr
     */
    public GridView(MainView callback) {
        this.callback = callback;
        updateGrid();
        generateGridView();
        updateBoebotLocation();
        updateBoebotOrientation();
    }

    /**
     * Recalculate the attributes of the GridView based on the changed size of the grid
     *
     * @author Kerr
     */
    public void updateGrid() {
        int resolutionPX = 1200;
        int resolutionPY = 1000;
        int gridSize = 42;
        int gridYShift = 320;
        int gridXShift = 700;

        this.gridWidth = callback.getSettingsDialog().gridWidth;
        this.gridHeight = callback.getSettingsDialog().gridHeight;

        if (gridWidth > gridHeight) {
            this.gridSquareSize = ((gridSize * resolutionPX / 100.0) / (double) (gridWidth - 1));
        } else {
            this.gridSquareSize = ((gridSize * resolutionPX / 100.0) / (double) (gridHeight - 1));
        }

        this.gridStartPX = (resolutionPX - gridXShift - gridSquareSize * (gridWidth - 1)) / 2;
        this.gridEndPY = (resolutionPY - gridYShift) - ((resolutionPY - gridYShift) - gridSquareSize * (gridHeight - 1)) / 2;

        mainLayout.getChildren().clear();
    }

    /**
     * Update the location of the boebot
     *
     * @author Kerr
     */
    public void updateBoebotLocation() {
        markBoeBotLocation(callback.getSettingsDialog().boebotX, callback.getSettingsDialog().boebotY);
    }

    /**
     * Update the orientation of the boebot
     *
     * @author Kerr
     */
    public void updateBoebotOrientation() {
        int vx = callback.getSettingsDialog().boebotVX;
        int vy = callback.getSettingsDialog().boebotVY;

        if (vx == 0 && vy == 1) {
            boebotImage.setRotate(0);
        }
        if (vx == 1 && vy == 0) {
            boebotImage.setRotate(90);
        }
        if (vx == 0 && vy == -1) {
            boebotImage.setRotate(180);
        }
        if (vx == -1 && vy == 0) {
            boebotImage.setRotate(270);
        }
    }

    /**
     * Generate a grid of a given size and the given location.
     *
     * @author Kerr
     */
    public void generateGridView() {

        // Create the horizontal line segments
        for (int nodeY = 0; nodeY < gridHeight; nodeY++) {
            for (int nodeX = 0; nodeX < gridWidth - 1; nodeX++) {

                // Create a line segment and add the line segment to the layout
                Line line = new Line(convertXtoPX(nodeX), convertYtoPY(nodeY), convertXtoPX(nodeX + 1), convertYtoPY(nodeY));
                line.setStrokeWidth(3);
                line.setStroke(Color.gray(0.75));
                mainLayout.getChildren().addAll(line);

                // Add the line segment to the collection of line segments for future reference
                lineSegments.put(nodeX + "-" + nodeY + " to " + (nodeX + 1) + "-" + nodeY, line);
            }
        }

        // Create the vertical line segments
        for (int nodeX = 0; nodeX < gridWidth; nodeX++) {
            for (int nodeY = 0; nodeY < gridHeight - 1; nodeY++) {

                // Create a line segment and add the line segment to the layout
                Line line = new Line(convertXtoPX(nodeX), convertYtoPY(nodeY), convertXtoPX(nodeX), convertYtoPY(nodeY + 1));
                line.setStrokeWidth(3);
                line.setStroke(Color.gray(0.75));
                mainLayout.getChildren().addAll(line);

                // Add the line segment to the collection of line segments for future reference
                lineSegments.put(nodeX + "-" + nodeY + " to " + nodeX + "-" + (nodeY + 1), line);
            }
        }
        //Add the image of the robot to the grid
        mainLayout.getChildren().addAll(boebotImage);
    }

    //Getters

    /**
     * Getter method that returns the mainLayout, the layout containing the complete grid.
     *
     * @return generated layout with the grid.
     * @author Kerr
     */
    public Pane getMainLayout() {
        return mainLayout;
    }

    /**
     * Getter method that returns the width of the drawn grid.
     *
     * @return the width of the drawn grid.
     * @author Kerr
     */
    public int getGridWidth() {
        return gridWidth;
    }

    /**
     * Getter method that returns the height of the drawn grid.
     *
     * @return the height of the drawn grid.
     * @author Kerr
     */
    public int getGridHeight() {
        return gridHeight;
    }

    /**
     * Helper getter method that returns a line segment of the grid
     *
     * @param startX X location of the start node of the line segment.
     * @param startY Y location of the start node of the line segment.
     * @param endX   X location of the end node of the line segment.
     * @param endY   Y location of the end node of the line segment.
     * @return the line segment between the two nodes
     * @author Kerr
     */
    private Line getLineSegment(int startX, int startY, int endX, int endY) {
        return lineSegments.get(startX + "-" + startY + " to " + endX + "-" + endY);
    }

    /**
     * Helper method that converts the x-location of an node into the x-location in pixels
     *
     * @param x the x-location of the node
     * @return the x-location of the node in pixels
     * @author Kerr
     */
    private double convertXtoPX(int x) {
        // Start location of the grid on the x-axis (in pixels)
        return (gridStartPX + x * gridSquareSize);
    }

    /**
     * Helper method that converts the y-location of an node into the y-location in pixels
     *
     * @param y the x-location of the node
     * @return the x-location of the node in pixels
     * @author Kerr
     */
    private double convertYtoPY(int y) {
        return (gridEndPY - y * gridSquareSize);
    }


    /**
     * Marks a given line segment as 'untraversed' (red), meaning the robot this not pass this path yet.
     *
     * @param startX X location of the start node of the line segment.
     * @param startY Y location of the start node of the line segment.
     * @param endX   X location of the end node of the line segment.
     * @param endY   Y location of the end node of the line segment.
     * @author Kerr
     */
    private void markUntraversed(int startX, int startY, int endX, int endY) {
        getLineSegment(startX, startY, endX, endY).setStroke(Color.rgb(198, 0, 48));
    }

    /**
     * Marks a given line segment as 'traversed' (green), meaning the robot passed this path.
     *
     * @param startX X location of the start node of the line segment.
     * @param startY Y location of the start node of the line segment.
     * @param endX   X location of the end node of the line segment.
     * @param endY   Y location of the end node of the line segment.
     * @author Kerr
     */
    public void markTraversed(int startX, int startY, int endX, int endY) {
        getLineSegment(startX, startY, endX, endY).setStroke(Color.GREEN);
    }

    /**
     * Helper method that marks a sub route as untraversed (marking the entire route red)
     * @param route list of coordinates the boebot passes
     *
     * @author Kerr
     */
    public void displayUntraversedRoute(List<int[]> route) { //TODO fix this method
        resetLineSegments();
        for (int i = 0; i < route.size() - 1; i++) {
            int x1 = route.get(i)[0];
            int y1 = route.get(i)[1];
            int x2 = route.get(i + 1)[0];
            int y2 = route.get(i + 1)[1];
            markUntraversed(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2), Math.max(y1, y2));
        }
    }

    /**
     * Reset all line segments to normal (black)
     *
     * @author Kerr
     */
    public void resetLineSegments() {
        for (Line line : lineSegments.values()) {
            line.setStroke(Color.gray(0.75));
        }
    }

    /**
     * Helper method that creates a centered text label given a node.
     *
     * @param x     X location of the node.
     * @param y     Y location of the node.
     * @param label text that should be displayed
     * @return Text object that is centered at (x,y)
     * @author Kerr
     */
    private Text createCenteredText(int x, int y, String label, boolean isWhite) {
        Text text = new Text(label);
        Font font = Font.font("Verdana", FontWeight.BOLD, 13);
        text.setFont(font);
        text.setX(convertXtoPX(x) - text.prefWidth(-1) / 2);
        text.setY(convertYtoPY(y));
        text.setTextOrigin(VPos.CENTER);

        if (isWhite) {
            text.setFill(Color.WHITE);
        }
        return text;
    }

    /**
     * Put an object on a given node, represented by a red circle.
     *
     * @param x     X location of the node.
     * @param y     Y location of the node.
     * @param label name of the object
     * @author Kerr
     */
    public void markObjectLocation(int x, int y, String label) {
        Circle circle = new Circle(convertXtoPX(x), convertYtoPY(y), 15, Color.rgb(198, 0, 48));
        Group group = new Group(circle, createCenteredText(x, y, label, true));
        mainLayout.getChildren().addAll(group);
        pointsOfInterestLocations.put(x + "-" + y, group);
    }

    /**
     * Mark a node as a destination of an object, represented by a white circle with a red outline.
     *
     * @param x     X location of the node.
     * @param y     Y location of the node.
     * @param label name of the object
     * @author Kerr
     */
    public void markObjectDestination(int x, int y, String label) {
        Circle circle = new Circle(convertXtoPX(x), convertYtoPY(y), 15, Color.WHITE);
        circle.setStroke(Color.rgb(198, 0, 48));
        circle.setStrokeWidth(2);

        Group group = new Group(circle, createCenteredText(x, y, label, false));
        mainLayout.getChildren().addAll(group);

        pointsOfInterestLocations.put(x + "-" + y, group);
    }

    /**
     * Put an obstruction on a given node, represented by a gray circle.
     *
     * @param x X location of the node.
     * @param y Y location of the node.
     * @author Kerr
     */
    public void markObstructionLocation(int x, int y, String label) {
        Circle circle = new Circle(convertXtoPX(x), convertYtoPY(y), 15, Color.GRAY);

        Group group = new Group(circle, createCenteredText(x, y, label, true));
        mainLayout.getChildren().addAll(group);

        pointsOfInterestLocations.put(x + "-" + y, group);
    }

    /**
     * Remove any point of interest (object location, destination or obstruction) from the give node
     *
     * @param x X location of the node.
     * @param y Y location of the node.
     * @author Kerr
     */
    public void deletePointOfInterest(int x, int y) {
        mainLayout.getChildren().remove(pointsOfInterestLocations.get(x + "-" + y));
        pointsOfInterestLocations.remove(x + "-" + y);
    }

    /**
     * Mark the location of the robot given a node, represented by a blue square
     *
     * @param x X location of the node.
     * @param y Y location of the node.
     * @author Kerr
     */
    public void markBoeBotLocation(int x, int y) {
        boebotImage.setX(convertXtoPX(x) - 12.5);
        boebotImage.setY(convertYtoPY(y) - 16);
        boebotImage.toFront();
    }

    public void rotateBoebot(int degrees) {
        boebotImage.setRotate(boebotImage.getRotate() - degrees);
        boebotImage.toFront();
    }
}