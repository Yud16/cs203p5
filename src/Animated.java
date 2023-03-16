public interface Animated extends Entity {
    double getAnimationPeriod();
    void nextImage();
    void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
}

