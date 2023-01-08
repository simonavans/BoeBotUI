package BackEnd.PathFinding;

import java.util.ArrayList;

/**
 * Class that defines a node: a coordinate on a graph. Each node can be uniquely defined by its x and y value.
 * A node can be obstructed (= an object is placed on the node) or not, has at least one node connected to it, has a
 * shortest path from it to the 'starting node' of the graph, with a given minimum weight.
 */
public class Node {

    private int x;
    private int y;

    private boolean isObstructed = false; // false = no object on node, true = object on node.
    private ArrayList<Node> adjacentNodes = new ArrayList<>(); // List of nodes directly connected to this node.

    private ArrayList<Node> shortestPath = new ArrayList<>(); // List of nodes on the shortest path
    private Integer distance = Integer.MAX_VALUE; // Weight of the shortest path ( = ' travel time' between this node and the start node).

    /**
     * Construct a new node with a given x and y location.
     *
     * @param x value of the x-coordinate of the node.
     * @param y value of the y-coordinate of the node.
     * @author Kerr
     */
    Node(int x, int y) {
        this.x = x;
        this.y = y;
    }


    // Getters

    /**
     * Get the x value of the node location.
     *
     * @return x value of the node location.
     * @author Kerr
     */
    int getX() {return x;}

    /**
     * Get the y value of the node location.
     *
     * @return y value of the node location.
     * @author Kerr
     */
    int getY() {return y;}

    /**
     * return if the node is obstructed (has an object on it) or not
     *
     * @return true = there is an object on the node, false = there is no object on the node
     * @author Kerr
     */
    boolean isObstructed() {return isObstructed;}

    /**
     * Get an ArrayList with the nodes directly connected to this node.
     *
     * @return an ArrayList with the nodes directly connected to this node.
     * @author Kerr
     */
    ArrayList<Node> getAdjacentNodes() {return adjacentNodes;}

    /**
     * Get an ArrayList of nodes which are passed while following the shortest path from a start node to this node.
     *
     * @return an ArrayList of nodes which are passed while following the shortest path from a start node to this node.
     * @author Kerr
     */
    ArrayList<Node> getShortestPath() {return shortestPath;}

    /**
     * Get the minimum weight of the shortest path. This is roughly proportional to the travel time between this node
     * and the starting node.
     *
     * @return the minimum weight of the shortest path.
     * @author Kerr
     */
    Integer getDistance() {return distance;}


    // Setters

    /**
     * Add an obstruction on this node (change isObstructed to false).
     *
     * @author Kerr
     */
    void addObstruction() {this.isObstructed = true;}

    /**
     * Remove an obstruction on this node (change isObstructed to true).
     *
     * @author Kerr
     */
    void removeObstruction() {this.isObstructed = false;}

    /**
     * Add a node which is directly adjacent to this node.
     *
     * @param adjacentNode a node which is directly adjacent to this node.
     * @author Kerr
     */
    void addAdjacentNode(Node adjacentNode) {adjacentNodes.add(adjacentNode);}


    /**
     * Set the weight of the shortest path  This is roughly proportional to the travel time between this node
     * and the starting node.
     *
     * @param shortestPath the weight of the shortest path.
     * @author Kerr
     */
    void setShortestPath(ArrayList<Node> shortestPath) { this.shortestPath = shortestPath;}

    /**
     * Set the list of nodes which are passed while following the shortest path from a start node to this node. The
     * previous list is overwritten.
     *
     * @param distance an ArrayList of nodes which are passed while following the shortest path from a start node to this node.
     * @author Kerr
     */
    void setDistance(Integer distance) {this.distance = distance;}

    /**
     * Clear this ArrayList of nodes which are passed while following the shortest path from a start node to this node.
     *
     * @author Kerr
     */
    void resetNodeShortestPath() {
        this.getShortestPath().clear();
    }

    /**
     * return the string format of a node, in the format of '(x-value, y-value)'
     *
     * @return a string in the format (x-value, y-value)
     */
    public String toString() {return "(" + x + ", " + y + ")";}
}