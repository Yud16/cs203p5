import java.util.Objects;

public class Node {
    private double g;

    private double h;

    private double f;
    private Node prior;
    private Point loc;
    public Node(double g, double h, double f, Node prev, Point loc) {
        this.g = g;
        this.h = h;
        this.f = f;
        this.prior = prev;
        this.loc = loc;
    }
    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }
    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }
    public double getF() {
        return f;
    }
    public void setF(double f) {
        this.f = f;
    }
    public Node getPrior() {
        return prior;
    }
    public Point getLoc() {
        return loc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(loc, node.loc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loc);
    }
}
