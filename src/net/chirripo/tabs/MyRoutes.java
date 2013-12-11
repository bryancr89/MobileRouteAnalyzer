package net.chirripo.tabs;

import java.util.List;

import net.chirripo.list.RouteList;
import net.chirripo.logic.Logic;
import net.chirripo.mobilerouteanalyzer.R;
import net.chirripo.models.RouteModel;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class MyRoutes extends Activity {
	
	ListView list;
	private Logic _logic;	
	Context ctx;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.my_routes);  		
        ctx = this;
        _logic = new Logic(ctx);
        
        final List<RouteModel> routes =_logic.GetListRoutes();
        RouteList adapter = new RouteList(ctx, routes);
        
        list=(ListView)findViewById(R.id.listView1);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {                
            	Intent runRoute = new Intent(ctx, RunningRoute.class);
            	runRoute.putExtra("routeName", routes.get(position).getName().toString());
            	runRoute.putExtra("routeId", routes.get(position).getId()); 
            	startActivity(runRoute);
            }
        });        
    }
    
}
