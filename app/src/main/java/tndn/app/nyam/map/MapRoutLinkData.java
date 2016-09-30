package tndn.app.nyam.map;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class MapRoutLinkData implements Parcelable {

    /*
    "list": [{
     "x": 126.98428412916579,
          "y": 37.560775919596466,
          "rotation": 0
        },
     */
    int length;
    ArrayList<MapPointsData> points;

    public MapRoutLinkData() {
        points = new ArrayList<MapPointsData>();
    }

    protected MapRoutLinkData(Parcel in) {
        length = in.readInt();
        points = new ArrayList<MapPointsData>();
        in.readTypedList(points, MapPointsData.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(length);
        parcel.writeTypedList(points);
    }

    public static final Creator<MapRoutLinkData> CREATOR = new Creator<MapRoutLinkData>() {
        @Override
        public MapRoutLinkData createFromParcel(Parcel in) {
            return new MapRoutLinkData(in);
        }

        @Override
        public MapRoutLinkData[] newArray(int size) {
            return new MapRoutLinkData[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public ArrayList<MapPointsData> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<MapPointsData> points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "MapRouteLinkData{" +
                "length=" + length +
                ", points=" + points +
                '}';
    }
}
