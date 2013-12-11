package net.chirripo.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.chirripo.entities.DaoMaster;
import net.chirripo.entities.DaoMaster.DevOpenHelper;
import net.chirripo.entities.DaoSession;
import net.chirripo.entities.Routes;
import net.chirripo.entities.RoutesDao;
import net.chirripo.entities.RunRoutes;
import net.chirripo.entities.RunRoutesDao;
import net.chirripo.entities.WayPoints;
import net.chirripo.entities.WayPointsDao;
import net.chirripo.entities.WayPointsDao.Properties;
import net.chirripo.models.RouteModel;
import net.chirripo.models.WaypointsModel;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Repository implements IRepository {
	//Properties
	private SQLiteDatabase _db;
	private DaoMaster _daoMaster;
	private DaoSession _daoSession;	
	private RoutesDao _routesDao;
	private RunRoutesDao _runRoutesDao;
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
		_runRoutesDao = _daoSession.getRunRoutesDao();
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
	
	public void SaveRoute(long routeId, String name, double duration, double distance){
		Routes route = GetRoute(routeId);
		route.setName(name);
		
		SaveRanRoute(routeId, duration, distance);
		_routesDao.update(route);
	}
	
	public void SaveRanRoute(long routeId, double duration, double distance){
		int count = GetCountRouteRuns(routeId);
		
		RunRoutes runRoute = new RunRoutes(null,distance, duration, count, new Date(), routeId);
		_runRoutesDao.insert(runRoute);
	}
	
	public void AddWayPoint(long routeId, int count, double lat, double lng, double distance){
		WayPoints wayPoint = new WayPoints(null, lat, lng, distance, routeId, count);
		_waypointsDao.insert(wayPoint);
	}
	
	public RouteModel GetFirstRunRouteById(long routeId){
		Routes route = GetRoute(routeId);
		RunRoutes firstRunRoute = _runRoutesDao.queryBuilder()
								.where(net.chirripo.entities.RunRoutesDao.Properties.RouteId.eq(routeId))
								.orderAsc(net.chirripo.entities.RunRoutesDao.Properties.Count)
								.limit(1)
								.unique();
		List<WayPoints> waypoints = _waypointsDao.queryBuilder()
								.where(Properties.Count.eq(firstRunRoute.getCount())).list();
		return CreateRouteModel(firstRunRoute, route, waypoints);
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
		return _runRoutesDao.queryBuilder()
			.where(net.chirripo.entities.RunRoutesDao.Properties.RouteId.eq(routeId))
			.orderDesc(net.chirripo.entities.RunRoutesDao.Properties.Count).limit(1)
			.unique()
			.getCount();
	}
	
	public void DeleteRoute(long routeId){
		_routesDao.deleteByKey(routeId);
	}
	
	public void DeleteWayPoints(long routeId){
		List<WayPoints> waypoints =  _waypointsDao.queryBuilder()
				.where(net.chirripo.entities.WayPointsDao.Properties.RouteId.eq(routeId))
				.list();
			
		for(WayPoints i: waypoints){
			_waypointsDao.deleteByKey(i.getId());
		}
	}

	public int GetCountRouteRuns(long routeId){
		RunRoutes element = _runRoutesDao.queryBuilder()
								.where(net.chirripo.entities.RunRoutesDao.Properties.RouteId.eq(routeId))
								.orderDesc(net.chirripo.entities.RunRoutesDao.Properties.Count)
								.limit(1)
								.unique();		
		return element != null ?  element.getCount() + 1 : 0;
	}
	
	public RouteModel GetSlowerRun(long routeId){
		Routes route = GetRoute(routeId);
		RunRoutes slowerRun = _runRoutesDao.queryBuilder()
								.where(net.chirripo.entities.RunRoutesDao.Properties.RouteId.eq(routeId))
								.orderAsc(net.chirripo.entities.RunRoutesDao.Properties.Duration)
								.limit(1)
								.unique();
		List<WayPoints> waypoints = _waypointsDao.queryBuilder()
								.where(Properties.Count.eq(slowerRun.getCount())).list();
		return CreateRouteModel(slowerRun, route, waypoints);
	}
	
	public RouteModel GetFasterRun(long routeId){
		Routes route = GetRoute(routeId);
		RunRoutes fasterRun = _runRoutesDao.queryBuilder()
				.where(net.chirripo.entities.RunRoutesDao.Properties.RouteId.eq(routeId))
				.orderDesc(net.chirripo.entities.RunRoutesDao.Properties.Duration)
				.limit(1)
				.unique();
		List<WayPoints> waypoints = _waypointsDao.queryBuilder()
						.where(Properties.Count.eq(fasterRun.getCount())).list();
		return CreateRouteModel(fasterRun, route, waypoints);
	}
	
	public RunRoutes GetSlowerRunDuration(long routeId){
		RunRoutes slowerRun = _runRoutesDao.queryBuilder()
				.where(net.chirripo.entities.RunRoutesDao.Properties.RouteId.eq(routeId))
				.orderDesc(net.chirripo.entities.RunRoutesDao.Properties.Duration)
				.limit(1)
				.unique();
		return slowerRun;
	}
	
	public RunRoutes GetFasterRunDuration(long routeId){
		RunRoutes fasterRun = _runRoutesDao.queryBuilder()
				.where(net.chirripo.entities.RunRoutesDao.Properties.RouteId.eq(routeId))
				.orderAsc(net.chirripo.entities.RunRoutesDao.Properties.Duration)
				.limit(1)
				.unique();
		return fasterRun;
	}
	
	public double GetAvgRoute(long routeId){
		double avg = 0;
		List<RunRoutes> runs = _runRoutesDao.queryBuilder()
				.where(net.chirripo.entities.RunRoutesDao.Properties.RouteId.eq(routeId))
				.list();
		
		if(runs != null &&  runs.size() != 0){
			for(RunRoutes i: runs){
				avg += i.getDuration();
			}
			avg = avg / runs.size();
		}
		return avg;
	}
	
	//Private methods
	private Routes GetRoute(long routeId) {
		return _routesDao.queryBuilder().where(net.chirripo.entities.RunRoutesDao.Properties.Id.eq(routeId)).unique();
	}
	
	private RouteModel CreateRouteModel(RunRoutes runRoute, Routes route, List<WayPoints> waypoints){
		RouteModel result = new RouteModel();
		if(runRoute != null) {
			result.setDistance(runRoute.getDistance());
			result.setDuration(runRoute.getDuration());
			//result.setRunDate(runRoute.getRundate());
		}
		if(route != null){
			result.setId(route.getId());
			result.setName(route.getName());
		}
		if(waypoints != null){
			List<WaypointsModel> waypointsList = new ArrayList<WaypointsModel>();
			for(WayPoints i: waypoints){
				waypointsList.add(new WaypointsModel(i.getLat(), i.getLng()));
			}
			result.setWaypoints(waypointsList);
		}
		return result;
	}
}
