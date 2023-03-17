import processing.core.PImage;

import java.util.List;

public abstract class Plant implements Entity, ActiEntities{
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;
    private final double actionPeriod;
    private final double animationPeriod;

    private int health;
    public Plant(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod, int health) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.health = health;
    }
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Functions.createActivityAction(this, world, imageStore), getActionPeriod());
        scheduler.scheduleEvent(this, Functions.createAnimationAction(this, 0), getAnimationPeriod());

    }
    public double getActionPeriod() {
        return actionPeriod;
    }
    public void setHealth(int i) {
        this.health = i;
    }
    public int getHealth() {
        return health;
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
    public void nextImage() {
        imageIndex = imageIndex + 1;
    }

    public double getAnimationPeriod() {
        return this.animationPeriod;
    }
    @Override
    public int getImageIndex() {
        return imageIndex;
    }

    abstract boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore);
}
