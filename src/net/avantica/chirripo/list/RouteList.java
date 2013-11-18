package net.avantica.chirripo.list;

import com.avantica.chirripo.mobilerouteanalyzer.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RouteList extends ArrayAdapter<String> {
	private final Context context;
	private final String[] web;
	public RouteList(Context context, String[] web) {
		super(context, R.layout.route_item, web);
		this.context = context;
		this.web = web;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View rowView= inflater.inflate(R.layout.route_item, null, true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.textView1);
		
		txtTitle.setText(web[position]);
		
		return rowView;
	}
}
