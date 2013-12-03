package net.chirripo.list;

import java.util.List;
import net.chirripo.mobilerouteanalyzer.R;
import net.chirripo.models.RouteModel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RouteList extends ArrayAdapter<RouteModel> {
	private final Context context;
	private final List<RouteModel> routes;
	public RouteList(Context context, List<RouteModel> routes) {
		super(context, R.layout.route_item, routes);
		this.context = context;
		this.routes = routes;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View rowView= inflater.inflate(R.layout.route_item, null, true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.textView1);
		
		txtTitle.setText(routes.get(position).getName());
		
		return rowView;
	}
}