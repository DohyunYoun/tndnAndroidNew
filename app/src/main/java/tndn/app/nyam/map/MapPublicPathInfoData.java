package tndn.app.nyam.map;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class MapPublicPathInfoData implements Parcelable {
/*
       "info": {
          "mapObj": "2170291:1:1:33",
          "payment": 3300,
          "busTransitCount": 1,
          "subwayTransitCount": 0,
          "busStationCount": 32,
          "subwayStationCount": 0,
          "totalStationCount": 32,
          "totalTime": 74,
          "totalWalk": 531,
          "trafficDistance": 38089,
          "totalDistance": 38620,
          "firstStartStation": "제주시청(시외)",
          "lastEndStation": "동문로터리",
          "totalWalkTime": -1
        }
 */

    private String mapObj;
    private int payment;
    private int busTransitCount;
    private int subwayTransitCount;
    private int busStationCount;
    private int subwayStationCount;
    private int totalStationCount;
    private int totalTime;
    private int totalWalk;
    private int trafficDistance;
    private int totalDistance;
    private String firstStartStation;
    private String lastEndStation;
    private String totalWalkTime;

    public MapPublicPathInfoData() {

    }

    protected MapPublicPathInfoData(Parcel in) {
        mapObj = in.readString();
        payment = in.readInt();
        busTransitCount = in.readInt();
        subwayTransitCount = in.readInt();
        busStationCount = in.readInt();
        subwayStationCount = in.readInt();
        totalStationCount = in.readInt();
        totalTime = in.readInt();
        totalWalk = in.readInt();
        trafficDistance = in.readInt();
        totalDistance = in.readInt();
        firstStartStation = in.readString();
        lastEndStation = in.readString();
        totalWalkTime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mapObj);
        parcel.writeInt(payment);
        parcel.writeInt(busTransitCount);
        parcel.writeInt(subwayTransitCount);
        parcel.writeInt(busStationCount);
        parcel.writeInt(subwayStationCount);
        parcel.writeInt(totalStationCount);
        parcel.writeInt(totalTime);
        parcel.writeInt(totalWalk);
        parcel.writeInt(trafficDistance);
        parcel.writeInt(totalDistance);
        parcel.writeString(firstStartStation);
        parcel.writeString(lastEndStation);
        parcel.writeString(totalWalkTime);
    }

    public static final Creator<MapPublicPathInfoData> CREATOR = new Creator<MapPublicPathInfoData>() {
        @Override
        public MapPublicPathInfoData createFromParcel(Parcel in) {
            return new MapPublicPathInfoData(in);
        }

        @Override
        public MapPublicPathInfoData[] newArray(int size) {
            return new MapPublicPathInfoData[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "MapPublicPathInfoData{" +
                "mapObj='" + mapObj + '\'' +
                ", payment=" + payment +
                ", busTransitCount=" + busTransitCount +
                ", subwayTransitCount=" + subwayTransitCount +
                ", busStationCount=" + busStationCount +
                ", subwayStationCount=" + subwayStationCount +
                ", totalStationCount=" + totalStationCount +
                ", totalTime=" + totalTime +
                ", totalWalk=" + totalWalk +
                ", trafficDistance=" + trafficDistance +
                ", totalDistance=" + totalDistance +
                ", firstStartStation='" + firstStartStation + '\'' +
                ", lastEndStation='" + lastEndStation + '\'' +
                ", totalWalkTime='" + totalWalkTime + '\'' +
                '}';
    }

    public String getMapObj() {
        return mapObj;
    }

    public void setMapObj(String mapObj) {
        this.mapObj = mapObj;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public int getBusTransitCount() {
        return busTransitCount;
    }

    public void setBusTransitCount(int busTransitCount) {
        this.busTransitCount = busTransitCount;
    }

    public int getSubwayTransitCount() {
        return subwayTransitCount;
    }

    public void setSubwayTransitCount(int subwayTransitCount) {
        this.subwayTransitCount = subwayTransitCount;
    }

    public int getBusStationCount() {
        return busStationCount;
    }

    public void setBusStationCount(int busStationCount) {
        this.busStationCount = busStationCount;
    }

    public int getSubwayStationCount() {
        return subwayStationCount;
    }

    public void setSubwayStationCount(int subwayStationCount) {
        this.subwayStationCount = subwayStationCount;
    }

    public int getTotalStationCount() {
        return totalStationCount;
    }

    public void setTotalStationCount(int totalStationCount) {
        this.totalStationCount = totalStationCount;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getTotalWalk() {
        return totalWalk;
    }

    public void setTotalWalk(int totalWalk) {
        this.totalWalk = totalWalk;
    }

    public int getTrafficDistance() {
        return trafficDistance;
    }

    public void setTrafficDistance(int trafficDistance) {
        this.trafficDistance = trafficDistance;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(int totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getFirstStartStation() {
        return firstStartStation;
    }

    public void setFirstStartStation(String firstStartStation) {
        this.firstStartStation = firstStartStation;
    }

    public String getLastEndStation() {
        return lastEndStation;
    }

    public void setLastEndStation(String lastEndStation) {
        this.lastEndStation = lastEndStation;
    }

    public String getTotalWalkTime() {
        return totalWalkTime;
    }

    public void setTotalWalkTime(String totalWalkTime) {
        this.totalWalkTime = totalWalkTime;
    }
}
