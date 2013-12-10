package net.chirripo.tabs;


import java.util.ArrayList;
import java.util.List;

import net.chirripo.logic.Logic;
import net.chirripo.mobilerouteanalyzer.R;
import net.chirripo.models.RouteModel;
import net.chirripo.models.WaypointsModel;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


public class RunningRoute extends Activity {	
	
	private Logic _logic;	
	private GoogleMap _map;
	private Bundle _bundle;
	private Context _ctx;
	private String _routeName;
	private long _routeId;
	private float _zoom = 16;
	RouteModel routeObject = null;
	List<WaypointsModel> _routeWayPoints = new ArrayList<WaypointsModel>();
	private List<LatLng> _routeWayPointsList = new ArrayList<LatLng>();
	private List<LatLng> _runningRouteWayPoints = new ArrayList<LatLng>();
	private LatLng _startRoutePoint;
	private LatLng _endRoutePoint;
	private Marker _startRouteMarker;
	private Marker _endRouteMarker;
	private ImageButton _runRoute;
	private ImageButton _stopRoute;
	private Chronometer _runningChronometer;
	private Location _location;
	private LatLng _myLocation;
	private LocationClient _locationClient;
	private float _rangeDistance = 0;
	private float _rangeRoutePermited = 5;	
	private boolean _isRunningSet = false;	
	private Double _routeDistance = 0.0;
	private Double _distanceKilometers = 0.0;
	private TextView _distanceTextView;
	private long _timeRouteSeconds;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running_route);
                
        //get parameters from the other activity
        _bundle = getIntent().getExtras();
        _routeName = _bundle.getString("routeName");
        _routeId = _bundle.getLong("routeId"); 
        _logic = new Logic(this);
        _runRoute = (ImageButton)findViewById(R.id.run_route_button); 
        _stopRoute = (ImageButton)findViewById(R.id.stop_route_button);
        _runningChronometer = (Chronometer)findViewById(R.id.running_route_chronometer);
        _distanceTextView = (TextView)findViewById(R.id.show_running_distance);
        _ctx = this;
        
        _runRoute.setOnClickListener(new OnClickListener() {
			
        	float[] distance = new float[1];  
        	double startLatDistance;
            double startwLngDistance;
            double actualLatDistance;
            double actulLngDistance;
        	
			@Override
			public void onClick(View v) {
				
				if(_isRunningSet){
					Toast.makeText(_ctx, "Running the route..", Toast.LENGTH_LONG).show();
				}else{
					startLatDistance = _startRoutePoint.latitude;
					startwLngDistance = _startRoutePoint.longitude;                	
	            	actualLatDistance = _myLocation.latitude;
	            	actulLngDistance = _myLocation.longitude;  
					
					Location.distanceBetween(startLatDistance, startwLngDistance, actualLatDistance, actulLngDistance, distance);				
					_rangeDistance = distance[0];
					
					if(_rangeDistance > _rangeRoutePermited){
						Toast.makeText(_ctx, "No Route near", Toast.LENGTH_LONG).show();
					}else{
						_isRunningSet = true;
						
						//insert first way point to the list
						_runningRouteWayPoints.add(_myLocation);	
						
						//star the chronometer to log the route total time
						_runningChronometer.setBase(SystemClock.elapsedRealtime());
						_runningChronometer.start();
						
						Toast.makeText(_ctx, "Running..", Toast.LENGTH_LONG).show();
					}
				}				
			}
		});
        
        _stopRoute.setOnClickListener(new OnClickListener() {
			
        	float[] distance = new float[1];  
        	double endLatDistance;
            double endLngDistance;
            double actualLatDistance;
            double actulLngDistance;
        	
			@Override
			public void onClick(View v) {
				
				if(_isRunningSet){
					endLatDistance = _endRoutePoint.latitude;
					endLngDistance = _endRoutePoint.longitude;                	
	            	actualLatDistance = _myLocation.latitude;
	            	actulLngDistance = _myLocation.longitude;  
					
					Location.distanceBetween(endLatDistance, endLngDistance, actualLatDistance, actulLngDistance, distance);				
					_rangeDistance = distance[0];
					
					if(_rangeDistance > _rangeRoutePermited){
						Toast.makeText(_ctx, "No Route end near", Toast.LENGTH_LONG).show();
					}else{
						
						_isRunningSet = false;						
						//insert last way point to the list
						_runningRouteWayPoints.add(_myLocation);
						
						//save time route
						long elapsedMillis = SystemClock.elapsedRealtime() - _runningChronometer.getBase();
						_timeRouteSeconds =  ((elapsedMillis / 1000) % 60);
						
						Toast.makeText(_ctx, "Ending route..", Toast.LENGTH_LONG).show();
					}
				}else{
					Toast.makeText(_ctx, "No Route running", Toast.LENGTH_LONG).show();
				}				
			}
		});
        
        
        routeObject = _logic.GetFirstRunRouteById(_routeId);
        
        //set the fastest value of this route
        TextView txtViewFastest= (TextView)findViewById(R.id.fastest_value);
        txtViewFastest.setText(_logic.GetFasterRunDuration(_routeId));
        //set the slowest value of this route
        TextView txtViewSlowest= (TextView)findViewById(R.id.slowest_value);
        txtViewSlowest.setText(_logic.GetSlowerRunDuration(_routeId));
        //set the AVG value of this route
        TextView txtViewAvg = (TextView)findViewById(R.id.avg_value);
        txtViewAvg.setText(_logic.GetAvgRoute(_routeId));
        //set the route name to the activity title
        setTitle(_routeName);
        
        //map initialization  
        _map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map_running)).getMap();               
        _map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        _map.setMyLocationEnabled(true);
        
        //call the function to center the map and draw the route on the map
        centerMapAndShowRoute();
        getUserCurrentLocation();
    }
    
    //function to center the map and show the route
	private void centerMapAndShowRoute(){
		_routeWayPoints = routeObject.getWaypoints();
		_startRoutePoint = new LatLng(_routeWayPoints.get(0).getLat(), _routeWayPoints.get(0).getLng());
		_endRoutePoint = new LatLng(_routeWayPoints.get(_routeWayPoints.size()-1).getLat(), _routeWayPoints.get(_routeWayPoints.size()-1).getLng());
		
	    /*CameraUpdate center = CameraUpdateFactory.newLatLng(_startRoutePoint);
	    CameraUpdate zoom = CameraUpdateFactory.zoomTo(_zoom);	
	    _map.moveCamera(center);
	    _map.animateCamera(zoom);*/
	    
		//show the market at the start point
	    _startRouteMarker = _map.addMarker(new MarkerOptions()
    	.position(_startRoutePoint)
    	.title("Start Point")
    	.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
	    
	    
	    for(WaypointsModel wayPoint: _routeWayPoints){
	    	_routeWayPointsList.add(new LatLng(wayPoint.getLat(), wayPoint.getLng()));
	    }
	    	    
	    //send to draw the route between the start point and the end point
		PolylineOptions po = new PolylineOptions();
		po.addAll(_routeWayPointsList);
		po.width(4).color(Color.BLUE);
		_map.addPolyline(po);	
	     
		//show the market at the end point
		_endRouteMarker = _map.addMarker(new MarkerOptions()
    	.position(_endRoutePoint)
    	.title("End Point")
    	.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
	     
	}
	
	//calls the events to center the map in the current location
    private void getUserCurrentLocation() {           	
    	_locationClient = new LocationClient(this, mConnectionCallbacks, mConnectionFailedListener);
    	_locationClient.connect();
    }
    
    //callback for connection working with the location client
    private ConnectionCallbacks mConnectionCallbacks = new ConnectionCallbacks() {

        @Override
        public void onDisconnected() {
        }

        @Override
        public void onConnected(Bundle arg0) {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setFastestInterval(1000);
            locationRequest.setInterval(1000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            _locationClient.requestLocationUpdates(locationRequest, mLocationListener);            
            
            _location = _locationClient.getLastLocation();

            if (_location != null) {
                _myLocation = new LatLng(_location.getLatitude(),
                        _location.getLongitude());
            }
            _map.animateCamera(CameraUpdateFactory.newLatLngZoom(_myLocation, _zoom));            
        }
    };

    //callback for failures working with the location client
    private OnConnectionFailedListener mConnectionFailedListener = new OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(ConnectionResult arg0) {
            Log.e("tag", "ConnectionFailed");
        }
    };
    
    //location listener to show information when the location changes
    private LocationListener mLocationListener = new LocationListener() {
        
		@Override
            public void onLocationChanged(Location location) {            
                _myLocation = new LatLng(location.getLatitude(), location.getLongitude());  
                                
                float[] distance = new float[1]; 
                double lastLatDistance;
                double lastLngDistance;
                double actualLatDistance;
                double actulLngDistance;
                
                //insert way points to the list while moving on the road 
                if(!_runningRouteWayPoints.isEmpty()){  
                	
                	lastLatDistance = _runningRouteWayPoints.get(_runningRouteWayPoints.size()-1).latitude;
                	lastLngDistance = _runningRouteWayPoints.get(_runningRouteWayPoints.size()-1).longitude;                	
                	actualLatDistance = _myLocation.latitude;
                	actulLngDistance = _myLocation.longitude;                	
                	Location.distanceBetween(lastLatDistance, lastLngDistance, actualLatDistance, actulLngDistance, distance);
                	
                	_routeDistance = _routeDistance + distance[0];                	
                	_distanceKilometers = _routeDistance / 1000;  
                	
                	String showDistance = String.format("%.8f", _distanceKilometers);                	
    				_distanceTextView.setText(showDistance + " km");
                	
    				_runningRouteWayPoints.add(_myLocation);
                	
                }
        }
    };   
	
}