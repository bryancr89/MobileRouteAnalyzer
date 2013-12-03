package net.chirripo.entities;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table RUN_ROUTES.
 */
public class RunRoutes {

    private Long id;
    private double distance;
    private double duration;
    private int count;
    private long routeId;

    public RunRoutes() {
    }

    public RunRoutes(Long id) {
        this.id = id;
    }

    public RunRoutes(Long id, double distance, double duration, int count, long routeId) {
        this.id = id;
        this.distance = distance;
        this.duration = duration;
        this.count = count;
        this.routeId = routeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getRouteId() {
        return routeId;
    }

    public void setRouteId(long routeId) {
        this.routeId = routeId;
    }

}