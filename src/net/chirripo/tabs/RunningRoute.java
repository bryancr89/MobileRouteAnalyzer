package net.chirripo.tabs;


import net.chirripo.logic.Logic;
import net.chirripo.mobilerouteanalyzer.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;


public class RunningRoute extends Activity {	
	
	private Logic _logic;
	View rootView;
	Context ctx;
	private GoogleMap _map;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running_route);
        setTitle("My new title");
        _map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map22)).getMap();
        /*rootView = findViewById(R.layout.running_route);
        ctx = rootView.getContext();
        _logic = new Logic(ctx);
        MapView mapView = (MapView) findViewById(R.id.mapviewtest);

        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        //map initialization 
        _map = mapView.getMap();    
        _map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        _map.setMyLocationEnabled(true);*/
        
        //Bundle bundle = getIntent().getExtras();
        //get parameters from the other activity
        
    }
    
}