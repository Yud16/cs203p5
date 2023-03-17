import processing.core.PImage;

import java.util.List;

public interface Moving extends Entity, Animated, ActiEntities{
    default void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Functions.createActivityAction(this, world, imageStore), getActionPeriod());
        scheduler.scheduleEvent(this, Functions.createAnimationAction(this, 0), getAnimationPeriod());
    }
    void nextImage();
    double getActionPeriod();
    Point nextPosition(WorldModel world, Point destPos);
    double getAnimationPeriod();
}
