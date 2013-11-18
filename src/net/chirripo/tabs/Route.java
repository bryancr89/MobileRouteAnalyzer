package net.chirripo.tabs;

import net.chirripo.mobilerouteanalyzer.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public class Route extends Fragment {
	
	private GoogleMap map;
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.route, container, false);
         
        map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        
        return rootView;
    }
}
