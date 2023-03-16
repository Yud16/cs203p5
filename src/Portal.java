import processing.core.PImage;

import java.util.List;

public class Portal implements Entity, Animated {
    private String id;
    private Point position;
    private List<PImage> images;
    private int animationPeriod;
    public Portal(String id, Point position, List<PImage> images, int animationPeriod) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.animationPeriod = animationPeriod;
    }


    @Override
    public double getAnimationPeriod() {
        return 0;
    }

    @Override
    public void nextImage() {

    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {

    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public Point getPosition() {
        return null;
    }

    @Override
    public void setPosition(Point position) {

    }

    @Override
    public String log() {
        return null;
    }

    @Override
    public PImage getCurrentImage() {
        return null;
    }
}
