package com.netlynxtech.noiselynx;

import java.util.ArrayList;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.netlynxtech.noiselynx.classes.DataWrapper;
import com.netlynxtech.noiselynx.classes.GraphActivityXAxisFormat;

public class GraphActivity extends SherlockActivity {

	private XYPlot plot;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DataWrapper dw = (DataWrapper) getIntent().getSerializableExtra("data");
		ArrayList<Number> yAxisData = dw.getYAxis();
		ArrayList<String> xAxisData = dw.getXAxis();
		// fun little snippet that prevents users from taking screenshots
		// on ICS+ devices :-)
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
		setContentView(R.layout.graph_activity);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		// initialize our XYPlot reference:
		plot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
		plot.setDomainLabel("Time");
		plot.getGraphWidget().setDomainValueFormat(new GraphActivityXAxisFormat(xAxisData));

		// Turn the above arrays into XYSeries':
		XYSeries series1 = new SimpleXYSeries(yAxisData, // SimpleXYSeries takes a List so turn our array into a List
				SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means use the element index as the x value
				"Decibels"); // Set the display title of the series

		// Create a formatter to use for drawing a series using LineAndPointRenderer
		// and configure it from xml:
		LineAndPointFormatter series1Format = new LineAndPointFormatter();
		series1Format.setPointLabelFormatter(new PointLabelFormatter());
		series1Format.configure(getApplicationContext(), R.xml.line_point_formatter_with_plf1);

		// add a new series' to the xyplot:
		plot.addSeries(series1, series1Format);

		// reduce the number of range labels
		plot.setTicksPerRangeLabel(3);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
	}
}