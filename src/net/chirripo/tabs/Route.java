package net.chirripo.tabs;

import java.util.ArrayList;
import java.util.List;

import net.chirripo.logic.Logic;
import net.chirripo.mobilerouteanalyzer.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.EditText;
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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class Route extends Fragment {
	
	private GoogleMap _map;
	private Location _location;
	private LatLng _myLocation;
	private LocationClient _locationClient;
	private float _zoom = 16;
	private View _rootView;
	private TextView _distanceTextView;
	private ImageButton _startButton;
	private ImageButton _stopButton;	
	private EditText _routeName;
	private Marker _startMarker;
	private Marker _endMarker;
	private List<LatLng> _wayPointsList = new ArrayList<LatLng>();
	private boolean _isStartSet = false;
	private boolean _isStopSet = false;
	private long _routeId = -1;
	private Double _routeDistance = 0.0;
	private Double _distanceKilometers = 0.0;
	private Chronometer _routeChronometer;
	
	Logic dbLogic;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        _rootView = inflater.inflate(R.layout.route, container, false);
        _distanceTextView = (TextView) _rootView.findViewById(R.id.show_distance);
        _routeChronometer = (Chronometer) _rootView.findViewById(R.id.time_chronometer);
        
        dbLogic = new Logic(_rootView.getContext());
        
        //initialization and onClick listener for start button
        _startButton = (ImageButton)_rootView.findViewById(R.id.start_button);
        _startButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(!_isStartSet){
					if(_startMarker != null){
						_startMarker.remove();
					}
					
					_startMarker = _map.addMarker(new MarkerOptions()
			    	.position(_myLocation)
			    	.title("Start Point")
			    	.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
					
					//star the chronometer to log the route total time
					_routeChronometer.setBase(SystemClock.elapsedRealtime());
					_routeChronometer.start();
					
					//insert first way point to the list
					_wayPointsList.add(_myLocation);
					_isStartSet = true;
					
					_routeId = dbLogic.AddRoute(_myLocation.latitude, _myLocation.longitude);						
					Toast.makeText(_rootView.getContext(), "Starting Route...", Toast.LENGTH_SHORT).show();	
					
				}else{
					Toast.makeText(_rootView.getContext(), "Calculating Route...", Toast.LENGTH_SHORT).show();
				}				
			}
		});
        
        //initialization and onClick listener for end button
        _stopButton = (ImageButton)_rootView.findViewById(R.id.stop_button);
        _stopButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(_wayPointsList.isEmpty()){
					Toast.makeText(_rootView.getContext(), "Route Start Point Required", Toast.LENGTH_SHORT).show();
				}else if(_isStopSet){
					Toast.makeText(_rootView.getContext(), "Route Calculated", Toast.LENGTH_SHORT).show();
				}else{
					if(_endMarker != null){
						_endMarker.remove();
					}
					
					_endMarker = _map.addMarker(new MarkerOptions()
			    	.position(_myLocation)
			    	.title("End Point")
			    	.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
					
					//stop the chronometer
					_routeChronometer.stop();
					
					//insert last way point to the list
					_wayPointsList.add(_myLocation);
					_isStopSet = true;
					
					//send to draw the route between the start point and the end point
					PolylineOptions po = new PolylineOptions();
					po.addAll(_wayPointsList);
					po.width(4).color(Color.BLUE);
					_map.addPolyline(po);	
					
					dbLogic.UpdateRoute(_routeId, _myLocation.latitude, _myLocation.longitude);
					
					//create and show alert dialog for save the route
					createSaveRouteDialog();
				}				
			}
		});
        
        //map initialization 
        _map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();        
        _map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        _map.setMyLocationEnabled(true);
               
        //call the function to relocated the user in its current position
        centerMapOnMyLocation();
             
        return _rootView;
    }

    //calls the events to center the map in the current location
    private void centerMapOnMyLocation() {           	
    	_locationClient = new LocationClient(_rootView.getContext(), mConnectionCallbacks, mConnectionFailedListener);
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
            locationRequest.setFastestInterval(10000);
            locationRequest.setInterval(10000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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
                if(!_wayPointsList.isEmpty() && _routeId != -1){  
                	
                	lastLatDistance = _wayPointsList.get(_wayPointsList.size()-1).latitude;
                	lastLngDistance = _wayPointsList.get(_wayPointsList.size()-1).longitude;                	
                	actualLatDistance = _myLocation.latitude;
                	actulLngDistance = _myLocation.longitude;                	
                	Location.distanceBetween(lastLatDistance, lastLngDistance, actualLatDistance, actulLngDistance, distance);
                	
                	_routeDistance = _routeDistance + distance[0];
                	
                	_distanceKilometers = _routeDistance / 1000;     
                	
                	String showDistance = String.format("%.8f", _distanceKilometers);
                	
    				_distanceTextView.setText(showDistance + " km");
                	
                	_wayPointsList.add(_myLocation);
                	dbLogic.AddWayPoint(_routeId, 0, _myLocation.latitude, _myLocation.longitude);
                }
				
        }
    };   
    
    //function to create a alert dialog to input the route name and then save the route
    private void createSaveRouteDialog(){
    	// get save_route view
    	LayoutInflater layoutInflater = LayoutInflater.from(_rootView.getContext());
    	View promptView = layoutInflater.inflate(R.layout.save_route, null);
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_rootView.getContext());
    	
    	_routeName = (EditText) promptView.findViewById(R.id.route_name);
    	
    	// set save_route.xml to be the layout file of the alertdialog builder
    	alertDialogBuilder.setView(promptView);    	    	
    	
    	// setup a dialog window
    	alertDialogBuilder
			.setTitle("Save Route")
			.setPositiveButton("Save", new DialogInterface.OnClickListener() {
	    		public void onClick(DialogInterface dialog, int id) {
	    			//this method is going to be override later    			
	    		}
	    	}).setNegativeButton("Cancel",new DialogInterface.OnClickListener() {			    	
	    		public void onClick(DialogInterface dialog, int id) {	    				    		
	    			dbLogic.DeleteRoute(_routeId);	    			
	    			setDefaultValues();	    			
	    			dialog.cancel();
	    		}
	    	});
    	
    	// create the save route alert dialog
    	final AlertDialog alertSaveRoute = alertDialogBuilder.create();
    	alertSaveRoute.show();
    	
    	alertSaveRoute.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				String getRouteName = _routeName.getText().toString();
	    		
    			//validating before save the route
				if(TextUtils.isEmpty(getRouteName.trim())){
					Toast.makeText(_rootView.getContext(), "Route Name Required", Toast.LENGTH_SHORT).show();
				}else{					
					dbLogic.SaveRoute(_routeId, getRouteName);						
					setDefaultValues();					
					Toast.makeText(_rootView.getContext(), "Route Saved", Toast.LENGTH_LONG).show();
					alertSaveRoute.dismiss();
				}
				
			}
		});
    }
    
    //function to set the default values of some variables
    private void setDefaultValues(){
    	_isStartSet = false;
		_isStopSet = false;
		_wayPointsList.clear();
		_map.clear();
		_routeId = -1;
		_distanceTextView.setText("0.00 km");
		_routeChronometer.setBase(SystemClock.elapsedRealtime());
    }
}
