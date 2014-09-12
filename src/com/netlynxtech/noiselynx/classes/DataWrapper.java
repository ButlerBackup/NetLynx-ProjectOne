package com.netlynxtech.noiselynx.classes;

import java.io.Serializable;
import java.util.ArrayList;

public class DataWrapper implements Serializable {

	private ArrayList<Number> yAxis;
	private ArrayList<String> xAxis;

	public DataWrapper(ArrayList<Number> yAxis, ArrayList<String> xAxis) {
		this.yAxis = yAxis;
		this.xAxis = xAxis;
	}

	public ArrayList<Number> getYAxis() {
		return this.yAxis;
	}

	public ArrayList<String> getXAxis() {
		return this.xAxis;
	}
}
