package tndn.app.nyam.map;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class MapPublicPathData implements Parcelable {

    //결과 종류   1-지하철, 2-버스, 3-버스+지하철
    int pathType;
    MapPublicPathSubPathData subpath;
    MapPublicPathInfoData info;

    public MapPublicPathData() {

    }

    protected MapPublicPathData(Parcel in) {
        pathType = in.readInt();
        subpath = in.readParcelable(MapPublicPathSubPathData.class.getClassLoader());
        info = in.readParcelable(MapPublicPathInfoData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(pathType);
        parcel.writeParcelable(subpath, flags);
        parcel.writeParcelable(info, flags);
    }

    public static final Creator<MapPublicPathData> CREATOR = new Creator<MapPublicPathData>() {
        @Override
        public MapPublicPathData createFromParcel(Parcel in) {
            return new MapPublicPathData(in);
        }

        @Override
        public MapPublicPathData[] newArray(int size) {
            return new MapPublicPathData[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "MapPublicPathData{" +
                "pathType='" + pathType + '\'' +
                ", subpath=" + subpath +
                ", info=" + info +
                '}';
    }

    public int getPathType() {
        return pathType;
    }

    public void setPathType(int pathType) {
        this.pathType = pathType;
    }

    public MapPublicPathSubPathData getSubpath() {
        return subpath;
    }

    public void setSubpath(MapPublicPathSubPathData subpath) {
        this.subpath = subpath;
    }

    public MapPublicPathInfoData getInfo() {
        return info;
    }

    public void setInfo(MapPublicPathInfoData info) {
        this.info = info;
    }
}
