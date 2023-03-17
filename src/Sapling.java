import processing.core.PImage;

import java.util.List;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class Sapling extends Plant implements Entity, Animated, ActiEntities{
    private final int healthLimit;
    public Sapling(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod, int health, int healthLimit) {
        super(id, position,images,actionPeriod,animationPeriod,health);
        this.healthLimit = healthLimit;
    }
    public boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (getHealth() <= 0) {
            Stump stump = EntityCreator.createStump(EntityCreator.STUMP_KEY + "_" + getId(), getPosition(), imageStore.getImageList(EntityCreator.STUMP_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(stump);

            return true;
        } else if (getHealth() >= healthLimit) {
            Tree tree = EntityCreator.createTree(EntityCreator.TREE_KEY + "_" + getId(), getPosition(), Functions.getNumFromRange(EntityCreator.TREE_ACTION_MAX, EntityCreator.TREE_ACTION_MIN), Functions.getNumFromRange(EntityCreator.TREE_ANIMATION_MAX, EntityCreator.TREE_ANIMATION_MIN), Functions.getIntFromRange(EntityCreator.TREE_HEALTH_MAX, EntityCreator.TREE_HEALTH_MIN), imageStore.getImageList(EntityCreator.TREE_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(tree);
            tree.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        this.setHealth(getHealth()+1);
        if (!this.transformPlant(world, scheduler, imageStore)) {

            scheduler.scheduleEvent(this, Functions.createActivityAction(this, world, imageStore), getActionPeriod());
        }
    }
}
