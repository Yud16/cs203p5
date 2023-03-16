import processing.core.PImage;

import java.util.List;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class Sapling implements Entity, Animated, ActiEntities, Plant{
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;
    private int health;
    private final double actionPeriod;
    private final double animationPeriod;
    private final int healthLimit;
    public Sapling(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod, int health, int healthLimit) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.health = health;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.healthLimit = healthLimit;
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

    public void setHealth(int i) {health = i;}

    public int getHealth() {
        return health;
    }
    public boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (health <= 0) {
            Stump stump = EntityCreator.createStump(EntityCreator.STUMP_KEY + "_" + id, position, imageStore.getImageList(EntityCreator.STUMP_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(stump);

            return true;
        } else if (health >= healthLimit) {
            Tree tree = EntityCreator.createTree(EntityCreator.TREE_KEY + "_" + id, position, Functions.getNumFromRange(EntityCreator.TREE_ACTION_MAX, EntityCreator.TREE_ACTION_MIN), Functions.getNumFromRange(EntityCreator.TREE_ANIMATION_MAX, EntityCreator.TREE_ANIMATION_MIN), Functions.getIntFromRange(EntityCreator.TREE_HEALTH_MAX, EntityCreator.TREE_HEALTH_MIN), imageStore.getImageList(EntityCreator.TREE_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(tree);
            tree.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Functions.createActivityAction(this, world, imageStore), actionPeriod);
        scheduler.scheduleEvent(this, Functions.createAnimationAction(this, 0), getAnimationPeriod());
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        this.health++;
        if (!this.transformPlant(world, scheduler, imageStore)) {

            scheduler.scheduleEvent(this, Functions.createActivityAction(this, world, imageStore), actionPeriod);
        }
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
