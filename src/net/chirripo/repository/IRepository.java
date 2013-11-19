package net.chirripo.repository;

import java.util.List;
import net.chirripo.models.*;

public interface IRepository {
	
	void CreateDB(String databaseName);
	
	long AddRoute(String routeName, double latStart, double lngStart, double latStop, double lngStop);
	
	void AddWayPoint(long routeId, int count, double lat, double lng);
	
	List<RouteModel>GetListRoutes();
	
	int GetRunTimesRoute(long routeId);
	
}
