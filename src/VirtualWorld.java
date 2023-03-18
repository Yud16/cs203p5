import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import processing.core.*;

public final class VirtualWorld extends PApplet {
    private static String[] ARGS;

    private static final int VIEW_WIDTH = 640;
    private static final int VIEW_HEIGHT = 480;
    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;

    private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;

    private static final String IMAGE_LIST_FILE_NAME = "imagelist";
    private static final String DEFAULT_IMAGE_NAME = "background_default";
    private static final int DEFAULT_IMAGE_COLOR = 0x808080;

    private static final String FAST_FLAG = "-fast";
    private static final String FASTER_FLAG = "-faster";
    private static final String FASTEST_FLAG = "-fastest";
    private static final double FAST_SCALE = 0.5;
    private static final double FASTER_SCALE = 0.25;
    private static final double FASTEST_SCALE = 0.10;

    private String loadFile = "world.sav";
    private long startTimeMillis = 0;
    private double timeScale = 1.0;

    private ImageStore imageStore;
    private WorldModel world;
    private WorldView view;
    private EventScheduler scheduler;

    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        parseCommandLine(ARGS);
        loadImages(IMAGE_LIST_FILE_NAME);
        loadWorld(loadFile, this.imageStore);

        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world, TILE_WIDTH, TILE_HEIGHT);
        this.scheduler = new EventScheduler();
        this.startTimeMillis = System.currentTimeMillis();
        this.scheduleActions(world, scheduler, imageStore);
    }

    public void draw() {
        double appTime = (System.currentTimeMillis() - startTimeMillis) * 0.001;
        double frameTime = (appTime - scheduler.getCurrentTime())/timeScale;
        this.update(frameTime);
        view.drawViewport();
    }

    private void update(double frameTime){
        scheduler.updateOnTime(frameTime);
    }

    // Just for debugging and for P5
    // Be sure to refactor this method as appropriate
    public void mousePressed() {
        Point pressed = mouseToPoint();
        System.out.println("CLICK! " + pressed.getX() + ", " + pressed.getY());

        Optional<Entity> entityOptional = world.getOccupant(pressed);
        if (entityOptional.isPresent()) {
            Entity entity = entityOptional.get();
            String msg = entity.getId() + ": " + entity.getClass() + " : ";
            if (entity instanceof Plant) {
                Plant p = (Plant) entity;
                msg = msg + p.getHealth();
            }
            System.out.println(msg);


            if (entity.getClass() == Portal.class) {
                Point pressedLeft = new Point(pressed.getX() - 1, pressed.getY());
                // CREATING THE WIZARD
                Wizard someWizard = EntityCreator.createWizard("new wizard", pressedLeft,
                        imageStore.getImageList(EntityCreator.WIZARD_KEY), 1.15, 1.15);
                world.addEntity(someWizard);
                someWizard.scheduleActions(scheduler, world, imageStore);

                Background blue = new Background("blue", imageStore.getImageList("blue"));
                // HERE: have to change the background images
                // TODO: make this more efficient/change to helper method

                // SETTING THE BACKGROUND
                int y = pressed.getY();
                int x = pressed.getX();

                world.setBackground(y-1, x+1, blue);
                world.setBackground(y-1, x, blue);
                world.setBackground(y-1, x-1, blue);
                world.setBackground(y, x+1, blue);
                world.setBackground(y, x, blue);
                world.setBackground(y, x-1, blue);
                world.setBackground(y +1, x +1, blue);
                world.setBackground(y+1, x, blue);
                world.setBackground(y+1,x-1, blue);

                // TODO: BUSH SHOULD REALLY BE MODELED AFTER HOUSE
                // ADDING THE BUSHES
                Bush bushTopLeft = EntityCreator.createBush("bush",
                        new Point(pressed.getX() - 1, pressed.getY() -1), 0,
                        imageStore.getImageList(EntityCreator.BUSH_KEY));

                Bush bushTopRight = EntityCreator.createBush("bush",
                        new Point(pressed.getX() + 1, pressed.getY() -1), 0,
                        imageStore.getImageList(EntityCreator.BUSH_KEY));

                Bush bushBottomLeft = EntityCreator.createBush("bush",
                        new Point(pressed.getX() - 1, pressed.getY() + 1), 0,
                        imageStore.getImageList(EntityCreator.BUSH_KEY));

                Bush bushBottomRight = EntityCreator.createBush("bush",
                        new Point(pressed.getX() + 1, pressed.getY() + 1), 0,
                        imageStore.getImageList(EntityCreator.BUSH_KEY));

                world.addEntity(bushTopLeft);
                world.addEntity(bushTopRight);
                world.addEntity(bushBottomLeft);
                world.addEntity(bushBottomRight);
            }
        }
        // TODO: have to animate the surrounding tiles when a wizard is pressed,
        // TODO: have to make portal and wizard bigger (have to look better)
        // TODO: going to write it to work first, then add in some helper methods
        // TODO: there is error when fairy and wizard have to cross paths ... that will be fixed with further implemtation

        // idea: could we illuminate the path from the wizard to the fairy (and kind of darken everything else)
    }

    private void scheduleActions(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        for (Entity entity : world.getEntities()) {
            if (entity instanceof Animated) {
                ((Animated) entity).scheduleActions(scheduler, world, imageStore);
            }
        }
    }

    private Point mouseToPoint() {
        return view.getViewport().viewportToWorld(mouseX / TILE_WIDTH, mouseY / TILE_HEIGHT);
    }

    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP -> dy -= 1;
                case DOWN -> dy += 1;
                case LEFT -> dx -= 1;
                case RIGHT -> dx += 1;
            }
            view.shiftView(dx, dy);
        }
    }

    private static Background createDefaultBackground(ImageStore imageStore) {
        return new Background(DEFAULT_IMAGE_NAME, imageStore.getImageList(DEFAULT_IMAGE_NAME));
    }

    private static PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        Arrays.fill(img.pixels, color);
        img.updatePixels();
        return img;
    }

    private void loadImages(String filename) {
        this.imageStore = new ImageStore(createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
        try {
            Scanner in = new Scanner(new File(filename));
            imageStore.loadImages(in, this);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    private void loadWorld(String file, ImageStore imageStore) {
        this.world = new WorldModel();
        try {
            Scanner in = new Scanner(new File(file));
            world.load(in, imageStore, createDefaultBackground(imageStore));
        } catch (FileNotFoundException e) {
            Scanner in = new Scanner(file);
            world.load(in, imageStore, createDefaultBackground(imageStore));
        }
    }

    private void parseCommandLine(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG -> timeScale = Math.min(FAST_SCALE, timeScale);
                case FASTER_FLAG -> timeScale = Math.min(FASTER_SCALE, timeScale);
                case FASTEST_FLAG -> timeScale = Math.min(FASTEST_SCALE, timeScale);
                default -> loadFile = arg;
            }
        }
    }

    public static void main(String[] args) {
        VirtualWorld.ARGS = args;
        PApplet.main(VirtualWorld.class);
    }

    public static List<String> headlessMain(String[] args, double lifetime){
        VirtualWorld.ARGS = args;

        VirtualWorld virtualWorld = new VirtualWorld();
        virtualWorld.setup();
        virtualWorld.update(lifetime);

        return virtualWorld.world.log();
    }
}
