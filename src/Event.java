/**
 * An event is made up of an Entity that is taking an
 * Action a specified time.
 */
public final class Event {
    public Action getAction() {
        return action;
    }

    private Action action;

    public double getTime() {
        return time;
    }
    private double time;

    public Entity getEntity() {
        return entity;
    }

    private Entity entity;

    public Event(Action action, double time, Entity entity) {
        this.action = action;
        this.time = time;
        this.entity = entity;
    }
}
