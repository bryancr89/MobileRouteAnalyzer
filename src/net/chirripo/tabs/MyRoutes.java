package net.chirripo.tabs;

import java.util.List;

import net.chirripo.mobilerouteanalyzer.R;
import net.chirripo.models.RouteModel;
import net.chirripo.list.RouteList;
import net.chirripo.logic.Logic;
import android.content.Context;
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
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.my_routes, container, false);
        final Context ctx = rootView.getContext();
        _logic = new Logic(ctx);
        final List<RouteModel> routes =_logic.GetListRoutes();
        RouteList adapter = new RouteList(ctx, routes);
        
        list=(ListView) rootView.findViewById(R.id.listView1);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(ctx, "You Clicked at " + routes.get(position).name  + " " + routes.get(position).id , Toast.LENGTH_SHORT).show();

            }
        });
        return rootView;
    }
}
