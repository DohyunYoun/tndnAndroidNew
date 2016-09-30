package tndn.app.nyam.map;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class MapPublicPathSubPathData implements Parcelable {

    MapPublicPathSubPath01Data sub01;
    MapPublicPathSubPath02Data sub02;
    MapPublicPathSubPath01Data sub03;

    public MapPublicPathSubPathData() {

    }

    protected MapPublicPathSubPathData(Parcel in) {
        sub01 = in.readParcelable(MapPublicPathSubPath01Data.class.getClassLoader());
        sub02 = in.readParcelable(MapPublicPathSubPath02Data.class.getClassLoader());
        sub03 = in.readParcelable(MapPublicPathSubPath01Data.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(sub01, flags);
        parcel.writeParcelable(sub02, flags);
        parcel.writeParcelable(sub03, flags);
    }

    public static final Creator<MapPublicPathSubPathData> CREATOR = new Creator<MapPublicPathSubPathData>() {
        @Override
        public MapPublicPathSubPathData createFromParcel(Parcel in) {
            return new MapPublicPathSubPathData(in);
        }

        @Override
        public MapPublicPathSubPathData[] newArray(int size) {
            return new MapPublicPathSubPathData[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "MapPublicPathSubPathData{" +
                "sub01=" + sub01 +
                ", sub02=" + sub02 +
                ", sub03=" + sub03 +
                '}';
    }

    public MapPublicPathSubPath01Data getSub01() {
        return sub01;
    }

    public void setSub01(MapPublicPathSubPath01Data sub01) {
        this.sub01 = sub01;
    }

    public MapPublicPathSubPath02Data getSub02() {
        return sub02;
    }

    public void setSub02(MapPublicPathSubPath02Data sub02) {
        this.sub02 = sub02;
    }

    public MapPublicPathSubPath01Data getSub03() {
        return sub03;
    }

    public void setSub03(MapPublicPathSubPath01Data sub03) {
        this.sub03 = sub03;
    }
}
