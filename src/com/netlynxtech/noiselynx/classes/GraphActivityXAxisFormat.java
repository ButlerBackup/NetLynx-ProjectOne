package com.netlynxtech.noiselynx.classes;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;

public class GraphActivityXAxisFormat extends Format {

	List<String> xLabels = new ArrayList<String>();

	public GraphActivityXAxisFormat(List<String> data) {
		xLabels = data;
	}

	@Override
	public StringBuffer format(Object arg0, StringBuffer arg1, FieldPosition arg2) {
		int parsedInt = Math.round(Float.parseFloat(arg0.toString()));
		String labelString = xLabels.get(parsedInt);
		arg1.append(labelString);
		return arg1;
	}

	@Override
	public Object parseObject(String arg0, ParsePosition arg1) {
		// TODO Auto-generated method stub
		return xLabels.indexOf(arg0);
	}
}