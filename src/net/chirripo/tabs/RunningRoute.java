package net.chirripo.tabs;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import net.chirripo.logic.Logic;
import net.chirripo.mobilerouteanalyzer.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class RunningRoute extends Fragment {	
	
	private Logic _logic;
	View rootView;
	Context ctx;
	private GoogleMap _map;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.running_route, container, false);
        ctx = rootView.getContext();
        _logic = new Logic(ctx);
        
        //map initialization 
        _map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.running_map))
                .getMap();        
        _map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        _map.setMyLocationEnabled(true);
        
        //Bundle bundle = getIntent().getExtras();
        //get parameters from the other activity
        
        return rootView;
    }
    
}