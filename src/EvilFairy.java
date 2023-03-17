import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class EvilFairy extends FairyOG implements Entity, Animated, ActiEntities, Moving{
    public EvilFairy(String id, Point position, List<PImage> images,double actionPeriod, double animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fairyTarget = world.findNearest(getPosition(), new ArrayList<>(List.of(House.class)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (this.moveTo(world, fairyTarget.get(), scheduler)) {

                Sapling sapling = EntityCreator.createSapling(EntityCreator.SAPLING_KEY + "_" + fairyTarget.get().getId(), tgtPos, imageStore.getImageList(EntityCreator.SAPLING_KEY), 0);

                world.addEntity(sapling);
                sapling.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this, Functions.createActivityAction(this, world, imageStore), getActionPeriod());
    }
}
