package net.chirripo.mobilerouteanalyzer;

import net.chirripo.tabs.MyRoutes;
import net.chirripo.tabs.Route;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;


public class MainActivity extends Activity {
		 
	private ImageButton _myRoutesButton;
	private ImageButton _addRouteButton;
	private Context _ctx;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		
		_myRoutesButton = (ImageButton)findViewById(R.id.my_routes_button);
		_addRouteButton = (ImageButton)findViewById(R.id.add_route_button);
		_ctx = this;
		
		//on click event my routes button
		_myRoutesButton.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(_ctx, MyRoutes.class);				 
				startActivity(intent);						
			}
		});
		
		//on click event add route button
		_addRouteButton.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				 Intent intent = new Intent(_ctx, Route.class);				 
				 startActivity(intent);					
			}
		});
		
	}    
}
