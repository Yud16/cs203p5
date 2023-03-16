import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPathingStrartegy implements PathingStrategy {
    private Node current;
    private PriorityQueue<Node> openList;
    private HashSet<Node> closedList;

    public AStarPathingStrartegy() {
        Comparator<Node> comp = Comparator.comparing(Node::getF);
        openList = new PriorityQueue<Node>(comp);
        closedList = new HashSet<Node>();
    }

    @Override
    public List<Point> computePath(Point start, Point end, Predicate<Point> canPassThrough, BiPredicate<Point, Point> withinReach, Function<Point, Stream<Point>> potentialNeighbors) {
        Comparator<Node> comp = Comparator.comparing(Node::getF);
        openList = new PriorityQueue<Node>(comp);
        closedList = new HashSet<Node>();
        Node startN = new Node(0.0, 0.0, 0.0, null, start);
        openList.add(startN);
        current = startN;
        while (openList.size() > 0 && !withinReach.test(current.getLoc(), end)) {
            List<Point> neigh = potentialNeighbors.apply(current.getLoc()).collect(Collectors.toList());
            for (Point p : neigh) {
                if (!closedList.contains(p) && canPassThrough.test(p)) {
                    double g = current.getG() + 1;
                    double h = p.distanceSquared(end);
                    double f = g + h;
                    Node n = new Node(g, h, f, current, p);
                    if (openList.contains(n)) {
                        openList.remove(n);
                    }
                    openList.add(n);
                }
            }
            closedList.add(current);
            while (closedList.contains(openList.peek())) {
                openList.remove();
            }
            current = openList.peek();
        }
        List<Point> path = new ArrayList<Point>();
        if (current != null && withinReach.test(current.getLoc(), end)) {
            path.add(current.getLoc());
            while (current.getPrior() != null && current.getPrior() != startN) {
                path.add(0,current.getPrior().getLoc());
                current = current.getPrior();
            }
        }
        return path;
    }
}