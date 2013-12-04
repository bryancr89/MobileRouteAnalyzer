package net.chirripo.tabs;


import java.util.ArrayList;
import java.util.List;

import net.chirripo.logic.Logic;
import net.chirripo.mobilerouteanalyzer.R;
import net.chirripo.models.RouteModel;
import net.chirripo.models.WaypointsModel;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;


public class RunningRoute extends Activity {	
	
	private Logic _logic;	
	private GoogleMap _map;
	private Bundle _bundle;
	private String _routeName;
	private long _routeId;
	private float _zoom = 16;
	RouteModel routeObject = null;
	List<WaypointsModel> _routeWayPoints = new ArrayList<WaypointsModel>();
	private List<LatLng> _wayPointsList = new ArrayList<LatLng>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running_route);
                
        //get parameters from the other activity
        _bundle = getIntent().getExtras();
        _routeName = _bundle.getString("routeName");
        _routeId = _bundle.getLong("routeId"); 
        _logic = new Logic(this);
        
        routeObject = _logic.GetFirstRunRouteById(_routeId);
        
        //set the route name to the activity title
        setTitle(_routeName);
        
        //map initialization  
        _map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map_running)).getMap();               
        _map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        _map.setMyLocationEnabled(true);
        
        //call the function to center the map and draw the route on the map
        centerMapAndShowRoute();
    }
    
	private void centerMapAndShowRoute(){
		_routeWayPoints = routeObject.getWaypoints();
	     
	    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(_routeWayPoints.get(0).getLat(), _routeWayPoints.get(0).getLng()));
	    CameraUpdate zoom = CameraUpdateFactory.zoomTo(_zoom);
	
	    _map.moveCamera(center);
	    _map.animateCamera(zoom);
	     
	    
	    for(WaypointsModel wayPoint: _routeWayPoints){
	    	_wayPointsList.add(new LatLng(wayPoint.getLat(), wayPoint.getLng()));
	    }
	    
	    
	    //send to draw the route between the start point and the end point
		PolylineOptions po = new PolylineOptions();
		po.addAll(_wayPointsList);
		po.width(4).color(Color.BLUE);
		_map.addPolyline(po);	
	     
	     
	}
}