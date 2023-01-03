package BackEnd.PathFinding;

import FrontEnd.MainView;

/**
 * Superclass that defines a graph: a collection of connected nodes. A graph consists of an ArrayList of nodes. Each
 * node should have an unique x and y value pair.
 *
 */
public class Grid extends Graph {

    private int width;
    private int height;
    private MainView callback;

    /**
     * Construct a grid shaped Graph given a width and height. all nodes are connected to their neighboring nodes
     * (minimum of 2, maximum of 4) with angles of 180 or 90 degrees. The starting coordinate is (0,0) and the final
     * coordinate is (width - 1 , height - 1).
     * @param callback class to which the method should callback
     *
     * @author Kerr
     */
    public Grid(MainView callback) {
        super();
        this.callback = callback;
        updateGrid();
    }

    /**
     * Method that updates the grid to the current size
     *
     * @author Kerr
     */
    public void updateGrid() {
        this.width = callback.getSettingsView().gridWidth;
        this.height = callback.getSettingsView().gridHeight;

        super.getGraphNodes().clear();
        generateGrid();
        generateAdjacencyList();
    }

    /**
     * Helper method that creates a total of width * height nodes in a grid shape.
     *
     * @author Kerr
     */
    private void generateGrid() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                super.addNode(x, y);
            }
        }
    }

    /**
     * Helper method that assigns each node in the grid 2-4 adjacent nodes. Nodes in the corners have exactly 2 neighbors,
     * Nodes on the edge have exactly 3 neighbors and all other nodes have exactly 4 neighbors.
     *
     * @author Kerr
     */
    private void generateAdjacencyList() {
        for (Node node : super.getGraphNodes().values()) {

            // Node is not on left edge
            if (node.getX() != 0) {
                node.addAdjacentNode(super.getNode(node.getX() - 1, node.getY()));
            }
            // Node is not on right edge
            if (node.getX() != (width - 1)) {
                node.addAdjacentNode(super.getNode(node.getX() + 1, node.getY()));
            }
            // Node is not on bottom edge
            if (node.getY() != 0) {
                node.addAdjacentNode(super.getNode(node.getX(), node.getY() - 1));
            }
            // node is not on bottom edge
            if (node.getY() != (height - 1)) {
                node.addAdjacentNode(super.getNode(node.getX(), node.getY() + 1));
            }
        }
    }
}
