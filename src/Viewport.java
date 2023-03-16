public final class Viewport {
    private int row;
    private int col;
    private final int numRows;

    private final int numCols;

    public Viewport(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
    }
    public int getRow() {
        return row;
    }
    public int getNumCols() {
        return numCols;
    }
    public int getNumRows() {
        return numRows;
    }
    public int getCol() {
        return col;
    }
    public boolean contains(Point p) {
        return p.getY() >= row && p.getY() < row + numRows && p.getX() >= col && p.getX() < col + numCols;
    }

    public Point worldToViewport(int col, int row) {
        return new Point(col - this.col, row - this.row);
    }

    public Point viewportToWorld(int col, int row) {
        return new Point(col + this.col, row + this.row);
    }

    public void shift(int col, int row) {
        this.col = col;
        this.row = row;
    }

}
