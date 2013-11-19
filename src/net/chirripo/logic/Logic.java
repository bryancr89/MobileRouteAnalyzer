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
	
	public long AddRoute(String routeName, double latStart, double lngStart, double latStop, double lngStop) {
		return _repository.AddRoute(routeName, latStart, lngStart, latStop, lngStop);
	}
	
}
