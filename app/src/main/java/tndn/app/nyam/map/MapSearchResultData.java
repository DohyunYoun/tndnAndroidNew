package tndn.app.nyam.map;

import android.os.Parcel;
import android.os.Parcelable;

public class MapSearchResultData implements Parcelable {

    /*
    "list": [{
            "name": "景福宫(政府首尔厅舍)",
            "kor": "경복궁",
            "cateNm": "公共汽车站",
            "cateNmKor": "마을버스정류장",
            "sido": "首尔特别市",
            "sgg": "钟路区",
            "hemd": "清云孝子洞",
            "x": 126.97936338468706,
            "y": 37.57696090480175,
            "sidoKor": "서울특별시",
            "sggKor": "종로구",
            "hemdKor": "청운효자동",
            "bemdKor": "세종로"
        },
     */
    String name="";
    String name_kor="";
    String cateNm="";
    String cateNmKor="";
    String sido_chn="";
    String sido_kor="";
    String sgg_chn="";
    String sgg_kor="";
    String hemd_chn="";
    String hemd_kor="";
    String bemd_kor="";
    float x;
    float y;

    public MapSearchResultData() {

    }

    protected MapSearchResultData(Parcel in) {
        name = in.readString();
        name_kor = in.readString();
        cateNm = in.readString();
        cateNmKor = in.readString();
        sido_chn = in.readString();
        sido_kor = in.readString();
        sgg_chn = in.readString();
        sgg_kor = in.readString();
        hemd_chn = in.readString();
        hemd_kor = in.readString();
        bemd_kor = in.readString();
        x = in.readFloat();
        y = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeString(name_kor);
        parcel.writeString(cateNm);
        parcel.writeString(cateNmKor);
        parcel.writeString(sido_chn);
        parcel.writeString(sido_kor);
        parcel.writeString(sgg_chn);
        parcel.writeString(sgg_kor);
        parcel.writeString(hemd_chn);
        parcel.writeString(hemd_kor);
        parcel.writeString(bemd_kor);
        parcel.writeFloat(x);
        parcel.writeFloat(y);
    }

    public static final Creator<MapSearchResultData> CREATOR = new Creator<MapSearchResultData>() {
        @Override
        public MapSearchResultData createFromParcel(Parcel in) {
            return new MapSearchResultData(in);
        }

        @Override
        public MapSearchResultData[] newArray(int size) {
            return new MapSearchResultData[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_kor() {
        return name_kor;
    }

    public void setName_kor(String name_kor) {
        this.name_kor = name_kor;
    }

    public String getCateNm() {
        return cateNm;
    }

    public void setCateNm(String cateNm) {
        this.cateNm = cateNm;
    }

    public String getCateNmKor() {
        return cateNmKor;
    }

    public void setCateNmKor(String cateNmKor) {
        this.cateNmKor = cateNmKor;
    }

    public String getSido_chn() {
        return sido_chn;
    }

    public void setSido_chn(String sido_chn) {
        this.sido_chn = sido_chn;
    }

    public String getSido_kor() {
        return sido_kor;
    }

    public void setSido_kor(String sido_kor) {
        this.sido_kor = sido_kor;
    }

    public String getSgg_chn() {
        return sgg_chn;
    }

    public void setSgg_chn(String sgg_chn) {
        this.sgg_chn = sgg_chn;
    }

    public String getSgg_kor() {
        return sgg_kor;
    }

    public void setSgg_kor(String sgg_kor) {
        this.sgg_kor = sgg_kor;
    }

    public String getHemd_chn() {
        return hemd_chn;
    }

    public void setHemd_chn(String hemd_chn) {
        this.hemd_chn = hemd_chn;
    }

    public String getHemd_kor() {
        return hemd_kor;
    }

    public void setHemd_kor(String hemd_kor) {
        this.hemd_kor = hemd_kor;
    }

    public String getBemd_kor() {
        return bemd_kor;
    }

    public void setBemd_kor(String bemd_kor) {
        this.bemd_kor = bemd_kor;
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

    @Override
    public String toString() {
        return "MapSearchResultData{" +
                "name='" + name + '\'' +
                ", name_kor='" + name_kor + '\'' +
                ", cateNm='" + cateNm + '\'' +
                ", cateNmKor='" + cateNmKor + '\'' +
                ", sido_chn='" + sido_chn + '\'' +
                ", sido_kor='" + sido_kor + '\'' +
                ", sgg_chn='" + sgg_chn + '\'' +
                ", sgg_kor='" + sgg_kor + '\'' +
                ", hemd_chn='" + hemd_chn + '\'' +
                ", hemd_kor='" + hemd_kor + '\'' +
                ", bemd_kor='" + bemd_kor + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
