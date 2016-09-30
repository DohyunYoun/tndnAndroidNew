package tndn.app.nyam.map;

import android.os.Parcel;
import android.os.Parcelable;

public class MapPublicPathEtcData implements Parcelable {
    /*
       {
            "searchType": 0,
            "startRadius": 700,
            "endRadius": 700,
            "subwayCount": 0,
            "busCount": 4,
            "subwayBusCount": 0,
            "pointDistance": 27798,
            "outTrafficCheck": 0
          }
     */

    //0 : 도시내, 1: 도시간 직통 2: 도시간 환승
    private int searchType;
    //출발지 반경
    private int startRadius;
    //도착지 반경
    private int endRadius;
    //지하철 결과 개수
    private int subwayCount;
    //버스 결과 개수
    private int busCount;
    //“버스+지하철” 결과 개수
    private int subwayBusCount;
    //출발지(SX, SY)와 도착지(EX, EY)의 직선 거리
    private int pointDistance;
    //도시간 직통 탐색 결과 유무 (환승이 아닌 직통입니다!!!) 0-False, 1-True
    private int outTrafficCheck;


    public MapPublicPathEtcData() {

    }

    protected MapPublicPathEtcData(Parcel in) {
        searchType = in.readInt();
        startRadius = in.readInt();
        endRadius = in.readInt();
        subwayCount = in.readInt();
        busCount = in.readInt();
        subwayBusCount = in.readInt();
        pointDistance = in.readInt();
        outTrafficCheck = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(searchType);
        parcel.writeInt(startRadius);
        parcel.writeInt(endRadius);
        parcel.writeInt(subwayCount);
        parcel.writeInt(busCount);
        parcel.writeInt(subwayBusCount);
        parcel.writeInt(pointDistance);
        parcel.writeInt(outTrafficCheck);
    }

    public static final Creator<MapPublicPathEtcData> CREATOR = new Creator<MapPublicPathEtcData>() {
        @Override
        public MapPublicPathEtcData createFromParcel(Parcel in) {
            return new MapPublicPathEtcData(in);
        }

        @Override
        public MapPublicPathEtcData[] newArray(int size) {
            return new MapPublicPathEtcData[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "MapPublicPathEtcData{" +
                "searchType=" + searchType +
                ", startRadius=" + startRadius +
                ", endRadius=" + endRadius +
                ", subwayCount=" + subwayCount +
                ", busCount=" + busCount +
                ", subwayBusCount=" + subwayBusCount +
                ", pointDistance=" + pointDistance +
                ", outTrafficCheck=" + outTrafficCheck +
                '}';
    }

    public int getSearchType() {
        return searchType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }

    public int getStartRadius() {
        return startRadius;
    }

    public void setStartRadius(int startRadius) {
        this.startRadius = startRadius;
    }

    public int getEndRadius() {
        return endRadius;
    }

    public void setEndRadius(int endRadius) {
        this.endRadius = endRadius;
    }

    public int getSubwayCount() {
        return subwayCount;
    }

    public void setSubwayCount(int subwayCount) {
        this.subwayCount = subwayCount;
    }

    public int getBusCount() {
        return busCount;
    }

    public void setBusCount(int busCount) {
        this.busCount = busCount;
    }

    public int getSubwayBusCount() {
        return subwayBusCount;
    }

    public void setSubwayBusCount(int subwayBusCount) {
        this.subwayBusCount = subwayBusCount;
    }

    public int getPointDistance() {
        return pointDistance;
    }

    public void setPointDistance(int pointDistance) {
        this.pointDistance = pointDistance;
    }

    public int getOutTrafficCheck() {
        return outTrafficCheck;
    }

    public void setOutTrafficCheck(int outTrafficCheck) {
        this.outTrafficCheck = outTrafficCheck;
    }
}
