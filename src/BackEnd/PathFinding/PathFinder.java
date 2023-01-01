package BackEnd.PathFinding;

import FrontEnd.MainView;

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
    private Node startNode;

    private int startOrientationVX;
    private int startOrientationVY;

    private int turnWeight;
    private int forwardWeight;

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

    public void updateGrid() {
        this.grid = callback.getGrid();
    }

    public void updateStartLocation() {
        this.startX = callback.getSettingsView().boebotX;
        this.startY = callback.getSettingsView().boebotY;
        this.startNode = grid.getNode(startX, startY);
    }

    public void updateStartOrientation() {
        this.startOrientationVX = callback.getSettingsView().boebotVX;
        this.startOrientationVY = callback.getSettingsView().boebotVY;
    }

    public void updateWeights() {
        this.turnWeight = callback.getSettingsView().turnWeight;
        this.forwardWeight = callback.getSettingsView().forwardWeight;
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
     */
    public int getTurnWeight() {return turnWeight;}

    /**
     * Getter method that returns the weight of moving forward.
     * @return the weight of making moving forward.
     */
    public int getForwardWeight() { return forwardWeight;}

    /**
     * Calculate the shortest route (the route that takes the leas amount of time to complete) between the robots
     * current location and its destination given by an x and y coordinate
     *
     * @param destinationX the x coordinate of the destination
     * @param destinationY the y coordinate of the destination
     * @return an ArrayList with Strings which guide the robot through the shortest route. If no route is found
     * (meaning the destination is unreachable), return null
     *
     * @author Kerr
     */
    public ArrayList<Node> calculateShortestPathFromSource(int destinationX, int destinationY, boolean dropOff) {
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
                    startNode = previousNode;
                } else {
                    startNode = destinationNode;
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
     * Helper method that calculates the angle of the path between two given nodes, based on the orientation of the
     * robot.
     *
     * @param currentNode the current location of the robot
     * @param destinationNode the location of an node adjacent to the current node
     * @return the angle of the path between the two given nodes
     *
     * @author Kerr
     */
    private int[] calculateVectors(Node currentNode, Node destinationNode) {
        return calculateVectors(currentNode, destinationNode, startOrientationVX, startOrientationVY);
    }

    /**
     * Helper method that calculates the angle of the path between two given nodes, based on the orientation of the
     * robot.
     *
     * @param currentNode the current location of the robot
     * @param destinationNode the location of an node adjacent to the current node
     * @param startOrientationVX X component of the start orientation of the robot
     * @param startOrientationVY Y component of the start orientation of the robot
     * @return the angle of the path between the two given nodes
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
     * Helper method that determines based on two vectors if the robot moves left, right, backwards or forward
     * @param vectors list of two vectors in the format {x1, y1, x2, y2}.
     * @return Direction the robot moves in (L = left, R = right, F = forward, T = backward).
     *
     * @author Kerr
     */
    private String calculateDirection(int[] vectors) {
        int dotProduct = (vectors[0] * vectors[2] + vectors[1] * vectors[3]);
        int crossProduct = (vectors[0] * vectors[3] - vectors[1] * vectors[2]);

        // If the dot product = 0, the vectors are at 90 degrees. The cross product then determines if the vectors are
        // right handed (cross product = -1) or left handed (cross product = 1). If the dot product equals 1, the vectors
        // are at 0 degrees. Else, the vectors are at 180 degrees.
        if (dotProduct == 0 && crossProduct == -1 ) {return "R";}
        if (dotProduct == 0 && crossProduct == 1) {return "L";}
        if (dotProduct == 1) {return "F";}
        return "T";
    }

    /**
     * convert a route (given as a list of nodes the robot passes) to a list of coordinates in the format {x, y}.
     * @param route a list of nodes the robot passes.
     * @return an ArrayList of arrays with coordinates the robot passes in the format {x, y}
     *
     * @author Kerr
     */
    public ArrayList<int[]> convertRouteToInt(ArrayList<Node> route) {
        ArrayList<int[]> routeToInt = new ArrayList<>();

        for (Node node : route) {
            routeToInt.add(new int[]{node.getX(), node.getY()});
        }
        return routeToInt;
    }

    //TODO this method could be cleaned up a bit further (optional)

    /**
     * Convert a route (given as a list of nodes the robot passes) to a list of instructions in the format L = turn left,
     * R = turn right, F = move forward, P = place object here. Turning 180 degrees is defined as turing left twice.
     * @param route a list of nodes the robot passes.
     * @param startOrientationVX X component of the orientation vector of the robot.
     * @param startOrientationVY Y component of the orientation vector of the robot.
     * @param dropOff true = route is to drop an object off, false = route is to pick an object up.
     * @return a list of instructions.
     *
     * @author Kerr
     */
    public ArrayList<String> convertRouteToString(ArrayList<Node> route, int startOrientationVX, int startOrientationVY, boolean dropOff) {
        ArrayList<String> routeToString = new ArrayList<>();

        for (int i = 0; i < route.size() - 1; i++) {
            String step = calculateDirection(calculateVectors(route.get(i), route.get(i + 1), startOrientationVX, startOrientationVY));
            routeToString.add(step);

            if (step.equals("L") || step.equals("R")) {
                routeToString.add("F");
            }
            if (step.equals("T")) {
                routeToString.add("L");
                routeToString.add("L");
            }
        }

        if (dropOff) {
            routeToString.remove(routeToString.size() - 1);
            routeToString.add("P");
        }
        return routeToString;
    }
}
