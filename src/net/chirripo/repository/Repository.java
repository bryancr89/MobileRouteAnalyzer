package net.chirripo.repository;

import java.util.ArrayList;
import java.util.List;

import net.chirripo.entities.*;
import net.chirripo.models.*;
import net.chirripo.entities.DaoMaster.DevOpenHelper;
import net.chirripo.entities.WayPointsDao.Properties;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Repository implements IRepository {
	private SQLiteDatabase db;
	private DaoMaster daoMaster;
	private DaoSession daoSession;
	
	private RoutesDao routesDao;
	private WayPointsDao waypointsDao;
	private Context ctx;
	
	public Repository(Context ctx){
		this.ctx = ctx;
		CreateDB("llanoBonito");
	}
	
	public void CreateDB(String databaseName){
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(ctx, databaseName, null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		
		routesDao = daoSession.getRoutesDao();
		waypointsDao = daoSession.getWayPointsDao();
	}
	
	public long AddRoute(String routeName, double latStart, double lngStart, double latStop, double lngStop) {
		Routes route= new Routes(null, routeName, latStart, lngStart, latStop, lngStop);
        routesDao.insert(route);
		return route.getId();
	}

	public void AddWayPoint(long routeId, int count, double lat, double lng) {
		WayPoints wayPoint = new WayPoints(null, count, lat, lng, routeId);
		waypointsDao.insert(wayPoint);
	}
	
	public List<RouteModel> GetListRoutes() {
		List<Routes> routes = routesDao.queryBuilder().list();
		List<RouteModel> result = new ArrayList<RouteModel>();
		for (Routes element: routes) { 
			result.add(new RouteModel(element.getId(), element.getName()));
		}
		return result;
	}
	
	public int GetRunTimesRoute(long routeId){
		return waypointsDao.queryBuilder()
			.where(Properties.RouteId.eq(routeId))
			.orderDesc(Properties.Count).limit(1)
			.unique()
			.getCount();
	}
}
