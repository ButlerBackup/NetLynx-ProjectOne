package dev.blacksheep.netlynx.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.koushikdutta.async.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import dev.blacksheep.netlynx.Consts;
import dev.blacksheep.netlynx.R;
import dev.blacksheep.netlynx.classes.Utils;

public class HistoryAdapter extends BaseAdapter {
	Context context;
	ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;

	public HistoryAdapter(Context context, ArrayList<HashMap<String, String>> data) {
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
		TextView tvTime;
		TextView tvLEQ;
		RelativeLayout rl_history_items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.history_activity_layout_items, null);
			holder = new ViewHolder();
			holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
			holder.tvLEQ = (TextView) convertView.findViewById(R.id.tvLEQ);
			holder.rl_history_items = (RelativeLayout) convertView.findViewById(R.id.rl_history_items);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		HashMap<String, String> item = new HashMap<String, String>();
		item = data.get(position);
		holder.tvTime.setText(new Utils(context).parseDatetime(item.get(Consts.HISTORY_DATETIMESTAMP)));
		holder.tvLEQ.setText(item.get(Consts.MONITORING_LEQ_FIVE_MINUTES));
		if (item.get(Consts.MONITORING_ALERT).equals(Consts.MONITORING_ALERT_YES)) {
			holder.rl_history_items.setBackgroundResource(R.drawable.card_alert);
		} else {
			holder.rl_history_items.setBackgroundResource(R.drawable.card_normal);
			
		}
		return convertView;
	}
}
