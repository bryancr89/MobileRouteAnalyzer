package net.chirripo.logic;

import java.util.List;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import net.chirripo.models.*;
import net.chirripo.repository.Repository;

public class Logic {
	private Repository _repository;
	
	public Logic(Context ctx){
		_repository = new Repository(ctx);
	}
	
	public RouteModel GetFirstRunRouteById(long routeId){
		return _repository.GetFirstRunRouteById(routeId);
	}
	
	public List<RouteModel> GetListRoutes(){
		return _repository.GetListRoutes();
	}
	
	public long AddRoute(double latStart, double lngStart) {
		return _repository.AddRoute(latStart, lngStart);
	}
	
	public void UpdateRoute(long routeId, double latStop, double lngStop){
		_repository.UpdateRoute(routeId, latStop, lngStop);
	}
	
	public void SaveRoute(long routeId, String name, double duration, double distance){
		_repository.SaveRoute(routeId, name, duration, distance);
	}
	
	public void AddWayPoint(long routeId, int count, double lat, double lng, double distance){
		_repository.AddWayPoint(routeId, count, lat, lng, distance);
	}
	
	public void DeleteRoute(long routeId){
		_repository.DeleteWayPoints(routeId);
		_repository.DeleteRoute(routeId);
	}
	
	public int GetCountRouteRuns(long routeId){
		return _repository.GetCountRouteRuns(routeId);
	}
	
	public RouteModel GetSlowerRun(long routeId){
		return _repository.GetSlowerRun(routeId);
	}
	
	public RouteModel GetFasterRun(long routeId){
		return _repository.GetFasterRun(routeId);
	}
	
	public String GetSlowerRunDuration(long routeId){
		double slowerRun = _repository.GetSlowerRunDuration(routeId);
		return GetTime(slowerRun); 
	}
	
	public String GetFasterRunDuration(long routeId){
		double fastestRun = _repository.GetFasterRunDuration(routeId);
		return GetTime(fastestRun);
	}
	
	public String GetAvgRoute(long routeId){
		double avgRun = _repository.GetAvgRoute(routeId);
		return GetTime(avgRun);
	}
	
	private String GetTime(double totalSeconds){
		double hours = (totalSeconds / 3600),
				remainder = totalSeconds % 3600, 
				minutes = remainder / 60, 
				seconds = remainder % 60; 
		return String.format("%dh: %dm: %ds", (int)hours, (int)minutes, (int)seconds);
	}
}
