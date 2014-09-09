package com.netlynxtech.noiselynx.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netlynxtech.noiselynx.Consts;
import com.netlynxtech.noiselynx.R;
import com.netlynxtech.noiselynx.classes.Utils;

public class AlertsAdapter extends BaseAdapter {
	Context context;
	ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;

	public AlertsAdapter(Context context, ArrayList<HashMap<String, String>> data) {
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
		TextView tvSubject;
		TextView tvMessage;
		TextView tvMessageID;
		RelativeLayout rl_alert_items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.alert_item, parent, false);
			holder = new ViewHolder();
			holder.tvMessageID = (TextView) convertView.findViewById(R.id.tvMessageID);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tvMessageTime);
			holder.tvSubject = (TextView) convertView.findViewById(R.id.tvMessageSubject);
			holder.tvMessage = (TextView) convertView.findViewById(R.id.tvMessageBody);
			holder.rl_alert_items = (RelativeLayout) convertView.findViewById(R.id.rl_alert_items);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		HashMap<String, String> item = new HashMap<String, String>();
		item = data.get(position);
		holder.tvMessageID.setText(item.get(Consts.MESSAGES_MESSAGE_ID));
		holder.tvTime.setText(new Utils(context).parseDatetime(item.get(Consts.MESSAGES_MESSAGE_TIMESTAMP)));
		holder.tvSubject.setText(item.get(Consts.MESSAGES_MESSAGE_SUBJECT));
		holder.tvMessage.setText(Html.fromHtml(item.get(Consts.MESSAGES_MESSAGE_BODY)));
		if (item.get(Consts.MESSAGES_MESSAGE_PRIORITY).equals(Consts.MONITORING_ALERT_YES)) {
			holder.rl_alert_items.setBackgroundResource(R.drawable.card_alert);
		} else {
			holder.rl_alert_items.setBackgroundResource(R.drawable.card);
		}
		return convertView;
	}
}
