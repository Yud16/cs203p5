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
public final class Wizard implements Entity, Animated, ActiEntities, Moving {
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;
    private final double actionPeriod;
    private final double animationPeriod;

    public Wizard(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
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

    // But we are transforming a different Entity, so are we even allowed to do that?

    // THIS MIGHT NOT WORK, BECAUSE TRANSFORM SHOULD BE CALLED
    private boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (position.adjacent(target.getPosition())) {
            return true;
        } else {
            Point nextPos = nextPosition(world, target.getPosition());
            if (!position.equals(nextPos)) {
                world.moveEntity(scheduler, nextPos, this);
            }
            return false;
        }
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Functions.createActivityAction(this, world, imageStore), actionPeriod);
        scheduler.scheduleEvent(this, Functions.createAnimationAction(this, 0), getAnimationPeriod());
    }

    // TODO: make sure that this implementation is OK!
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> WizardTarget = world.findNearest(position, new ArrayList<>(List.of(Fairy.class)));
        world.makeWizardTrail(imageStore, getPosition());
        if (WizardTarget.isPresent()) {
            Point tgtPos = WizardTarget.get().getPosition();

            if (this.moveTo(world, WizardTarget.get(), scheduler)) {

                EvilFairy ef = EntityCreator.createEvilFairy(EntityCreator.EVIL_FAIRY_KEY + "_" +
                        WizardTarget.get().getId(), tgtPos, imageStore.getImageList(EntityCreator.EVIL_FAIRY_KEY),
                        0.5, 0.5);
                // NOTE - actionPeriod and animationPeriod can't be 0

                world.removeEntity(scheduler, WizardTarget.get());
                scheduler.unscheduleAllEvents(WizardTarget.get());

                world.addEntity(ef);
                ef.scheduleActions(scheduler, world, imageStore);
            }
        }
        scheduler.scheduleEvent(this, Functions.createActivityAction(this, world, imageStore), actionPeriod);
    }

    public void nextImage() {
        imageIndex = imageIndex + 1;
    }

    @Override
    public double getActionPeriod() {
        return actionPeriod;
    }

    @Override
    public Point nextPosition(WorldModel world, Point destPos) {
        PathingStrategy strat = new AStarPathingStrartegy();
        Predicate<Point> cpt = p -> world.withinBounds(p) && !(world.isOccupied(p));
        BiPredicate<Point, Point> wir = Point::adjacent;
        List<Point> path = strat.computePath(getPosition(), destPos, cpt, wir, PathingStrategy.CARDINAL_NEIGHBORS);
        if (path != null && path.size() > 0) {
            return path.get(0);
        }
        return getPosition();
    }

    public double getAnimationPeriod() {
        return this.animationPeriod;
    }
    @Override
    public int getImageIndex() {
        return imageIndex;
    }
}
