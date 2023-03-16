import processing.core.PImage;
/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public interface Entity {
    String getId();
    Point getPosition();
    void setPosition(Point position);
    String log();
    PImage getCurrentImage();
}
