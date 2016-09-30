package tndn.app.nyam.map;

import android.os.Parcel;
import android.os.Parcelable;

public class MapPublicPathSubPath01Data implements Parcelable {
    /*
     {
                "trafficType": 3,
                "distance": 161,
                "sectionTime": 2
              },
     */

    //이동 수단 종류 (도보, 버스, 지하철)  1-지하철, 2-버스, 3-도보
    private int trafficType;
    //이동 거리 (m)
    private int distance;
    //이동 소요 시간 (Min.)
    private int sectionTime;

    public MapPublicPathSubPath01Data() {

    }

    protected MapPublicPathSubPath01Data(Parcel in) {
        sectionTime = in.readInt();
        distance = in.readInt();
        sectionTime = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(sectionTime);
        parcel.writeInt(distance);
        parcel.writeInt(sectionTime);
    }

    public static final Creator<MapPublicPathSubPath01Data> CREATOR = new Creator<MapPublicPathSubPath01Data>() {
        @Override
        public MapPublicPathSubPath01Data createFromParcel(Parcel in) {
            return new MapPublicPathSubPath01Data(in);
        }

        @Override
        public MapPublicPathSubPath01Data[] newArray(int size) {
            return new MapPublicPathSubPath01Data[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "MapPublicPathSubPath01Data{" +
                "trafficType=" + trafficType +
                ", distance=" + distance +
                ", sectionTime=" + sectionTime +
                '}';
    }

    public int getTrafficType() {
        return trafficType;
    }

    public void setTrafficType(int trafficType) {
        this.trafficType = trafficType;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getSectionTime() {
        return sectionTime;
    }

    public void setSectionTime(int sectionTime) {
        this.sectionTime = sectionTime;
    }
}
