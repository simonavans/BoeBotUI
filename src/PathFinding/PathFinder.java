package PathFinding;

import java.util.ArrayList;

/**
 * Class that implements a path finding algorithm based on the Dijkstra algorithm inspired by
 * https://www.baeldung.com/java-dijkstra
 */
public class PathFinder {

    private final Graph grid;

    private Node startNode; // the current starting location of the boebot
    private int startOrientationVX; //TODO it is nicer to summarize this in one variable (angle)
    private int startOrientationVY;  //TODO See above

    /**
     * Constructs a pathfinder given a certain graph, start location and start orientation.
     *
     * @param grid              graph which the boebot will follow
     * @param startX            x location of the starting location of the boebot
     * @param startY            y location of the starting location of the boebot
     * @param startOrientationX x-component of the vector facing the same direction as the boebot
     * @param startOrientationY y-component of the vector facing the same direction as the boebot
     * @author Kerr
     */
    PathFinder(Graph grid, int startX, int startY, int startOrientationX, int startOrientationY) {
        this.grid = grid;

        this.startNode = grid.getNode(startX, startY);
        this.startOrientationVX = startOrientationX;
        this.startOrientationVY = startOrientationY;
    }

    /**
     * Calculate the shortest route (the route that takes the leas amount of time to complete) between the boebots
     * current location and its destination given by an x and y coordinate
     *
     * @param destinationX the x coordinate of the destination
     * @param destinationY the y coordinate of the destination
     * @return an ArrayList with Strings which guide the boebot through the shortest route. If no route is found
     * (meaning the destination is unreachable), return null
     * @author Kerr
     */
    ArrayList<String> calculateShortestPathFromSource(int destinationX, int destinationY) {
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
                    Integer edgeWeight = calculateAngle( calculateEdgeAngle(currentNode, adjacentNode)) / 90 * 20 + 10; //TODO fix 'random' numbers
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }

            settledNodes.add(currentNode);

            // If the currentNode is the destination node, the shortest route (should) be found and this route can be returned
            if (currentNode.equals(destinationNode)) {

                destinationNode.getShortestPath().add(destinationNode);
                ArrayList<String> route = this.convertRouteToString(destinationNode.getShortestPath());

                // Update the location and orientation of the boebot to its final orientation of its destination
                Node previousNode = destinationNode.getShortestPath().get(destinationNode.getShortestPath().size() - 2);
                startOrientationVY = currentNode.getY() - previousNode.getY();
                startOrientationVX = currentNode.getX() - previousNode.getX();
                startNode = destinationNode;

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
     * @param edgeWeigh      the edge weight of the newly found connection
     * @param sourceNode     the node from which the newly found connection is found
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




    //TODO The following methods are still in their beta phase

    /**
     * Helper method that calculates the angle of the path between two given nodes, based on the orientation of the
     * boebot.
     *
     * @param currentNode  the current location of the boebot
     * @param destinationNode the location of an node adjacent to the current node
     * @return the angle of the path between the two given nodes
     * @author Kerr
     */
    private int[] calculateEdgeAngle(Node currentNode, Node destinationNode) {

        // Define the node from which the boebot came, together with its orientation vector x and y component
        Node previousNode;
        int previousNodeVX;
        int previousNodeVY;

        // Define the orientation vector x and y of the boebot if it moved to the adjacent node
        int destinationNodeNodeVX = destinationNode.getX() - currentNode.getX();
        int destinationNodeVY = destinationNode.getY() - currentNode.getY(); //TODO this was previously wrong, now correct

        // if the boebot is not in its starting position, define the orientation of the boebot by its previous location
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


    private int calculateAngle(int[] vectors) {
        int dotProduct = (vectors[0] * vectors[2] + vectors[1] * vectors[3]);
        return (int) (Math.acos(dotProduct) * 180 / Math.PI);
    }

    private String calculateDirection(int[] vectors) {
        int dotProduct = (vectors[0] * vectors[2] + vectors[1] * vectors[3]);
        if (dotProduct == 0) {
            if (vectors[0] == 0 && vectors[1] == 1) {
                if (vectors[2] == -1) {return "Turn left90";}
                else {return "Turn right90";}
            } else if (vectors[0] == 0 && vectors[1] == -1) {
                if (vectors[2] == -1) {return "Turn right180";}
                else {return "Turn left180";}
            } else if (vectors[1] == 0 && vectors[0] == 1) {
                if (vectors[3] == 1) {return "Turn left0";}
                else {return "Turn right0";}
            } else {
                if (vectors[3] == -1) {return "Turn left270";}
                else {return "Turn right270";}
            }
        } else if (dotProduct == 1) {
            return "Go straight";
        } else  {
            return "Turn around";
        }
    }

    public ArrayList<String> convertRouteToString(ArrayList<Node> route) {
        ArrayList<String> routeToString = new ArrayList<>();

        for (int i = 0; i < route.size() - 1; i++) {

            int[] vectors = calculateEdgeAngle(route.get(i), route.get(i + 1));
            String step = calculateDirection(vectors);
            routeToString.add(step);

            if (step.contains("Turn")) {
                routeToString.add("Go straight");
            }
        }
        return routeToString;

    }

}
