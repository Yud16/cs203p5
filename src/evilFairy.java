import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class evilFairy implements Entity, Animated, ActiEntities{
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;
    private final double actionPeriod;
    private final double animationPeriod;
    public evilFairy(String id, Point position, List<PImage> images,double actionPeriod, double animationPeriod) {
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

    private boolean moveToFairy(WorldModel world, Entity target, EventScheduler scheduler) {
        if (position.adjacent(target.getPosition())) {
            world.removeEntity(scheduler, target);
            return true;
        } else {
            Point nextPos = nextPositionFairy(world, target.getPosition());

            if (!position.equals(nextPos)) {
                world.moveEntity(scheduler, nextPos, this);
            }
            return false;
        }
    }

    private Point nextPositionFairy(WorldModel world, Point destPos) {
        PathingStrategy strat = new AStarPathingStrartegy();
        Predicate<Point> cpt = p -> world.withinBounds(p) && !(world.isOccupied(p));
        BiPredicate< Point, Point> wir = (p1, p2) -> p1.adjacent(p2);
        List<Point> path = strat.computePath(getPosition(),destPos, cpt, wir, PathingStrategy.CARDINAL_NEIGHBORS);
        if (path != null && path.size() > 0 && path.get(0).getClass() == Point.class) {
            return path.get(0);
        }
        return getPosition();
    }
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Functions.createActivityAction(this, world, imageStore), actionPeriod);
        scheduler.scheduleEvent(this, Functions.createAnimationAction(this, 0), getAnimationPeriod());
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fairyTarget = world.findNearest(position, new ArrayList<>(List.of(House.class)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (this.moveToFairy(world, fairyTarget.get(), scheduler)) {

                Sapling sapling = EntityCreator.createSapling(EntityCreator.SAPLING_KEY + "_" + fairyTarget.get().getId(), tgtPos, imageStore.getImageList(EntityCreator.SAPLING_KEY), 0);

                world.addEntity(sapling);
                sapling.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this, Functions.createActivityAction(this, world, imageStore), actionPeriod);
    }
    public void nextImage() {
        imageIndex = imageIndex + 1;
    }

    public double getAnimationPeriod() {return this.animationPeriod;}

    public PImage getCurrentImage() {
        return this.images.get(this.imageIndex % this.images.size());
    }

    /**
     * Helper method for testing. Preserve this functionality while refactoring.
     */
    public String log(){
        return this.id.isEmpty() ? null :
                String.format("%s %d %d %d", this.id, this.position.getX(), this.position.getY(), this.imageIndex);
    }

}
