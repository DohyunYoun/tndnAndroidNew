package tndn.app.nyam.map;

import java.util.ArrayList;

import tpmap.android.map.overlay.Label;
import tpmap.android.map.overlay.LabelsLayer;


public class LabelLayerWidget extends LabelsLayer {

	ArrayList<Label> list = new ArrayList<Label>();

	@Override
	protected Label getLabel(int index) {
		// TODO Auto-generated method stub
		if (index >= list.size())
			return null;
		return list.get(index);
	}

	@Override
	protected int size() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public void addItem(Label label) {
		list.add(label);
	}

	public void removeAll() {
		list.clear();
		list = new ArrayList<Label>();
	}

}


