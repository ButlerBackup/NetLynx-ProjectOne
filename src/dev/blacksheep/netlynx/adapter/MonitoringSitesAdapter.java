package dev.blacksheep.netlynx.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import dev.blacksheep.netlynx.Consts;
import dev.blacksheep.netlynx.R;
import dev.blacksheep.netlynx.SiteLocationActivity;
import dev.blacksheep.netlynx.classes.Utils;

public class MonitoringSitesAdapter extends BaseAdapter {
	Context context;
	ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;

	public MonitoringSitesAdapter(Context context, ArrayList<HashMap<String, String>> data) {
		this.context = context;
		this.data = data;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		TextView tvID;
		TextView tvTime;
		TextView tvDevice;
		TextView tvCurrentDBA;
		TextView tvLEQ1hour;
		TextView tvLEQ12hour;
		TextView tvLocationLat;
		TextView tvLocationLong;
		View view1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.monitoring_sites_item, null);
			holder = new ViewHolder();
			holder.tvID = (TextView) convertView.findViewById(R.id.tvID);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
			holder.tvDevice = (TextView) convertView.findViewById(R.id.tvDevice);
			holder.tvCurrentDBA = (TextView) convertView.findViewById(R.id.tvCurrentDBA);
			holder.tvLEQ1hour = (TextView) convertView.findViewById(R.id.tvLEQ1hour);
			holder.tvLEQ12hour = (TextView) convertView.findViewById(R.id.tvLEQ12hour);
			holder.tvLocationLat = (TextView) convertView.findViewById(R.id.tvLocationLat);
			holder.tvLocationLong = (TextView) convertView.findViewById(R.id.tvLocationLong);
			holder.view1 = (View) convertView.findViewById(R.id.view1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		HashMap<String, String> item = new HashMap<String, String>();
		item = data.get(position);
		holder.tvID.setText(item.get(Consts.MONITORING_DEVICE_ID));
		holder.tvTime.setText(new Utils(context).parseDatetime(item.get(Consts.MONITORING_DATE_TIME)));
		holder.tvDevice.setText(item.get(Consts.MONITORING_LOCATION));
		holder.tvCurrentDBA.setText(item.get(Consts.MONITORING_LEQ_FIVE_MINUTES));
		holder.tvLEQ1hour.setText(item.get(Consts.MONITORING_LEQ_ONE_HOUR));
		holder.tvLEQ12hour.setText(item.get(Consts.MONITORING_LEQ_TWELVE_HOUR));
		holder.tvLocationLat.setText(item.get(Consts.MONITORING_LOCATION_LAT));
		holder.tvLocationLong.setText(item.get(Consts.MONITORING_LOCATION_LONG));
		// Log.e("ALERT FOR " + item.get(Consts.MONITORING_LOCATION), item.get(Consts.MONITORING_ALERT));
		if (item.get(Consts.MONITORING_ALERT).equals(Consts.MONITORING_ALERT_YES)) {
			holder.view1.setBackgroundColor(Color.parseColor("#FF0000"));
		}

		holder.tvDevice.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, SiteLocationActivity.class);
				i.putExtra(Consts.MONITORING_DEVICE_ID, holder.tvID.getText().toString());
				i.putExtra(Consts.MONITORING_DATE_TIME, holder.tvTime.getText().toString());
				i.putExtra(Consts.MONITORING_LOCATION, holder.tvDevice.getText().toString());
				i.putExtra(Consts.MONITORING_LEQ_FIVE_MINUTES, holder.tvCurrentDBA.getText().toString());
				i.putExtra(Consts.MONITORING_LEQ_ONE_HOUR, holder.tvLEQ1hour.getText().toString());
				i.putExtra(Consts.MONITORING_LEQ_TWELVE_HOUR, holder.tvLEQ12hour.getText().toString());
				i.putExtra(Consts.MONITORING_LOCATION_LAT, holder.tvLocationLat.getText().toString());
				i.putExtra(Consts.MONITORING_LOCATION_LONG, holder.tvLocationLong.getText().toString());
				context.startActivity(i);
			}
		});
		return convertView;
	}
}
