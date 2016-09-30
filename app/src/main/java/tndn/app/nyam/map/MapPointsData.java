package tndn.app.nyam.map;

import android.os.Parcel;
import android.os.Parcelable;

public class MapPointsData implements Parcelable {

    /*
    "list": [{
     "x": 126.98428412916579,
          "y": 37.560775919596466,
          "rotation": 0
        },
     */
    float x;
    float y;

    public MapPointsData() {

    }

    protected MapPointsData(Parcel in) {
        x = in.readFloat();
        y = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeFloat(x);
        parcel.writeFloat(y);
    }

    public static final Creator<MapPointsData> CREATOR = new Creator<MapPointsData>() {
        @Override
        public MapPointsData createFromParcel(Parcel in) {
            return new MapPointsData(in);
        }

        @Override
        public MapPointsData[] newArray(int size) {
            return new MapPointsData[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "MapRouteNodeData{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

}
