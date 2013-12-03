package net.chirripo.models;

import java.util.List;

public class RouteModel {
	private long _id;
	private String _name;
	private long _latStart;
	private long _lngStart;
	private long _latStop;
	private long _lngStop;;
	private double _distance;
	private double _duration;
	private List<WaypointsModel> _waypoints;

	public RouteModel() { }
	
	public RouteModel(long id, String name) {
		super();
		this.setId(id);
		this.setName(name);
	}
	
	public RouteModel(long id, String name, long latStart, long lngStart, long latStop, long lngStop, List<WaypointsModel> waypoints) {
		this.setId(id);
		this.setName(name);
		this.setLatStart(latStart);
		this.setLngStart(lngStart);
		this.setLatStop(latStop);
		this.setLngStop(lngStop);
		this.setWaypoints(waypoints);
	}
	
	public long getId() {
		return _id;
	}

	public void setId(long id) {
		this._id = id;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		this._name = name;
	}

	public long getLatStart() {
		return _latStart;
	}

	public void setLatStart(long _latStart) {
		this._latStart = _latStart;
	}

	public long getLatStop() {
		return _latStop;
	}

	public void setLatStop(long _latStop) {
		this._latStop = _latStop;
	}

	public long getLngStop() {
		return _lngStop;
	}

	public void setLngStop(long _lngStop) {
		this._lngStop = _lngStop;
	}

	public List<WaypointsModel> getWaypoints() {
		return _waypoints;
	}

	public void setWaypoints(List<WaypointsModel> _waypoints) {
		this._waypoints = _waypoints;
	}

	public long getLngStart() {
		return _lngStart;
	}

	public void setLngStart(long _lngStart) {
		this._lngStart = _lngStart;
	}

	public double getDistance() {
		return _distance;
	}

	public void setDistance(double _distance) {
		this._distance = _distance;
	}

	public double getDuration() {
		return _duration;
	}

	public void setDuration(double _duration) {
		this._duration = _duration;
	}
}
