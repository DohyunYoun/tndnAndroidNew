package tndn.app.nyam.map;

import android.os.Parcel;
import android.os.Parcelable;

public class MapPublicPathSubPath02Data implements Parcelable {
/*
           {
            "trafficType": 2,
            "lane": [
              {
                "busNo": "781-1(성판악.시험장.경마장.광령2리)",
                "type": 21,
                "busID": 2170291
              }
            ],
            "stationCount": 32,
            "distance": 38089,
            "sectionTime": 66,
            "startX": 126.5294392,
            "startY": 33.4998582,
            "startID": 800166,
            "startName": "제주시청(시외)",
            "startARSID": "",
            "endARSID": "",
            "endX": 126.5682936,
            "endY": 33.2486951,
            "endID": 801876,
            "endName": "동문로터리"
          },
 */

    //이동 수단 종류 (도보, 버스, 지하철) 1-지하철, 2-버스, 3-도보
    private int trafficType;
    //이동 정거장 수
    private int stationCount;
    //이동 거리 (m)
    private int distance;
    //이동 소요 시간 (Min.)
    private int sectionTime;
    //승차 정류장/역 X 좌표
    private float startX;
    //승차 정류장/역 Y 좌표
    private float startY;
    //출발 정류장/역 코드
    private int startID;
    //승차 정류장/역명
    private String startName;
    //하차 정류장/역 X 좌표
    private float endX;
    //하차 정류장/역 Y 좌표
    private float endY;
    //도착 정류장/역 코드
    private int endID;
    //하차 정류장/역명
    private String endName;
    private MapPublicPathSubPath02_laneData lane;

    public MapPublicPathSubPath02Data() {

    }

    protected MapPublicPathSubPath02Data(Parcel in) {
        trafficType = in.readInt();
        stationCount = in.readInt();
        distance = in.readInt();
        sectionTime = in.readInt();
        startX = in.readFloat();
        startY = in.readFloat();
        startID = in.readInt();
        startName = in.readString();
        endX = in.readFloat();
        endY = in.readFloat();
        endID = in.readInt();
        endName = in.readString();
        lane = in.readParcelable(MapPublicPathSubPath02_laneData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(trafficType);
        parcel.writeInt(stationCount);
        parcel.writeInt(distance);
        parcel.writeInt(sectionTime);
        parcel.writeFloat(startX);
        parcel.writeFloat(startY);
        parcel.writeInt(startID);
        parcel.writeString(startName);
        parcel.writeFloat(endX);
        parcel.writeFloat(endY);
        parcel.writeInt(endID);
        parcel.writeString(endName);
        parcel.writeParcelable(lane, flags);
    }

    public static final Creator<MapPublicPathSubPath02Data> CREATOR = new Creator<MapPublicPathSubPath02Data>() {
        @Override
        public MapPublicPathSubPath02Data createFromParcel(Parcel in) {
            return new MapPublicPathSubPath02Data(in);
        }

        @Override
        public MapPublicPathSubPath02Data[] newArray(int size) {
            return new MapPublicPathSubPath02Data[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "MapPublicPathSubPath02Data{" +
                "trafficType=" + trafficType +
                ", stationCount=" + stationCount +
                ", distance=" + distance +
                ", sectionTime=" + sectionTime +
                ", startX=" + startX +
                ", startY=" + startY +
                ", startID=" + startID +
                ", startName='" + startName + '\'' +
                ", endX=" + endX +
                ", endY=" + endY +
                ", endID=" + endID +
                ", endName='" + endName + '\'' +
                ", lane=" + lane +
                '}';
    }

    public int getTrafficType() {
        return trafficType;
    }

    public void setTrafficType(int trafficType) {
        this.trafficType = trafficType;
    }

    public int getStationCount() {
        return stationCount;
    }

    public void setStationCount(int stationCount) {
        this.stationCount = stationCount;
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

    public float getStartX() {
        return startX;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public float getStartY() {
        return startY;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }

    public int getStartID() {
        return startID;
    }

    public void setStartID(int startID) {
        this.startID = startID;
    }

    public String getStartName() {
        return startName;
    }

    public void setStartName(String startName) {
        this.startName = startName;
    }


    public float getEndX() {
        return endX;
    }

    public void setEndX(float endX) {
        this.endX = endX;
    }

    public float getEndY() {
        return endY;
    }

    public void setEndY(float endY) {
        this.endY = endY;
    }

    public int getEndID() {
        return endID;
    }

    public void setEndID(int endID) {
        this.endID = endID;
    }

    public String getEndName() {
        return endName;
    }

    public void setEndName(String endName) {
        this.endName = endName;
    }

    public MapPublicPathSubPath02_laneData getLane() {
        return lane;
    }

    public void setLane(MapPublicPathSubPath02_laneData lane) {
        this.lane = lane;
    }
}
