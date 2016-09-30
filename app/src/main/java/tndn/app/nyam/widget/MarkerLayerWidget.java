package tndn.app.nyam.widget;

import java.util.ArrayList;

import tpmap.android.map.overlay.Marker;
import tpmap.android.map.overlay.MarkersLayer;


class MarkerLayerWidget extends MarkersLayer {


	ArrayList<Marker> list = new ArrayList<Marker>();

	@Override
	protected Marker getMarker(int index) {
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

	public void addItem(Marker mark) {
		list.add(mark);
	}

	public void removeAll() {
		list.clear();
		list = new ArrayList<Marker>();
	}

}
