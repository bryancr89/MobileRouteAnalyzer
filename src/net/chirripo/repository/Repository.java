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
	//Properties
	private SQLiteDatabase _db;
	private DaoMaster _daoMaster;
	private DaoSession _daoSession;	
	private RoutesDao _routesDao;
	private WayPointsDao _waypointsDao;
	private Context _ctx;
		
	//Public methods
	public Repository(Context ctx){
		this._ctx = ctx;
		CreateDB("llanoBonito");
	}
	
	public void CreateDB(String databaseName){
		//Little magic for the database.
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(_ctx, databaseName, null);
		_db = helper.getWritableDatabase();
		_daoMaster = new DaoMaster(_db);
		_daoSession = _daoMaster.newSession();
		
		//Define instances of the tables.
		_routesDao = _daoSession.getRoutesDao();
		_waypointsDao = _daoSession.getWayPointsDao();
	}
	
	public long AddRoute(double latStart, double lngStart){
		Routes route= new Routes(null, "", latStart, lngStart, 0, 0);
        _routesDao.insert(route);
		return route.getId();
	}
	
	public void UpdateRoute(long routeId, double latStop, double lngStop){
		Routes route = GetRoute(routeId);
		route.setLatStop(latStop);
		route.setLngStop(lngStop);
		_routesDao.update(route);
	}
	
	public void SaveRoute(long routeId, String name){
		Routes route = GetRoute(routeId);
		route.setName(name);
		_routesDao.update(route);
	}
	
	public void AddWayPoint(long routeId, int count, double lat, double lng){
		WayPoints wayPoint = new WayPoints(null, count, lat, lng, routeId);
		_waypointsDao.insert(wayPoint);
	}
	
	public List<RouteModel> GetListRoutes() {
		List<Routes> routes = _routesDao.queryBuilder().list();
		List<RouteModel> result = new ArrayList<RouteModel>();
		for (Routes element: routes) { 
			result.add(new RouteModel(element.getId(), element.getName()));
		}
		return result;
	}
	
	public int GetRunTimesRoute(long routeId){
		return _waypointsDao.queryBuilder()
			.where(Properties.RouteId.eq(routeId))
			.orderDesc(Properties.Count).limit(1)
			.unique()
			.getCount();
	}
	
	//Private methods
	private Routes GetRoute(long routeId) {
		return _routesDao.queryBuilder().where(Properties.Id.eq(routeId)).unique();
	}
}
