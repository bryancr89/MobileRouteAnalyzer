package net.chirripo.logic;

import java.util.List;

import android.content.Context;
import net.chirripo.models.*;
import net.chirripo.repository.Repository;

public class Logic {
	private Repository _repository;
	
	public Logic(Context ctx){
		_repository = new Repository(ctx);
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
	
	public void SaveRoute(long routeId, String name){
		_repository.SaveRoute(routeId, name);
	}
	
	public void AddWayPoint(long routeId, int count, double lat, double lng){
		_repository.AddWayPoint(routeId, count, lat, lng);
	}
	
	public void DeleteRoute(long routeId){
		_repository.DeleteWayPoints(routeId);
		_repository.DeleteRoute(routeId);
	}
}
