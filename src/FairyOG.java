import processing.core.PImage;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public abstract class FairyOG implements Entity, Animated, ActiEntities, Moving{
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;
    private final double actionPeriod;
    private final double animationPeriod;
    public FairyOG(String id, Point position, List<PImage> images,double actionPeriod, double animationPeriod) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }
    public String getId() {
        return id;
    }
    public Point getPosition() {
        return position;
    }
    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public List<PImage> getImages() {
        return images;
    }

    @Override
    public int getImageIndex() {
        return imageIndex;
    }

    public Point nextPosition(WorldModel world, Point destPos) {
        PathingStrategy strat = new AStarPathingStrartegy();
        Predicate<Point> cpt = p -> world.withinBounds(p) && !(world.isOccupied(p)) && world.getOccupancyCell(p).getClass() != Stump.class;
        BiPredicate< Point, Point> wir = Point::adjacent;
        List<Point> path = strat.computePath(getPosition(),destPos, cpt, wir, PathingStrategy.CARDINAL_NEIGHBORS);
        if (path != null && path.size() > 0) {
            return path.get(0);
        }
        return getPosition();
    }
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (position.adjacent(target.getPosition())) {
            world.removeEntity(scheduler, target);
            return true;
        } else {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!position.equals(nextPos)) {
                world.moveEntity(scheduler, nextPos, this);
            }
            return false;
        }
    }
    public void nextImage() {
        imageIndex = imageIndex + 1;
    }

    @Override
    public double getActionPeriod() {
        return actionPeriod;
    }

    public double getAnimationPeriod() {return this.animationPeriod;}
}
