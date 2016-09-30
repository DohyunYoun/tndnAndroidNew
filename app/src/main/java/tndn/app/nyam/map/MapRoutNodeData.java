package tndn.app.nyam.map;

import android.os.Parcel;
import android.os.Parcelable;

public class MapRoutNodeData implements Parcelable {

    /*
    "list": [{
     "x": 126.98428412916579,
          "y": 37.560775919596466,
          "rotation": 0
        },
     */
    float x;
    float y;
    int rotation;

    public MapRoutNodeData() {

    }

    protected MapRoutNodeData(Parcel in) {
        x = in.readFloat();
        y = in.readFloat();
        rotation = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeFloat(x);
        parcel.writeFloat(y);
        parcel.writeInt(rotation);
    }

    public static final Creator<MapRoutNodeData> CREATOR = new Creator<MapRoutNodeData>() {
        @Override
        public MapRoutNodeData createFromParcel(Parcel in) {
            return new MapRoutNodeData(in);
        }

        @Override
        public MapRoutNodeData[] newArray(int size) {
            return new MapRoutNodeData[size];
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
                ", rotation=" + rotation +
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

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }
}
