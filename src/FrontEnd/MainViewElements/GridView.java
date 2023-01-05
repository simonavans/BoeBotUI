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

    private ArrayList<int[]> routeToInt = new ArrayList<>();
    private ArrayList<String> routeToString = new ArrayList<>();

    private int stepNumberString = 0;
    private int stepNumberInt = 0;
    private int routeNumber = 1;


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

    //TODO docstring the methods below
    public void updateGrid() {
        int resolutionPX = 1000;
        int resolutionPY = 800;
        int gridSize = 42;
        int gridYShift = 250;
        int gridXShift = 590;

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
        generateGridView();
        updateBoebotLocation();
        updateBoebotOrientation();
    }

    public void updateBoebotLocation() {
        markBoeBotLocation(callback.getSettingsDialog().boebotX, callback.getSettingsDialog().boebotY);
        updateBoebotOrientation();
    }

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
    public boolean calculateRoute(ObservableList<Object> objectList, ObservableList<Obstruction> obstructionList) {

        // Configure pathfinder
        PathFinder pathFinder = callback.getPathfinder(); //TODO does this really need to be called back like this?
        Grid grid = callback.getGrid();

        // Reset previously calculated path and grid
        routeToInt.clear();
        routeToString.clear();
        grid.resetObstructions();

        // Get initial start location and orientation in case finding a path fails
        int initialX = pathFinder.getStartX();
        int initialY = pathFinder.getStartY();
        int initialVX = pathFinder.getStartOrientationVX();
        int initialVY = pathFinder.getStartOrientationVY();

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

            // Check if a path has been found, if not, return false
            if (pathIsFound(pathFinder, initialX, initialY, initialVX, initialVY, result1)) return false;

            grid.removeObstruction(destination[0][0], destination[0][1]);

            // Convert the calculated route to an list of Integers and to a list of strings and add them to the routeToInt and routeToString

            routeToInt.addAll(pathFinder.convertRouteToInt(result1));
            routeToString.addAll(pathFinder.convertRouteToString(result1, startOrientationVX, startOrientationVY));

            // Since the first route is always to pickup an item, add the pickup command to the end the list of commands
            routeToString.add("Pickup");

            // Get the start orientation of the robot
            startOrientationVX = pathFinder.getStartOrientationVX();
            startOrientationVY = pathFinder.getStartOrientationVY();

            // Calculate the route to the object destination and add the object to the grid
            ArrayList<Node> result2 = pathFinder.calculateShortestPathFromSource(destination[1][0], destination[1][1], true);

            // Check if a path has been found, if not, return false
            if (pathIsFound(pathFinder, initialX, initialY, initialVX, initialVY, result2)) return false;

            grid.addObstruction(destination[1][0], destination[1][1]);

            // Convert the calculated route to an list of Integers and to a list of strings and add them to the routeToInt and routeToString
            routeToInt.addAll(pathFinder.convertRouteToInt(result2));
            routeToString.addAll(pathFinder.convertRouteToString(result2, startOrientationVX, startOrientationVY));

            // Since the first route is always to pickup an item, add the pickup command to the end the list of commands
            routeToString.add("Place");

        }

        // Reset the stepNumberString
        stepNumberString = 0;
        stepNumberInt = 0;
        routeNumber = 1;

        // Show the first route
        displayUntraversedRoute();
        transmitNextStep();

        return true;
    }

    /**
     * Helper method that checks if a path is found and if not, resets the start location and orientation of the robot
     * @param pathFinder The pathfinder object used for the calculating the current route
     * @param initialX the initial X location of the robot before starting the route
     * @param initialY the initial Y location of the robot before starting the route
     * @param initialVX the initial X component of the orientation vector of the robot before starting the route
     * @param initialVY the initial Y component of the orientation vector of the robot before starting the route
     * @param result the calculated route
     *
     * @return true = a route has been found or false = a route has not been found
     */
    private boolean pathIsFound(PathFinder pathFinder, int initialX, int initialY, int initialVX, int initialVY, ArrayList<Node> result) {
        if (result == null) {
            pathFinder.setStartX(initialX);
            pathFinder.setStartY(initialY);
            pathFinder.setStartOrientationVX(initialVX);
            pathFinder.setStartOrientationVY(initialVY);
            return true;
        }
        return false;
    }


    /**
     * Helper method that gets the index of the nth occurrence of the value null in the routeToInt.
     * @param n nth occurrence of the value null to search for
     * @return index of the nth occurrence of the value null
     */
    private int getNthIndex(int n) {
        for (int i = 0; i < routeToInt.size(); i++) {
            if (routeToInt.get(i) == null) {
                n--;
                if (n == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Helper method that marks a sub route as untraversed (marking the entire route red)
     *
     * @author Kerr
     */
    private void displayUntraversedRoute() {
        resetLineSegments();
        List<int[]> route = routeToInt.subList(stepNumberInt, getNthIndex(routeNumber));
        for (int i = 0; i < route.size() - 1; i++) {
            int x1 = route.get(i)[0];
            int y1 = route.get(i)[1];
            int x2 = route.get(i + 1)[0];
            int y2 = route.get(i + 1)[1];
            markUntraversed(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2), Math.max(y1, y2));
        }
    }

    /**
     * Method that draws the next step of the current route, moving or rotating the image of the boebot accordingly,
     * marking routes as traversed/untraversed and removing/replacing objects.
     *
     * @author Kerr
     */
    public void displayNextStep() {
        // If the not the entire route has been shown, show the next step
        if (stepNumberString != routeToString.size()) {
            String command = routeToString.get(stepNumberString);


            if (command.equals("Pickup")) {
                // If the current command is "Pickup", pick the object on the location of the boebot up
                callback.onGridViewEvent("Pick Up");
                stepNumberString++;
                stepNumberInt += 2;
                routeNumber++;

                // Display the next route
                displayUntraversedRoute();

                // Update the command to the next step (2 steps further)
                command = routeToString.get(stepNumberString);
            }

            switch (command) {
                case "Right" :
                    // Rotate the boebot to the right
                    rotateBoebot(-90);
                    break;

                case "Left" :
                    // Rotate the boebot to the left
                    rotateBoebot(90);
                    break;

                case "Forward" :
                    // Get the old and new location of the robot
                    int[] oldLocation = routeToInt.get(stepNumberInt);
                    int[] newLocation = routeToInt.get(stepNumberInt + 1);

                    // Mark the line between the old and new location as traversed
                    markTraversed(
                            Math.min(newLocation[0], oldLocation[0]),
                            Math.min(newLocation[1], oldLocation[1]),
                            Math.max(newLocation[0], oldLocation[0]),
                            Math.max(newLocation[1], oldLocation[1]));

                    // Put the robot on the new location
                    markBoeBotLocation(newLocation[0], newLocation[1]);

                    // Change the location of the boebot in the settings
                    callback.getSettingsDialog().boebotX = newLocation[0];
                    callback.getSettingsDialog().boebotY = newLocation[1];
                    stepNumberInt++;
                    break;

                case "Place" :
                    // If the current command is "Place", place the object on the location of the boebot
                    callback.onGridViewEvent("Place");
                    markBoeBotLocation(routeToInt.get(stepNumberInt - 1)[0], routeToInt.get(stepNumberInt - 1)[1]);

                    // Change the location of the boebot in the settings
                    callback.getSettingsDialog().boebotX = routeToInt.get(stepNumberInt - 1)[0];
                    callback.getSettingsDialog().boebotY = routeToInt.get(stepNumberInt - 1)[1];

                    stepNumberInt += 2;
                    routeNumber++;

                    if (stepNumberString != routeToString.size() - 1) {
                        // If this is not the last route, display the next route
                        displayUntraversedRoute();
                    } else {
                        // Else clear the GridView and enable menu items again
                        resetLineSegments();
                        callback.onGridViewEvent("Finished Route");
                    }

                    break;
            }
            stepNumberString++;
            boebotImage.toFront();
        }
    }

    /**
     * Method that transmits the next step in the list of instructions to the robot
     *
     * @author Kerr
     */
    public void transmitNextStep() {
        if (stepNumberString != routeToString.size()) {
            String nextStep = routeToString.get(stepNumberString);

            // If the current step is to pick an object up, skip this step (this step is only used internally and not
            // by the boebot itself)
            if (nextStep.equals("Pickup")) {
                callback.onAutomaticControlEvent(routeToString.get(stepNumberString + 1));
            } else {
                callback.onAutomaticControlEvent(nextStep);
            }
        }
    }
}


















