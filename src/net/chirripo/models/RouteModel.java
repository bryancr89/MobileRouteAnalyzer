package net.chirripo.models;

public class RouteModel {
	public long id;
	public String name;
	public long latStart;
	public long lngStart;
	public long latStop;
	public long lngStop;
	
	public RouteModel(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public RouteModel(long id, String name, long latStart, long lngStart, long latStop, long lngStop) {
		this.id = id;
		this.name = name;
		this.latStart = latStart;
		this.lngStart = lngStart;
		this.latStop = latStop;
		this.lngStop = lngStop;
	}
}
