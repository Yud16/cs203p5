public class Activity implements Action{
    private final ActiEntities entity;
    private final WorldModel world;
    private final ImageStore imageStore;
    public Activity(ActiEntities entity, WorldModel world, ImageStore imageStore) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }
    public void executeAction(EventScheduler scheduler) {
            entity.executeActivity(world, imageStore, scheduler);
    }
}
