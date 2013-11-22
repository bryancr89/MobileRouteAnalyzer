package net.chirripo.repository;

import java.util.List;
import net.chirripo.models.*;

public interface IRepository {
	
	void CreateDB(String databaseName);
	
	long AddRoute(double latStart, double lngStart);
	
	void UpdateRoute(long routeId, double latStop, double lngStop);
	
	void SaveRoute(long routeId, String name);
	
	void AddWayPoint(long routeId, int count, double lat, double lng);
	
	List<RouteModel>GetListRoutes();
	
	int GetRunTimesRoute(long routeId);
	
	void DeleteRoute(long routeId);
	
	void DeleteWayPoints(long routeId);	
}
