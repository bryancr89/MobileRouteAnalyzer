package net.chirripo.models;

public class WaypointsModel {

	private double _lat;
	private double _lng;
	
	public WaypointsModel(double lat, double lng){
		this._lat = lat;
		this._lng = lng;
	}
	
	public double getLat() {
		return _lat;
	}
	
	public void setLat(double _lat) {
		this._lat = _lat;
	}
	
	public double getLng() {
		return _lng;
	}
	
	public void setLng(double _lng) {
		this._lng = _lng;
	}
}
