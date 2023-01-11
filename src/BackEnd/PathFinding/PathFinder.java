package backend.pathfinding;

import backend.Object;
import backend.Obstruction;
import frontend.MainView;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * Class that implements a path finding algorithm based on the Dijkstra algorithm. Inspired by
 * https://www.baeldung.com/java-dijkstra
 */
public class PathFinder {

    private MainView callback;
    private Graph grid;

    private int startX;
    private int startY;
    private int startOrientationVX;
    private int startOrientationVY;

    private int turnWeight;
    private int forwardWeight;

    private ArrayList<int[]> routeToInt = new ArrayList<>();
    private ArrayList<String> routeToString = new ArrayList<>();

    /**
     * Constructs a pathfinder given a certain graph, start location and start orientation.
     *
     * @param callback class to which the method should callback
     *
     * @author Kerr
     */
    public PathFinder(MainView callback) {
        this.callback = callback;
        updateGrid();
        updateStartLocation();
        updateStartOrientation();
        updateWeights();
    }

    /**
     * Method that updates the grid to the current size.
     *
     * @author Kerr
     */
    public void updateGrid() {
        this.grid = callback.getGrid();
    }

    /**
     * Method that updates the start location of the robot to the current location.
     *
     * @author Kerr
     */
    public void updateStartLocation() {
        this.startX = callback.getSettingsDialog().boebotX;
        this.startY = callback.getSettingsDialog().boebotY;
    }

    /**
     * Method that updates the start orientation of the robot to the current orientation.
     *
     * @author Kerr
     */
    public void updateStartOrientation() {
        this.startOrientationVX = callback.getSettingsDialog().boebotVX;
        this.startOrientationVY = callback.getSettingsDialog().boebotVY;
    }

    /**
     * Method that updates the weights of moving forward/turning to the current weights.
     *
     * @author Kerr
     */
    public void updateWeights() {
        this.turnWeight = callback.getSettingsDialog().turnWeight;
        this.forwardWeight = callback.getSettingsDialog().forwardWeight;
    }

    /**
     * Getter method that returns the X component of the location of the robot.
     * @return X component of the location of the robot.
     *
     * @author Kerr
     */
    public int getStartX() { return startX;}

    /**
     * Getter method that returns the Y component of the location of the robot.
     * @return Y component of the location of the robot.
     *
     * @author Kerr
     */
    public int getStartY() { return startY; }

    /**
     * Getter method that returns the X component of the orientation vector of the robot.
     * @return X component of the orientation vector of the robot.
     *
     * @author Kerr
     */
    public int getStartOrientationVX() {return startOrientationVX;}

    /**
     * Getter method that returns the Y component of the orientation vector of the robot.
     * @return Y component of the orientation vector of the robot.
     *
     * @author Kerr
     */
    public int getStartOrientationVY() {return startOrientationVY;}

    /**
     * Getter method that returns the weight of making a corner.
     * @return the weight of making a corner.
     *
     * @author Kerr
     */
    public int getTurnWeight() {return turnWeight;}

    /**
     * Getter method that returns the weight of moving forward.
     * @return the weight of making moving forward.
     *
     * @author Kerr
     */
    public int getForwardWeight() { return forwardWeight;}

    /**
     * Getter method that returns the route converted to a list of integers arrays in the format of {x,y}
     * @return route converted to a list of integers arrays in the format of {x,y}
     *
     * @author Kerr
     */
    public ArrayList<int[]> getRouteToInt() {return routeToInt;}

    /**
     * Getter method that returns the route to a list of string instruction in the format
     * Left = turn left, Right = turn right, Forward = move forward, place = place object, pickup = pick up object
     * @return a list of string instruction
     */
    public ArrayList<String> getRouteToString() {return routeToString; }

    /**
     * Helper method that calculates the shortest route (the route that takes the leas amount of time to complete) between the robots
     * current location and its destination given by an x and y coordinate
     *
     * @param destinationX the x coordinate of the destination
     * @param destinationY the y coordinate of the destination
     * @param dropOff tell the Pathfinder if it concerns a route to drop an object of or not. False makes it behave more
     * like a traditional pathfinder
     * @return an ArrayList with Strings which guide the robot through the shortest route. If no route is found
     * (meaning the destination is unreachable), return null
     *
     * @author Kerr
     */
    private ArrayList<Node> calculateShortestPathFromSource(int destinationX, int destinationY, boolean dropOff) {

        // Get the node from which the robot starts
        Node startNode = grid.getNode(startX, startY);

        // Reset residue from possible previous executions
        grid.resetNodeDistance();
        grid.resetNodeShortestPath();
        startNode.setDistance(0);

        // Create a list of settled nodes, which are nodes which have been visited
        ArrayList<Node> settledNodes = new ArrayList<>();
        // Create a list of unsettled nodes, which are nodes which have not been visited yet.
        ArrayList<Node> unsettledNodes = new ArrayList<>();

        unsettledNodes.add(startNode);
        Node destinationNode = grid.getNode(destinationX, destinationY);

        // if the list of unsettled nodes is empty, all possible routes are found and the destination is deemed unreachable
        while (unsettledNodes.size() != 0) {

            Node currentNode = getLowestDistanceNode(unsettledNodes); // get the node with the smallest path weight
            unsettledNodes.remove(currentNode);

            // go through all nodes adjacent to the current node and update their shortest route if a shorter route is found
            for (Node adjacentNode : currentNode.getAdjacentNodes()) {

                // If the node is already settled, do not check it. Do not check it either if the node is blocked by an
                // object UNLESS this node is the destination (then the object needs to be picked up)
                if (!settledNodes.contains(adjacentNode) && !adjacentNode.isObstructed() || adjacentNode.equals(destinationNode)) {
                    Integer edgeWeight = calculateAngle( calculateVectors(currentNode, adjacentNode)) / 90 * turnWeight + forwardWeight;
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }

            settledNodes.add(currentNode);

            // If the currentNode is the destination node, the shortest route (should) be found and this route can be returned
            if (currentNode.equals(destinationNode)) {

                ArrayList<Node> route = destinationNode.getShortestPath();

                // Update the location and orientation of the robot to its final orientation and destination
                Node previousNode = destinationNode.getShortestPath().get(destinationNode.getShortestPath().size() - 1);
                startOrientationVY = currentNode.getY() - previousNode.getY();
                startOrientationVX = currentNode.getX() - previousNode.getX();

                if (dropOff) {
                    startX = previousNode.getX();
                    startY = previousNode.getY();
                } else {
                    startX = destinationX;
                    startY = destinationY;
                }

                route.add(destinationNode);
                return route;
            }
        }
        return null;
    }

    /**
     * Helper method that searches through the list of unsettledNodes and returns the node with the lowest distance from
     * the start node.
     *
     * @param unsettledNodes the ArrayList of unsettled (unvisited) nodes
     * @return the node with the lowest distance from the start node.
     *
     * @author Kerr
     */
    private Node getLowestDistanceNode(ArrayList<Node> unsettledNodes) {
        // declare helper variables
        Node lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;

        for (Node node : unsettledNodes) {
            int nodeDistance = node.getDistance();

            // if the current node has the lowest distance so far, set it as the lowestDistanceNode and set its distance
            // as the lowest distance
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    /**
     * Helper method that calculates if the current route from the sourceNode to the evaluation node is the shortest path.
     * If so, the shortestPath and distance to that node are updated to be the current route.
     *
     * @param evaluationNode the node which a new route has been found, which requires evaluation
     * @param edgeWeigh the edge weight of the newly found connection
     * @param sourceNode the node from which the newly found connection is found
     *
     * @author Kerr
     */
    private void calculateMinimumDistance(Node evaluationNode, Integer edgeWeigh, Node sourceNode) {
        Integer sourceDistance = sourceNode.getDistance();

        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            ArrayList<Node> shortestPath = new ArrayList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }

    /**
     * Helper method that returns an array of integers representing the direction vectors of the route before and after
     * driving between two nodes. based on the start orientation of the robot.
     *
     * @param currentNode the current location of the robot
     * @param destinationNode the location of an node adjacent to the current node
     * @return an array of integers representing the direction vectors of the route before and after riving between two nodes.
     *
     * @author Kerr
     */
    private int[] calculateVectors(Node currentNode, Node destinationNode) {
        return calculateVectors(currentNode, destinationNode, startOrientationVX, startOrientationVY);
    }

    /**
     * Helper method that returns an array of integers representing the direction vectors of the route before and after
     * driving between two nodes. based on the start orientation of the robot.
     *
     * @param currentNode the current location of the robot
     * @param destinationNode the location of an node adjacent to the current node
     * @param startOrientationVX X component of the start orientation of the robot
     * @param startOrientationVY Y component of the start orientation of the robot
     * @return an array of integers representing the direction vectors of the route before and after riving between two nodes.
     *
     * @author Kerr
     */
    private int[] calculateVectors(Node currentNode, Node destinationNode, int startOrientationVX, int startOrientationVY) {

        // Define the node from which the robot came, together with its orientation vector x and y component
        Node previousNode;
        int previousNodeVX;
        int previousNodeVY;

        // Define the orientation vector x and y of the robot if it moved to the adjacent node
        int destinationNodeNodeVX = destinationNode.getX() - currentNode.getX();
        int destinationNodeVY = destinationNode.getY() - currentNode.getY();

        // if the robot is not in its starting position, define the orientation of the robot by its previous location
        // and where it moved to. Else, define the orientation as its starting orientation
        if (currentNode.getDistance() != 0) {
            previousNode = currentNode.getShortestPath().get(currentNode.getShortestPath().size() - 1);
            previousNodeVX = currentNode.getX() - previousNode.getX();
            previousNodeVY = currentNode.getY() - previousNode.getY();
        } else {
            previousNodeVX = startOrientationVX;
            previousNodeVY = startOrientationVY;
        }
        return new int[]{previousNodeVX, previousNodeVY, destinationNodeNodeVX, destinationNodeVY};
    }

    /**
     * Helper method that calculates the angle between to vectors
     * @param vectors list of two vectors in the format {x1, y1, x2, y2}
     * @return the angle in degrees between the two vectors
     *
     * @author Kerr
     */
    private int calculateAngle(int[] vectors) {
        int dotProduct = (vectors[0] * vectors[2] + vectors[1] * vectors[3]);
        return (int) (Math.acos(dotProduct) * 180 / Math.PI);
    }

    /**
     * Helper method that converts a route (given as a list of nodes the robot passes) to a list of coordinates in the format {x, y}.
     * @param route a list of nodes the robot passes.
     * @return an ArrayList of arrays with coordinates the robot passes in the format {x, y}
     *
     * @author Kerr
     */
    private ArrayList<int[]> convertRouteToInt(ArrayList<Node> route) {
        ArrayList<int[]> routeToInt = new ArrayList<>();

        // For every step in the route, construct an array in the format {x, y}
        for (Node node : route) {
            routeToInt.add(new int[]{node.getX(), node.getY()});
        }

        // Add null to the end to signify the end of a route
        routeToInt.add(null);

        return routeToInt;
    }

    /**
     * Helper method that converts a route (given as a list of nodes the robot passes) to a list of instructions in the format
     * Left = turn left, Right = turn right, Forward = move forward
     * Turning 180 degrees is defined as turing left twice.
     * @param route a list of nodes the robot passes.
     * @param startOrientationVX X component of the orientation vector of the robot.
     * @param startOrientationVY Y component of the orientation vector of the robot.
     * @return a list of instructions.
     *
     * @author Kerr
     */
    private ArrayList<String> convertRouteToString(ArrayList<Node> route, int startOrientationVX, int startOrientationVY) {

        // Set commands for moving forward, left, right and place
        String forward = "Forward";
        String left = "Left";
        String right = "Right";

        ArrayList<String> routeToString = new ArrayList<>();

        for (int i = 0; i < route.size() - 1; i++) {

            // determine the direction vectors for each step and calculate the cross and dot product
            int[] vectors = calculateVectors(route.get(i), route.get(i + 1), startOrientationVX, startOrientationVY);
            int dotProduct = (vectors[0] * vectors[2] + vectors[1] * vectors[3]);
            int crossProduct = (vectors[0] * vectors[3] - vectors[1] * vectors[2]);

            // If the dot product = 0, the vectors are at 90 degrees. The cross product then determines if the vectors are
            // right handed (cross product = -1) or left handed (cross product = 1). If the dot product equals 1, the vectors
            // are at 0 degrees. Else, the vectors are at 180 degrees.
            if (dotProduct == 0 && crossProduct == -1) {

                routeToString.add(right);
                routeToString.add(forward);

            } else if (dotProduct == 0 && crossProduct == 1) {

                routeToString.add(left);
                routeToString.add(forward);

            } else if (dotProduct == 1) {

                routeToString.add(forward);

            } else {

                routeToString.add(left);
                routeToString.add(left);
                routeToString.add(forward);
            }
        }
        return routeToString;
    }


    /**
     * Calculates a route over the grid given a list of objects and their destinations
     *
     * @param objectList A list of objects with their location and destination
     * @param obstructionList A list of obstructions with their locations
     * @author Kerr
     */
    public boolean calculateRoute(ObservableList<Object> objectList, ObservableList<Obstruction> obstructionList) {
        // Reset previously calculated path and grid
        routeToInt.clear();
        routeToString.clear();
        grid.resetObstructions();

        // Get initial start location and orientation in case finding a path fails
        int initialX = startX;
        int initialY = startY;
        int initialVX = startOrientationVX;
        int initialVY = startOrientationVY;

        ArrayList<int[][]> rearrangementList = new ArrayList<>();
        ArrayList<Node> result;
        int startVX;
        int startVY;

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

        // For each object location-destination pairs determine the route to the object location and then the object destination
        for (int[][] destination : rearrangementList) {

            // Get the start orientation of the robot
            startVX = startOrientationVX;
            startVY = startOrientationVY;

            // Calculate the route to the object location and remove the object from the grid
            result = calculateShortestPathFromSource(destination[0][0], destination[0][1], false);

            // Check if a path has been found, if not, return false
            if (result == null) {
                startX = initialX;
                startY = initialY;
                startOrientationVX = initialVX;
                startOrientationVY = initialVY;
                return false;
            }

            grid.removeObstruction(destination[0][0], destination[0][1]);

            // Convert the calculated route to an list of Integers and to a list of strings and add them to the routeToInt and routeToString
            routeToInt.addAll(convertRouteToInt(result));
            routeToString.addAll(convertRouteToString(result, startVX, startVY));

            // Since the first route is always to pickup an item, add the pickup command to the end the list of commands
            routeToString.add("Pickup");

            result.clear();

            // Get the start orientation of the robot
            startVX = startOrientationVX;
            startVY = startOrientationVY;

            // Calculate the route to the object destination and add the object to the grid
            result = calculateShortestPathFromSource(destination[1][0], destination[1][1], true);

            // Check if a path has been found, if not, return false
            if (result == null) {
                startX = initialX;
                startY = initialY;
                startOrientationVX = initialVX;
                startOrientationVY = initialVY;
                return false;
            }

            grid.addObstruction(destination[1][0], destination[1][1]);

            // Convert the calculated route to an list of Integers and to a list of strings and add them to the routeToInt and routeToString
            routeToInt.addAll(convertRouteToInt(result));
            routeToString.addAll(convertRouteToString(result, startVX, startVY));

            // Since the first route is always to pickup an item, add the pickup command to the end the list of commands
            routeToString.add("Place");

            result.clear();
        }
        return true;
    }

    /**
     * Calculates a route over the grid given a single object destination
     *
     * @param objectList A list of objects with their location and destination
     * @param obstructionList A list of obstructions with their locations
     * @param destinationX the destination X of the single object
     * @param destinationY the destination Y of the single object.
     * @author Kerr
     */
    public boolean calculateRoute(ObservableList<Object> objectList, ObservableList<Obstruction> obstructionList, int destinationX, int destinationY) {
        // Reset previously calculated path and grid
        routeToInt.clear();
        routeToString.clear();
        grid.resetObstructions();

        // Get initial start location and orientation in case finding a path fails
        int initialX = startX;
        int initialY = startY;
        int initialVX = startOrientationVX;
        int initialVY = startOrientationVY;

        ArrayList<Node> result;
        int startVX;
        int startVY;

        // Go through each object defined by the user
        for (Object object : objectList) {

            // Add an obstruction at the location of the object
            grid.addObstruction(object.getLocationX(), object.getLocationY());
        }

        // Go through each obstruction defined by the user
        for (Obstruction obstruction : obstructionList) {

            // Add an obstruction at the location of the obstruction
            grid.addObstruction(obstruction.getLocationX(), obstruction.getLocationY());
        }

        // Determine the route to the object destination

        // Get the start orientation of the robot
        startVX = startOrientationVX;
        startVY = startOrientationVY;

        // Calculate the route to the object location and remove the object from the grid
        result = calculateShortestPathFromSource(destinationX, destinationY, true);

        // Check if a path has been found, if not, return false
        if (result == null) {
            startX = initialX;
            startY = initialY;
            startOrientationVX = initialVX;
            startOrientationVY = initialVY;
            return false;
        }

        // Convert the calculated route to an list of Integers and to a list of strings and add them to the routeToInt and routeToString
        routeToInt.addAll(convertRouteToInt(result));
        routeToString.addAll(convertRouteToString(result, startVX, startVY));

        // Since the route is always to drop off an item, add the pickup command to the end the list of commands
        routeToString.add("Place");
        return true;
    }
}