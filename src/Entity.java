import processing.core.PImage;

import java.util.List;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public interface Entity {
    String getId();
    Point getPosition();
    void setPosition(Point position);
    List<PImage> getImages();
    default String log() {
        return this.getId().isEmpty() ? null :
                String.format("%s %d %d %d", this.getId(), this.getPosition().getX(), this.getPosition().getY(), this.getImageIndex());
    }
    default PImage getCurrentImage() {
        return this.getImages().get(this.getImageIndex() % this.getImages().size());
    }
    int getImageIndex();
    default void printInfo() {
        String msg = getId() + ": " + this.getClass() + " : ";
        if (this instanceof Plant) {
            Plant p = (Plant) this;
            msg = msg + p.getHealth();
        }
        System.out.println(msg);
    }
}

