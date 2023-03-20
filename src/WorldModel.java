import processing.core.PImage;

import java.util.*;

/**
 * Represents the 2D World in which this simulation is running.
 * Keeps track of the size of the world, the background image for each
 * location in the world, and the entities that populate the world.
 */
public final class WorldModel {
    private static int numRows;
    private static int numCols;
    private Background[][] background;
    private static Entity[][] occupancy;
    private static Set<Entity> entities;

    public WorldModel() {

    }

    public Background[][] getBackground() {
        return background;
    }
    public void setBackground(Background[][] b) {
        this.background = b;
    }
    public void setBackground(int r, int c, Background b) {
        this.background[r][c] = b;
    }

    public Set<Entity> getEntities() {
        return entities;
    }
    public void setEntities(Set<Entity> e) {
        this.entities = e;
    }


    public Entity[][] getOccupancy() {
        return occupancy;
    }
    public void setOccupancy(Entity[][] o) {
        this.occupancy = o;
    }

    public void setNumCols(int num) {
        this.numCols = num;
    }
    public void setNumRows(int n) {
        this.numRows = n;
    }
    public int getNumCols() {
        return numCols;
    }
    public int getNumRows() {
        return numRows;
    }

    public void addEntity(Entity entity) {
        if (withinBounds(entity.getPosition())) {
            setOccupancyCell(entity.getPosition(), entity);
            entities.add(entity);
        }
    }

    public Optional<PImage> getBackgroundImage(Point pos) {
        if (withinBounds(pos)) {
            return Optional.of(getBackgroundCell(pos).getCurrentImage());
        } else {
            return Optional.empty();
        }
    }
    public void makeWizardTrail(ImageStore imageStore, Point position) {
        Background ground = new Background("ground", imageStore.getImageList("ground"));
        setBackgroundCell(position, ground);
    }
    public void setBackgroundCell(Point pos, Background background) {
        this.background[pos.getY()][pos.getX()] = background;
    }
    public void setPortalBG(Point pressed, Background ground) {
        int y = pressed.getY();
        int x = pressed.getX();

        setBackground(y-2, x-1, ground);
        setBackground(y-1, x-1, ground);
        setBackground(y-1, x, ground);
        setBackground(y-1, x+1, ground);
        setBackground(y-1, x+2, ground);
        setBackground(y, x-2, ground);
        setBackground(y, x-1, ground);
        setBackground(y, x, ground);
        setBackground(y, x+1, ground);
        setBackground(y+1,x-1, ground);
        setBackground(y+1, x, ground);
        setBackground(y +1, x +1, ground);
        setBackground(y +1, x + 2, ground);
        setBackground(y + 2, x - 2, ground);
        setBackground(y + 2, x - 1, ground);
        setBackground(y + 2, x + 1, ground);
    }

    private Background getBackgroundCell(Point pos) {
        return background[pos.getY()][pos.getX()];
    }

    public Optional<Entity> findNearest(Point pos, List<Class> kinds) {
        List<Entity> ofType = new LinkedList<>();
        for (Class kind : kinds) {
            for (Entity entity : entities) {
                if (entity.getClass() == kind) {
                    ofType.add(entity);
                }
            }
        }

        return pos.nearestEntity(ofType);
    }

    public void removeEntity(EventScheduler scheduler, Entity entity) {
        scheduler.unscheduleAllEvents(entity);
        removeEntityAt(entity.getPosition());
    }

    public boolean isOccupied(Point pos) {
        return withinBounds(pos) && getOccupancyCell(pos) != null;
    }

    public void removeEntityAt(Point pos) {
        if (withinBounds(pos) && getOccupancyCell(pos) != null) {
            Entity entity = getOccupancyCell(pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.setPosition(new Point(-1, -1));
            entities.remove(entity);
            setOccupancyCell(pos, null);
        }
    }

    public Optional<Entity> getOccupant(Point pos) {
        if (this.isOccupied(pos)) {
            return Optional.of(getOccupancyCell(pos));
        } else {
            return Optional.empty();
        }
    }

    public Entity getOccupancyCell(Point pos) {
        return occupancy[pos.getY()][pos.getX()];
    }

    public void load(Scanner saveFile, ImageStore imageStore, Background defaultBackground){
        Functions.parseSaveFile(this, saveFile, imageStore, defaultBackground);
        if(background == null){
            background = new Background[numRows][numCols];
            for (Background[] row : background)
                Arrays.fill(row, defaultBackground);
        }
        if(occupancy == null){
            occupancy = new Entity[numRows][numCols];
            entities = new HashSet<>();
        }
    }

    public void tryAddEntity(Entity entity) {
        if (this.isOccupied(entity.getPosition())) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        addEntity(entity);
    }

    /**
     * Helper method for testing. Don't move or modify this method.
     */
    public List<String> log(){
        List<String> list = new ArrayList<>();
        for (Entity entity : entities) {
            String log = entity.log();
            if(log != null) list.add(log);
        }
        return list;
    }

    public boolean withinBounds(Point point) {
        return point.getY() >= 0 && point.getY() < numRows && point.getX() >= 0 && point.getX() < numCols;
    }

    public void setOccupancyCell(Point pos, Entity entity) {
        occupancy[pos.getY()][pos.getX()] = entity;
    }

    public void moveEntity(EventScheduler scheduler, Point pos, Entity entity) {
        Point oldPos = entity.getPosition();
        if (withinBounds(pos) && !pos.equals(oldPos)) {
            setOccupancyCell(oldPos, null);
            Optional<Entity> occupant = getOccupant(pos);
            occupant.ifPresent(target -> removeEntity(scheduler, target));
            setOccupancyCell(pos, entity);
            entity.setPosition(pos);
        }
    }
}
