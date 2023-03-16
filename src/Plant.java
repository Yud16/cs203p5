public interface Plant extends Entity{
    void setHealth(int i);
    int getHealth();
    boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore);
}
