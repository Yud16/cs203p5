import java.util.*;

import processing.core.PImage;
import processing.core.PApplet;

/**
 * This class contains many functions written in a procedural style.
 * You will reduce the size of this class over the next several weeks
 * by refactoring this codebase to follow an OOP style.
 */
public final class Functions {
    public static final Random rand = new Random();

    public static final int COLOR_MASK = 0xffffff;
    public static final int KEYED_IMAGE_MIN = 5;
    private static final int KEYED_RED_IDX = 2;
    private static final int KEYED_GREEN_IDX = 3;
    private static final int KEYED_BLUE_IDX = 4;

    public static final List<String> PATH_KEYS = new ArrayList<>(Arrays.asList("bridge", "dirt", "dirt_horiz", "dirt_vert_left", "dirt_vert_right", "dirt_bot_left_corner", "dirt_bot_right_up", "dirt_vert_left_bot"));

    public static final int PROPERTY_KEY = 0;
    public static final int PROPERTY_ID = 1;
    public static final int PROPERTY_COL = 2;
    public static final int PROPERTY_ROW = 3;
    public static final int ENTITY_NUM_PROPERTIES = 4;

    public static Action createAnimationAction(Animated entity, int repeatCount) {
        return new Animation(entity,repeatCount);
    }

    public static Action createActivityAction(ActiEntities entity, WorldModel world, ImageStore imageStore) {
        return new Activity(entity, world, imageStore);
    }
    public static int getIntFromRange(int max, int min) {
        Random rand = new Random();
        return min + rand.nextInt(max-min);
    }

    public static double getNumFromRange(double max, double min) {
        Random rand = new Random();
        return min + rand.nextDouble() * (max - min);
    }


    public static void parseSapling(WorldModel world, String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == EntityCreator.SAPLING_NUM_PROPERTIES) {
            int health = Integer.parseInt(properties[EntityCreator.SAPLING_HEALTH]);
            Entity entity = EntityCreator.createSapling(id, pt, imageStore.getImageList(EntityCreator.SAPLING_KEY), health);
            world.tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", EntityCreator.SAPLING_KEY, EntityCreator.SAPLING_NUM_PROPERTIES));
        }
    }

    public static void parseDude(WorldModel world, String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == EntityCreator.DUDE_NUM_PROPERTIES) {
            DudeNotFull entity = EntityCreator.createDudeNotFull(id, pt, imageStore.getImageList(EntityCreator.DUDE_KEY),Integer.parseInt(properties[EntityCreator.DUDE_LIMIT]), Double.parseDouble(properties[EntityCreator.DUDE_ACTION_PERIOD]), Double.parseDouble(properties[EntityCreator.DUDE_ANIMATION_PERIOD]));
            world.tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", EntityCreator.DUDE_KEY, EntityCreator.DUDE_NUM_PROPERTIES));
        }
    }

    public static void parseFairy(WorldModel world, String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == EntityCreator.FAIRY_NUM_PROPERTIES) {
            Entity entity = EntityCreator.createFairy(id, pt, Double.parseDouble(properties[EntityCreator.FAIRY_ACTION_PERIOD]), Double.parseDouble(properties[EntityCreator.FAIRY_ANIMATION_PERIOD]), imageStore.getImageList(EntityCreator.FAIRY_KEY));
            world.tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", EntityCreator.FAIRY_KEY, EntityCreator.FAIRY_NUM_PROPERTIES));
        }
    }

    public static void parseWizard(WorldModel world, String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == EntityCreator.WIZARD_NUM_PROPERTIES) {
            Entity entity = EntityCreator.createWizard(id, pt, imageStore.getImageList(EntityCreator.WIZARD_KEY), Double.parseDouble(properties[EntityCreator.WIZARD_ANIMATION_PERIOD]), Double.parseDouble(properties[EntityCreator.WIZARD_ACTION_PERIOD]));
            world.tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", EntityCreator.WIZARD_KEY, EntityCreator.WIZARD_NUM_PROPERTIES));
        }
    }

    public static void parseTree(WorldModel world, String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == EntityCreator.TREE_NUM_PROPERTIES) {
            Entity entity = EntityCreator.createTree(id, pt, Double.parseDouble(properties[EntityCreator.TREE_ACTION_PERIOD]), Double.parseDouble(properties[EntityCreator.TREE_ANIMATION_PERIOD]), Integer.parseInt(properties[EntityCreator.TREE_HEALTH]), imageStore.getImageList(EntityCreator.TREE_KEY));
            world.tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", EntityCreator.TREE_KEY, EntityCreator.TREE_NUM_PROPERTIES));
        }
    }

    public static void parseObstacle(WorldModel world, String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == EntityCreator.OBSTACLE_NUM_PROPERTIES) {
            Entity entity = EntityCreator.createObstacle(id, pt, Double.parseDouble(properties[EntityCreator.OBSTACLE_ANIMATION_PERIOD]), imageStore.getImageList(EntityCreator.OBSTACLE_KEY));
            world.tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", EntityCreator.OBSTACLE_KEY, EntityCreator.OBSTACLE_NUM_PROPERTIES));
        }
    }

    public static void parsePortal(WorldModel world, String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == EntityCreator.PORTAL_NUM_PROPERTIES) {
            Entity entity = EntityCreator.createPortal(id, pt, Double.parseDouble(properties[EntityCreator.PORTAL_ANIMATION_PERIOD]), imageStore.getImageList(EntityCreator.PORTAL_KEY));
            world.tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", EntityCreator.PORTAL_KEY, EntityCreator.PORTAL_NUM_PROPERTIES));
        }
    }

    public static void parseBurnedHouse(WorldModel world, String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == EntityCreator.BURNED_HOUSE_NUM_PROPERTIES) {
            Entity entity = EntityCreator.createBurnedHouse(id, pt, Double.parseDouble(properties[EntityCreator.BURNED_HOUSE_ANIMATION_PERIOD]), imageStore.getImageList(EntityCreator.BURNED_HOUSE_KEY));
            world.tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", EntityCreator.BURNED_HOUSE_KEY, EntityCreator.BURNED_HOUSE_NUM_PROPERTIES));
        }
    }

    public static void parseHouse(WorldModel world, String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == EntityCreator.HOUSE_NUM_PROPERTIES) {
            Entity entity = EntityCreator.createHouse(id, pt, imageStore.getImageList(EntityCreator.HOUSE_KEY));
            world.tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", EntityCreator.HOUSE_KEY, EntityCreator.HOUSE_NUM_PROPERTIES));
        }
    }

    public static void parseStump(WorldModel world, String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == EntityCreator.STUMP_NUM_PROPERTIES) {
            Entity entity = EntityCreator.createStump(id, pt, imageStore.getImageList(EntityCreator.STUMP_KEY));
            world.tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", EntityCreator.STUMP_KEY, EntityCreator.STUMP_NUM_PROPERTIES));
        }
    }



    /*
       Assumes that there is no entity currently occupying the
       intended destination cell.
    */

    public static void parseSaveFile(WorldModel world, Scanner saveFile, ImageStore imageStore, Background defaultBackground){
        String lastHeader = "";
        int headerLine = 0;
        int lineCounter = 0;
        while(saveFile.hasNextLine()){
            lineCounter++;
            String line = saveFile.nextLine().strip();
            if(line.endsWith(":")){
                headerLine = lineCounter;
                lastHeader = line;
                switch (line){
                    case "Backgrounds:" -> world.setBackground(new Background[world.getNumRows()][world.getNumCols()]);
                    case "Entities:" -> {
                        world.setOccupancy(new Entity[world.getNumRows()][world.getNumCols()]);
                        world.setEntities(new HashSet<>());
                    }
                }
            }else{
                switch (lastHeader){
                    case "Rows:" -> world.setNumRows(Integer.parseInt(line));
                    case "Cols:" -> world.setNumCols(Integer.parseInt(line));
                    case "Backgrounds:" -> Functions.parseBackgroundRow(world, line, lineCounter-headerLine-1, imageStore);
                    case "Entities:" -> Functions.parseEntity(world, line, imageStore);
                }
            }
        }
    }
    public static void parseBackgroundRow(WorldModel world, String line, int row, ImageStore imageStore) {
        String[] cells = line.split(" ");
        if(row < world.getNumRows()){
            int rows = Math.min(cells.length, world.getNumCols());
            for (int col = 0; col < rows; col++){
                world.setBackground(row,col, new Background(cells[col], imageStore.getImageList(cells[col])));
            }
        }
    }

    public static void parseEntity(WorldModel world, String line, ImageStore imageStore) {
        String[] properties = line.split(" ", Functions.ENTITY_NUM_PROPERTIES + 1);
        if (properties.length >= Functions.ENTITY_NUM_PROPERTIES) {
            String key = properties[PROPERTY_KEY];
            String id = properties[Functions.PROPERTY_ID];
            Point pt = new Point(Integer.parseInt(properties[Functions.PROPERTY_COL]), Integer.parseInt(properties[Functions.PROPERTY_ROW]));

            properties = properties.length == Functions.ENTITY_NUM_PROPERTIES ?
                    new String[0] : properties[Functions.ENTITY_NUM_PROPERTIES].split(" ");

            switch (key) {
                case EntityCreator.OBSTACLE_KEY -> Functions.parseObstacle(world, properties, pt, id, imageStore);
                case EntityCreator.DUDE_KEY -> Functions.parseDude(world, properties, pt, id, imageStore);
                case EntityCreator.FAIRY_KEY -> Functions.parseFairy(world, properties, pt, id, imageStore);
                case EntityCreator.HOUSE_KEY -> Functions.parseHouse(world, properties, pt, id, imageStore);
                case EntityCreator.TREE_KEY -> Functions.parseTree(world, properties, pt, id, imageStore);
                case EntityCreator.SAPLING_KEY -> Functions.parseSapling(world, properties, pt, id, imageStore);
                case EntityCreator.STUMP_KEY -> Functions.parseStump(world, properties, pt, id, imageStore);
                case EntityCreator.PORTAL_KEY-> Functions.parsePortal(world, properties, pt, id, imageStore);
                case EntityCreator.WIZARD_KEY -> Functions.parseWizard(world, properties, pt, id, imageStore);
                case EntityCreator.BURNED_HOUSE_KEY -> Functions.parseBurnedHouse(world, properties, pt, id, imageStore);
                default -> throw new IllegalArgumentException("Entity key is unknown");
            }
        }else{
            throw new IllegalArgumentException("Entity must be formatted as [key] [id] [x] [y] ...");
        }
    }

    public static int clamp(int value, int low, int high) {
        return Math.min(high, Math.max(value, low));
    }

    public static void processImageLine(Map<String, List<PImage>> images, String line, PApplet screen) {
        String[] attrs = line.split("\\s");
        if (attrs.length >= 2) {
            String key = attrs[0];
            PImage img = screen.loadImage(attrs[1]);
            if (img != null && img.width != -1) {
                List<PImage> imgs = getImages(images, key);
                imgs.add(img);

                if (attrs.length >= KEYED_IMAGE_MIN) {
                    int r = Integer.parseInt(attrs[KEYED_RED_IDX]);
                    int g = Integer.parseInt(attrs[KEYED_GREEN_IDX]);
                    int b = Integer.parseInt(attrs[KEYED_BLUE_IDX]);
                    setAlpha(img, screen.color(r, g, b), 0);
                }
            }
        }
    }

    public static List<PImage> getImages(Map<String, List<PImage>> images, String key) {
        return images.computeIfAbsent(key, k -> new LinkedList<>());
    }

    /*
      Called with color for which alpha should be set and alpha value.
      setAlpha(img, color(255, 255, 255), 0));
    */
    public static void setAlpha(PImage img, int maskColor, int alpha) {
        int alphaValue = alpha << 24;
        int nonAlpha = maskColor & COLOR_MASK;
        img.format = PApplet.ARGB;
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            if ((img.pixels[i] & COLOR_MASK) == nonAlpha) {
                img.pixels[i] = alphaValue | nonAlpha;
            }
        }
        img.updatePixels();
    }

}
