package net.chirripo.tabs;

import java.util.List;

import net.chirripo.mobilerouteanalyzer.R;
import net.chirripo.models.RouteModel;
import net.chirripo.list.RouteList;
import net.chirripo.logic.Logic;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


public class MyRoutes extends Fragment {
	
	ListView list;
	private Logic _logic;
	View rootView;
	Context ctx;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.my_routes, container, false);
        ctx = rootView.getContext();
        _logic = new Logic(ctx);
        final List<RouteModel> routes =_logic.GetListRoutes();
        RouteList adapter = new RouteList(ctx, routes);
        
        list=(ListView) rootView.findViewById(R.id.listView1);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(ctx, "You Clicked at " + routes.get(position).getName()  + " " + routes.get(position).getId() , Toast.LENGTH_SHORT).show();
            	Intent runRoute = new Intent(ctx, RunningRoute.class);
            	runRoute.putExtra("routeName", routes.get(position).getName().toString());
            	runRoute.putExtra("routeId", routes.get(position).getId());
                startActivity(runRoute);	                
            }
        });
        return rootView;
    }
    
}
