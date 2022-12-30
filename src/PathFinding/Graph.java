package PathFinding;

import java.util.HashMap;

public abstract class Graph {

    HashMap<String, Node> nodes = new HashMap<>(); // list of nodes in this graph

    // Getters

    /**
     * Get the list of nodes in this graph.
     * @return the list of nodes in this graph.
     *
     * @author Kerr
     */
    HashMap<String, Node> getGraphNodes() {return nodes;}

    /**
     * Get a node in this graph based on its x and y value.
     * @param x the x value of the wanted node.
     * @param y the y value of the wanted node.
     * @return the node with x-coordinate x and y-coordinate y
     *
     * @author Kerr
     */
    Node getNode (int x, int y) {return nodes.get(x + "-" + y);}


    // Setters

    /**
     * Add a node to this graph based on its x and y value.
     * @param x the x value of the wanted node.
     * @param y the y value of the wanted node.
     *
     * @author Kerr
     */
    void addNode(int x, int y) {nodes.put(x + "-" + y, new Node(x,y));}

    /**
     * Add an obstruction on a node given its x and y value.
     * @param x the x value of the wanted node
     * @param y the y value of the wanted node
     *
     * @author Kerr
     */
    public void addObstruction(int x, int y) {this.getNode(x, y).addObstruction();}

    /**
     * Remove an obstruction on a node given its x and y value.
     * @param x the x value of the wanted node
     * @param y the y value of the wanted node
     *
     * @author Kerr
     */
    public void removeObstruction(int x, int y) {this.getNode(x, y).removeObstruction();}

    /**
     * Reset the weight from all nodes to the start point in the graph.
     *
     * @author Kerr
     */
    void resetNodeDistance() {
        for (Node node : nodes.values()) {
            node.setDistance(Integer.MAX_VALUE);
        }
    }

    /**
     * Reset the shortest path from all nodes to the start point in the graph.
     *
     * @author Kerr
     */
    void resetNodeShortestPath() {
        for (Node node : nodes.values()) {
            node.resetNodeShortestPath();
        }
    }

}
