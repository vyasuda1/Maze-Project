package DimitrovYasuda.cs146.project3;

/**
 * Represents a cell in a maze.
 *
 * @author Viola Yasuda and Dimitar Dimitrov
 * @version 1.0 11/7/2021
 */
public class Cell {
    private final boolean[] hasNeighborConnections; //0 = north, 1 = east, 2 = south, 3 = west
    private final int[] coordinates; //element 0 = row, element 1 = column
    private Cell parent; //the previous cell to be visited
    private String color; //white = undiscovered, grey = discovered & not fully explored, black = discovered & explored
    private int orderDiscovered; //order in which cell is discovered
    private boolean onPath; //true if cell is on solution path, false otherwise

    /**
     * Constructs a Cell.
     * @param row the row in the maze the cell is located
     * @param col the column in the maze the cell is located
     */
    public Cell(int row, int col) {
        hasNeighborConnections = new boolean[]{false, false, false, false};
        coordinates = new int[]{row, col};
        parent = null;
        color = "white";
        orderDiscovered = -1;
        onPath = false;
    }

    /**
     * Gets the states of the neighbor connections of the cell.
     * @return hasNeighborConnections
     */
    public boolean[] getHasNeighborConnections() {
        return hasNeighborConnections;
    }

    /**
     * Sets the state of a neighbor connection of the cell.
     * @param index the index of the neighbor cell connection (north, east, south, or west)
     * @param hasConnection the status of the specified neighbor connection
     */
    public void setHasNeighborConnection(int index, boolean hasConnection) {
        hasNeighborConnections[index] = hasConnection;
    }

    /**
     * Gets the coordinates of the cell
     * @return an int array with the row and column of the cell in the maze
     */
    public int[] getCoordinates() {
        return coordinates;
    }

    /**
     * Gets the cell that was traveled from in order to get to this cell.
     * @return the parent of this cell
     */
    public Cell getParent() {
        return parent;
    }

    /**
     * Sets the parent of this cell.
     * @param parent the cell that was traveled from in order to get to this cell
     */
    public void setParent(Cell parent) {
        this.parent = parent;
    }

    /**
     * Gets the color of the cell (black, grey, or white).
     * @return the cell's color
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the color of the cell (black, grey, or white).
     * @param color the color to set the cell to
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Gets the order number that this cell was discovered.
     * @return the order-discovered number
     */
    public int getOrderDiscovered() {
        return orderDiscovered;
    }

    /**
     * Sets the order number that this cell was discovered.
     * @param orderDiscovered the order-discovered number
     */
    public void setOrderDiscovered(int orderDiscovered) {
        this.orderDiscovered = orderDiscovered;
    }

    /**
     * Returns whether this cell is on the maze's solution path.
     * @return true if it is on the path, false otherwise
     */
    public boolean isOnPath() {
        return onPath;
    }

    /**
     * Sets the status of whether the cell is on the maze's solution path.
     * @param onPath the state of whether the cell is on the path
     */
    public void setOnPath(boolean onPath) {
        this.onPath = onPath;
    }

    /**
     * Returns the string version of the cell.
     * @return the coordinates of the cell contained in parentheses.
     */
    @Override
    public String toString() {
        return "(" + coordinates[0] + "," + coordinates[1] + ")";
    }

    /**
     * Checks if two cells are equal.
     * @param o the other cell
     * @return true if they are at the same coordinates, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        Cell other = (Cell) o;
        return coordinates[0] == other.getCoordinates()[0] && coordinates[1] == other.getCoordinates()[1];
    }
}
