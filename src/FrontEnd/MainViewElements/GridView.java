package FrontEnd.MainViewElements;

import BackEnd.Object;
import BackEnd.Obstruction;
import BackEnd.PathFinding.Grid;
import BackEnd.PathFinding.Node;
import BackEnd.PathFinding.PathFinder;
import FrontEnd.MainView;
import javafx.collections.ObservableList;
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

import java.util.ArrayList;
import java.util.HashMap;

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

    private ArrayList<ArrayList<int[]>> routeToInt = new ArrayList<>();
    private ArrayList<ArrayList<String>> routeToString = new ArrayList<>();

    private int stepStringNumber = 0;
    private int stepIntNumber = 0;
    private int routeNumber = 0;


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
    }

    public void updateGrid() {
        int resolutionPX = 1000;
        int resolutionPY = 800;
        int gridSize = 45;
        int gridYShift = 250;
        int gridXShift = 525;

        this.gridWidth = callback.getSettingsView().gridWidth;
        this.gridHeight = callback.getSettingsView().gridHeight;

        if (gridWidth > gridHeight) {
            this.gridSquareSize = ((gridSize * resolutionPX / 100.0) / (double) (gridWidth - 1));
        } else {
            this.gridSquareSize = ((gridSize * resolutionPX / 100.0) / (double) (gridHeight - 1));
        }

        this.gridStartPX = (resolutionPX - gridXShift - gridSquareSize * (gridWidth - 1)) / 2;
        this.gridEndPY = (resolutionPY - gridYShift) - ((resolutionPY - gridYShift) - gridSquareSize * (gridHeight - 1)) / 2;

        mainLayout.getChildren().clear();
        generateGridView();
        updateBoebotLocation();
        updateBoebotOrientation();
    }

    public void updateBoebotLocation() {
        markBoeBotLocation(callback.getSettingsView().boebotX, callback.getSettingsView().boebotY);
        updateBoebotOrientation();
    }

    public void updateBoebotOrientation() {
        int vx = callback.getSettingsView().boebotVX;
        int vy = callback.getSettingsView().boebotVY;

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
    private void generateGridView() {

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
     * @return generated layout with the grid.
     *
     * @author Kerr
     */
    public Pane getMainLayout() {
        return mainLayout;
    }

    /**
     * Getter method that returns the calculated route (in string format)
     * @return the calculated route (in string format).
     *
     * @author Kerr
     */
    public ArrayList<ArrayList<String>> getRouteToString() {
        return routeToString;
    }

    /**
     * Getter method that returns the calculated route (in integer format)
     * @return the calculated route (in integer format)
     *
     * @author Kerr
     */
    public ArrayList<ArrayList<int[]>> getRouteToInt() {
        return routeToInt;
    }

    /**
     * Getter method that returns the width of the drawn grid.
     * @return the width of the drawn grid.
     *
     * @author Kerr
     */
    public int getGridWidth() {
        return gridWidth;
    }

    /**
     * Getter method that returns the height of the drawn grid.
     * @return the height of the drawn grid.
     *
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
    private void markTraversed(int startX, int startY, int endX, int endY) {
        getLineSegment(startX, startY, endX, endY).setStroke(Color.GREEN);
    }

    /**
     * Reset all line segments to normal (black)
     *
     * @author Kerr
     */
    private void resetLineSegments() {
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
    private void markBoeBotLocation(int x, int y) {
        boebotImage.setX(convertXtoPX(x) - 12.5);
        boebotImage.setY(convertYtoPY(y) - 16);
    }

    private void rotateBoebot(int degrees) {
        boebotImage.setRotate(boebotImage.getRotate() - degrees);
    }

    /**
     * Calculates a route over the grid given a list of objects and their destinations
     *
     * @param objectList A list of objects with their location and destination
     * @author Kerr
     */
    public void calculateRoute(ObservableList<Object> objectList, ObservableList<Obstruction> obstructionList) {
        // Reset previously calculated path
        routeToInt.clear();
        routeToString.clear();

        // Configure pathfinder
        PathFinder pathFinder = callback.getPathfinder();
        Grid grid = callback.getGrid();

        ArrayList<int[][]> rearrangementList = new ArrayList<>();

        // Go through each object defined by the user
        for (Object object : objectList) {

            // Add an obstruction at the location of the object
            grid.addObstruction(object.getLocationX(), object.getLocationY());

            // add the object location and destination to the list of location-destination pairs
            rearrangementList.add(new int[][]{{object.getLocationX(), object.getLocationY()}, {object.getDestinationX(), object.getDestinationY()}});
        }

        // Go through each obstruction defined by the user
        for (Obstruction obstruction : obstructionList) {

            // Add an obstruction at the location of the obstruction
            grid.addObstruction(obstruction.getLocationX(), obstruction.getLocationY());
        }

        routeToString = new ArrayList<>();

        // For each object location-destination pairs determine the route to the object location and then the object destination
        for (int[][] destination : rearrangementList) {

            // Get the start orientation of the robot
            int startOrientationVX = pathFinder.getStartOrientationVX();
            int startOrientationVY = pathFinder.getStartOrientationVY();

            // Calculate the route to the object location and remove the object from the grid
            ArrayList<Node> result1 = pathFinder.calculateShortestPathFromSource(destination[0][0], destination[0][1], false);
            grid.removeObstruction(destination[0][0], destination[0][1]);

            // Convert the calculated route to an list of Integers and to a list of strings
            routeToInt.add(pathFinder.convertRouteToInt(result1));
            routeToString.add(pathFinder.convertRouteToString(result1, startOrientationVX, startOrientationVY, false));


            // Get the start orientation of the robot
            startOrientationVX = pathFinder.getStartOrientationVX();
            startOrientationVY = pathFinder.getStartOrientationVY();

            // Calculate the route to the object destination and add the object to the grid
            ArrayList<Node> result2 = pathFinder.calculateShortestPathFromSource(destination[1][0], destination[1][1], true);
            grid.addObstruction(destination[1][0], destination[1][1]);

            // Convert the calculated route to an list of Integers and to a list of strings
            routeToInt.add(pathFinder.convertRouteToInt(result2));
            routeToString.add(pathFinder.convertRouteToString(result2, startOrientationVX, startOrientationVY, true));
        }

        // Reset the routeNumber and stepStringNumber
        routeNumber = 0;
        stepStringNumber = 0;
        stepIntNumber = 0;

        // Show the first route
        displayUntraversedRoute(0);
        displayUntraversedRoute(1);
    }

    /**
     * Helper method that marks a sub route as untraversed (marking the entire route red)
     * @param routeNumberOffset the offset compared to the current routeNumber
     */
    private void displayUntraversedRoute(int routeNumberOffset) {
        for (int i = 0; i < routeToInt.get(routeNumber + routeNumberOffset).size() - 1; i++) {
            int x1 = routeToInt.get(routeNumber + routeNumberOffset).get(i)[0];
            int y1 = routeToInt.get(routeNumber + routeNumberOffset).get(i)[1];
            int x2 = routeToInt.get(routeNumber + routeNumberOffset).get(i + 1)[0];
            int y2 = routeToInt.get(routeNumber + routeNumberOffset).get(i + 1)[1];
            markUntraversed(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2), Math.max(y1, y2));
        }
    }


    //TODO This needs a major cleanup
    /**
     * Method that draws the next step of the current route, moving or rotating the image of the boebot accordingly,
     * marking routes as traversed/untraversed and removing/replacing objects.
     *
     * @author Kerr
     */
    public void displayNextStep() {
        boebotImage.toFront();

        // If all the routes are displayed, do not do anything
        if (routeNumber != routeToString.size()) {

            // if the current step is the last step AND this step is to Place ("P") the object AND this is not the last route, reset all line segments and show the next route
            if (stepStringNumber == routeToString.get(routeNumber).size() - 1 && routeToString.get(routeNumber).get(routeToString.get(routeNumber).size() - 1).equals("P") && routeNumber != routeToString.size() -1) {
                resetLineSegments();
                displayUntraversedRoute(1);
                displayUntraversedRoute(2);
            }
            // If the current step is the past the last step, test if the last step is NOT Place ("P"), then the step
            // must be "pick up" (this has no official command) and the object location is to be removed. No matter what
            // since this is the last step, the routeNumber needs to be increased and both step numbers are reset.
            if (stepStringNumber == routeToString.get(routeNumber).size()) {

                if (!routeToString.get(routeNumber).get(routeToString.get(routeNumber).size() - 1).equals("P")) {
                    callback.onGridViewEvent("Pick Up");
                }

                routeNumber++;
                stepStringNumber = 0;
                stepIntNumber = 0;
            }

            // If the routeNumber is not passed the last route, get the current instruction
            if (routeNumber != routeToString.size()) {
                if (routeNumber == routeToString.size() - 1 && stepStringNumber == routeToString.get(routeNumber).size() - 1) {
                    System.out.println("Hoi");
                    callback.onGridViewEvent("Finished Route");
                }

                String stepString = routeToString.get(routeNumber).get(stepStringNumber);

                // If the current instruction is to rotate right ("R") or to rotate left ("L") rotate the robot image
                // If the instruction is to move forward ("F"), mark the next path as traversed and move the robot image
                // If the instruction is to place an object ("P") add an obstruction on the place destination, remove
                // the object from the object list and add it the obstruction list.
                switch (stepString) {
                    case "R":
                        rotateBoebot(-90);
                        break;
                    case "L":
                        rotateBoebot(90);
                        break;
                    case "F":
                        stepIntNumber++;
                        int[] stepIntNew = routeToInt.get(routeNumber).get(stepIntNumber);
                        int[] stepIntPrevious = routeToInt.get(routeNumber).get(stepIntNumber - 1);
                        markTraversed(
                                Math.min(stepIntPrevious[0], stepIntNew[0]),
                                Math.min(stepIntPrevious[1], stepIntNew[1]),
                                Math.max(stepIntPrevious[0], stepIntNew[0]),
                                Math.max(stepIntPrevious[1], stepIntNew[1]));
                        markBoeBotLocation(stepIntNew[0], stepIntNew[1]);
                        break;
                    case "P":
                        callback.onGridViewEvent("Place");
                        markBoeBotLocation(routeToInt.get(routeNumber).get(stepIntNumber - 1)[0], routeToInt.get(routeNumber).get(stepIntNumber - 1)[1]);

                        if (routeNumber == routeToString.size() - 1) {
                            resetLineSegments();
                        }
                }
                // If everything is done, add one to the stepString number
                stepStringNumber++;
            }
        }
    }
}


















