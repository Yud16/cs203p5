import processing.core.PImage;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public abstract class Dude implements Entity, Animated, ActiEntities, Moving{
    private final String id;
    private Point position;
    private final List<PImage> images;

    private int imageIndex;
    private final int resourceLimit;
    private int resourceCount;
    private final double actionPeriod;
    private final double animationPeriod;

    public Dude(String id, Point position, List<PImage> images, int resourceLimit, double actionPeriod, double animationPeriod) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }
    public int getImageIndex() {
        return imageIndex;
    }
    public void nextImage() {
        imageIndex = imageIndex + 1;
    }

    public int getResourceCount() {
        return resourceCount;
    }
    public void setResourceCount(int resourceCount) {
        this.resourceCount = resourceCount;
    }
    public String getId() {
        return id;
    }
    public Point getPosition() {
        return position;
    }
    public int getResourceLimit() {
        return resourceLimit;
    }
    public double getActionPeriod() {
        return actionPeriod;
    }
    public List<PImage> getImages() {
        return images;
    }
    public void setPosition(Point position) {
        this.position = position;
    }
    public double getAnimationPeriod() {
        return animationPeriod;
    }
    public Point nextPosition(WorldModel world, Point destPos) {
        PathingStrategy strat = new AStarPathingStrartegy();
        Predicate<Point> cpt = p -> world.withinBounds(p) && !(world.isOccupied(p) && world.getOccupancyCell(p).getClass() != Stump.class);
        BiPredicate<Point, Point> wir = (p1, p2) -> p1.adjacent(p2);
        List<Point> path = strat.computePath(getPosition(), destPos, cpt, wir, PathingStrategy.CARDINAL_NEIGHBORS);
        if (path != null && path.size() > 0) {
            return path.get(0);
        }
        return getPosition();
    }
}
