package com.netlynxtech.noiselynx.classes;

import java.io.Serializable;
import java.util.ArrayList;

public class DataWrapper implements Serializable {

	private ArrayList<Number> parliaments;

	public DataWrapper(ArrayList<Number> data) {
		this.parliaments = data;
	}

	public ArrayList<Number> getParliaments() {
		return this.parliaments;
	}

}
