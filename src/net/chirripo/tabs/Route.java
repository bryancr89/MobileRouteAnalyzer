package net.chirripo.tabs;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import net.chirripo.logic.Logic;
import net.chirripo.mobilerouteanalyzer.R;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class Route extends Fragment {
	
	private GoogleMap _map;
	private Location _location;
	private LatLng _myLocation;
	private LocationClient _locationClient;
	private float _zoom = 16;
	private View _rootView;
	private TextView _speedTextView;
	private ImageButton _startButton;
	private ImageButton _stopButton;
	private ImageButton _saveButton;
	private EditText _routeName;
	private Marker _startMarker;
	private Marker _endMarker;
	private List<LatLng> _wayPointsList = new ArrayList<LatLng>();
	private boolean _isStartSet = false;
	private boolean _isStopSet = false;
	private long _routeId = -1;
	
	Logic dbLogic;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        _rootView = inflater.inflate(R.layout.route, container, false);
        _speedTextView = (TextView) _rootView.findViewById(R.id.show_speed);
        _routeName = (EditText) _rootView.findViewById(R.id.route_name);
        
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
					
					//insert last way point to the list
					_wayPointsList.add(_myLocation);
					_isStopSet = true;
					
					//send to draw the route between the start point and the end point
					PolylineOptions po = new PolylineOptions();
					po.addAll(_wayPointsList);
					po.width(4).color(Color.BLUE);
					Polyline line = _map.addPolyline(po);	
					
					dbLogic.UpdateRoute(_routeId, _myLocation.latitude, _myLocation.longitude);					
					Toast.makeText(_rootView.getContext(), "Ending Route...", Toast.LENGTH_SHORT).show();
				}				
			}
		});
        
        //initialization and onClick listener for the save button
        _saveButton = (ImageButton) _rootView.findViewById(R.id.save_button);
        _saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String getRouteName = _routeName.getText().toString();
				
				//validating before save the route
				if(_wayPointsList.isEmpty()){
					Toast.makeText(_rootView.getContext(), "No Route Specified", Toast.LENGTH_SHORT).show();
				}else if(_wayPointsList.size() == 1){
					Toast.makeText(_rootView.getContext(), "Route End Point Required", Toast.LENGTH_SHORT).show();
				}else if(TextUtils.isEmpty(getRouteName.trim())){
					Toast.makeText(_rootView.getContext(), "Route Name Required", Toast.LENGTH_SHORT).show();
				}else{					
					dbLogic.SaveRoute(_routeId, getRouteName);					
					Toast.makeText(_rootView.getContext(), "Route Saved", Toast.LENGTH_LONG).show();
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

        private long mLastEventTime = 0;        

        @Override
            public void onLocationChanged(Location location) {
                double delayBtnEvents = (System.nanoTime()- mLastEventTime )/(1000000000.0);
                mLastEventTime = System.nanoTime();

                //Sampling rate is the frequency at which updates are received
                String samplingRate = (new DecimalFormat("0.0000").format(1/delayBtnEvents));     

                float speed = (float) (location.getSpeed() * 3.6);  // Converting m/s to Km/hr
                _speedTextView.setText(speed + " kmph" + ", " + samplingRate + " Hz"); //Updating UI 
                
                _myLocation = new LatLng(location.getLatitude(),
                        location.getLongitude());
                
                //insert way points to the list while moving on the road 
                if(!_wayPointsList.isEmpty() && _routeId != -1){
                	_wayPointsList.add(_myLocation);
                	dbLogic.AddWayPoint(_routeId, 0, _myLocation.latitude, _myLocation.longitude);
                }
				
        }
    };   
}
