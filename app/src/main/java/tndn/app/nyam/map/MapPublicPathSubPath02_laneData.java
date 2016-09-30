package tndn.app.nyam.map;

import android.os.Parcel;
import android.os.Parcelable;

public class MapPublicPathSubPath02_laneData implements Parcelable {
/*
            "lane": [
              {
                "busNo": "781-1(성판악.시험장.경마장.광령2리)",
                "type": 21,
                "busID": 2170291
              }
 */

    //버스 번호 (버스인 경우에만 필수)
    private String busNo;
    //버스 타입 (버스인 경우에만 필수)
    private int type;
    //버스 코드 (버스인 경우에만 필수)
    private int busID;

    public MapPublicPathSubPath02_laneData() {

    }

    protected MapPublicPathSubPath02_laneData(Parcel in) {
        busNo = in.readString();
        type = in.readInt();
        busID = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(busNo);
        parcel.writeInt(type);
        parcel.writeInt(busID);
    }

    public static final Creator<MapPublicPathSubPath02_laneData> CREATOR = new Creator<MapPublicPathSubPath02_laneData>() {
        @Override
        public MapPublicPathSubPath02_laneData createFromParcel(Parcel in) {
            return new MapPublicPathSubPath02_laneData(in);
        }

        @Override
        public MapPublicPathSubPath02_laneData[] newArray(int size) {
            return new MapPublicPathSubPath02_laneData[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "MapPublicPathSubPath02_laneData{" +
                "busNo='" + busNo + '\'' +
                ", type=" + type +
                ", busID=" + busID +
                '}';
    }

    public String getBusNo() {
        return busNo;
    }

    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBusID() {
        return busID;
    }

    public void setBusID(int busID) {
        this.busID = busID;
    }
}
