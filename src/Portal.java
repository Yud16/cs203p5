import processing.core.PImage;

import java.util.List;

public class Portal implements Entity, Animated {
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;
    private final double animationPeriod;

    public Portal(String id, Point position, List<PImage> images, double animationPeriod) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
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

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Functions.createAnimationAction(this, 0), getAnimationPeriod());
    }

    public void nextImage() {
        imageIndex = imageIndex + 1;
    }

    public double getAnimationPeriod() {
        return this.animationPeriod;
    }

    public PImage getCurrentImage() {
        return this.images.get(this.imageIndex % this.images.size());
    }
//    portal images/portal2.png 0 0 0 0
//    portal images/portal3.png 0 0 0 0
//    portal images/portal4.png 0 0 0 0
//    portal images/portal5.png 0 0 0 0
//    portal images/portal6.png 0 0 0 0
//    portal images/portal7.png 0 0 0 0
//    portal images/portal8.png 0 0 0 0
//    portal images/portal9.png 0 0 0 0
    /**
     * Helper method for testing. Preserve this functionality while refactoring.
     */
    public String log(){
        return this.id.isEmpty() ? null :
                String.format("%s %d %d %d", this.id, this.position.getX(), this.position.getY(), this.imageIndex);
    }
}
