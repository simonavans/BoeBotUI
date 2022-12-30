package PathFinding;

/**
 * Superclass that defines a graph: a collection of connected nodes. A graph consists of an ArrayList of nodes. Each
 * node should have an unique x and y value pair.
 *
 */
public class Grid extends Graph {

    private final int width;
    private final int height;

    /**
     * Construct a grid shaped Graph given a width and height. all nodes are connected to their neighboring nodes
     * (minimum of 2, maximum of 4) with angles of 180 or 90 degrees. The starting coordinate is (0,0) and the final
     * coordinate is (width - 1 , height - 1).
     * @param width the number of nodes in the x direction
     * @param height the number of nodes in the y direction
     *
     * @author Kerr
     */
    public Grid(int width, int height) {
        super();
        this.width = width;
        this.height = height;

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
        for (Node node : nodes.values()) {

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


    // Getters

    /**
     * Get the width of the grid (the number of nodes in the x direction).
     * @return the width of the grid
     *
     * @author Kerr
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the height of the grid (the number of nodes in the y direction).
     * @return the height of the grid
     *
     * @author Kerr
     */
    public int getHeight() {
        return height;
    }

}
