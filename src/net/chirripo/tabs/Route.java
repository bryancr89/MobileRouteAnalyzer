package net.chirripo.tabs;

import java.text.DecimalFormat;

import net.chirripo.mobilerouteanalyzer.R;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Route extends Fragment {
	
	private GoogleMap map;
	private Location location;
	private LatLng myLocation;
	private LocationClient locationClient;
	private float zoom = 16;
	private View rootView;
	private TextView speedTextView;
	private ImageButton startButton;
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        rootView = inflater.inflate(R.layout.route, container, false);
        speedTextView = (TextView) rootView.findViewById(R.id.show_speed);
        startButton = (ImageButton)rootView.findViewById(R.id.start_button);
        
        //onClick listener for start button
        startButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				map.addMarker(new MarkerOptions()
		    	.position(myLocation)
		    	.title("Start Point"));
			}
		});
         
        map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();        
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);
               
        centerMapOnMyLocation();
        
        return rootView;
    }

    //calls the events to center the map in the current location
    private void centerMapOnMyLocation() {           	
    	locationClient = new LocationClient(rootView.getContext(), mConnectionCallbacks, mConnectionFailedListener);
    	locationClient.connect();
    }
    
    //callback for connection working with the location client
    private ConnectionCallbacks mConnectionCallbacks = new ConnectionCallbacks() {

        @Override
        public void onDisconnected() {
        }

        @Override
        public void onConnected(Bundle arg0) {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setFastestInterval(0);
            locationRequest.setInterval(0).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationClient.requestLocationUpdates(locationRequest, mLocationListener);            
            
            location = locationClient.getLastLocation();

            if (location != null) {
                myLocation = new LatLng(location.getLatitude(),
                        location.getLongitude());
            }
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, zoom));            
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
                speedTextView.setText(speed + " kmph" + ", " + samplingRate + " Hz"); //Updating UI        	
        }
    };   
}
