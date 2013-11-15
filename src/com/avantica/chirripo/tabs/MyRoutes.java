package com.avantica.chirripo.tabs;

import com.avantica.chirripo.list.RouteList;
import com.avantica.chirripo.mobilerouteanalyzer.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("ValidFragment") public class MyRoutes extends Fragment {
	
	ListView list;
    @SuppressLint("ValidFragment") String[] web = {"Google Plus", "Twitter", "Windows", "Bing",
    		"Itunes", "Wordpress", "Drupal"};
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        final View rootView = inflater.inflate(R.layout.my_routes, container, false);
        RouteList adapter = new RouteList(rootView.getContext(), web);
        list=(ListView) rootView.findViewById(R.id.listView1);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(rootView.getContext(), "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();

            }
        });
        return rootView;
    }
}
