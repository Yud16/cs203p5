//import processing.core.PImage;
//import java.util.List;
///**
// * An entity that exists in the world. See EntityKind for the
// * different kinds of entities that exist.
// */
//public final class burningHouse implements Entity, Animated{
//    private final String id;
//    private Point position;
//    private final  List<PImage> images;
//    private final int imageIndex;
//
//    public burningHouse(String id, Point position, List<PImage> images) {
//        this.id = id;
//        this.position = position;
//        this.images = images;
//        imageIndex = 0;
//    }
//    public String getId() {
//        return id;
//    }
//    public Point getPosition() {
//        return position;
//    }
//
//    public void setPosition(Point position) {
//        this.position = position;
//    }
//
//    /**
//     * Helper method for testing. Preserve this functionality while refactoring.
//     */
//    public String log(){
//        return this.id.isEmpty() ? null :
//                String.format("%s %d %d %d", this.id, this.position.getX(), this.position.getY(), this.imageIndex);
//    }
//
//    @Override
//    public PImage getCurrentImage() {
//        return this.images.get(this.imageIndex % this.images.size());
//    }
//
//}
