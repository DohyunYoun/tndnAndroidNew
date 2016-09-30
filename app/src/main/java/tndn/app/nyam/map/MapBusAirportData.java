package tndn.app.nyam.map;

import android.os.Parcel;
import android.os.Parcelable;

public class MapBusAirportData implements Parcelable {
    /*
      "arrvvhid": 7997046,
                "calcdate": "2016-09-13T15:53:23+09:00",
                "leftstation": 34,
                "predicttravtm": 39,
                "routeid": 405037009,
                "stationord": 38,
                "updndir": 0
     */
    private String leftstation;
    private int predicttravtm;
    private String routeid;
    private String stationord;
    private String updndir;

    public MapBusAirportData() {

    }

    protected MapBusAirportData(Parcel in) {
        leftstation = in.readString();
        predicttravtm = in.readInt();
        routeid = in.readString();
        stationord = in.readString();
        updndir = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(leftstation);
        parcel.writeInt(predicttravtm);
        parcel.writeString(routeid);
        parcel.writeString(stationord);
        parcel.writeString(updndir);
    }

    public static final Creator<MapBusAirportData> CREATOR = new Creator<MapBusAirportData>() {
        @Override
        public MapBusAirportData createFromParcel(Parcel in) {
            return new MapBusAirportData(in);
        }

        @Override
        public MapBusAirportData[] newArray(int size) {
            return new MapBusAirportData[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "MapBusAirportData{" +
                "leftstation='" + leftstation + '\'' +
                ", predicttravtm='" + predicttravtm + '\'' +
                ", routeid='" + routeid + '\'' +
                ", stationord='" + stationord + '\'' +
                ", updndir='" + updndir + '\'' +
                '}';
    }

    public String getUpdndir() {
        return updndir;
    }

    public void setUpdndir(String updndir) {
        this.updndir = updndir;
    }

    public String getLeftstation() {
        return leftstation;
    }

    public void setLeftstation(String leftstation) {
        this.leftstation = leftstation;
    }

    public int getPredicttravtm() {
        return predicttravtm;
    }

    public void setPredicttravtm(int predicttravtm) {
        this.predicttravtm = predicttravtm;
    }

    public String getRouteid() {
        return routeid;
    }

    public String getBusNum() {
        return routeidTo(routeid, updndir, "busnum");
    }

    public String getBusGoto() {
        return routeidTo(routeid, updndir, "busgoto");
    }

    public void setRouteid(String routeid) {
        this.routeid = routeid;
    }

    public String getStationord() {
        return stationord;
    }

    public void setStationord(String stationord) {
        this.stationord = stationord;
    }


    /**
     * 제주국제공항입구[동]	월성 마을 방향	405000646
     * 702	사계경유 방면	405700010
     * 960	상두거리-제주 방면	405900007
     * 702	덕수경유 방면	405700010
     * 87	제주대 방면	405087007
     * 37	제주대 방면	405037009
     * 38	함덕 방면	405038004
     * 200	국제공항입구 방면	405200006
     * 제주국제공항(종점)	종점 방향	405001896
     * 600	제주 방면	405600002
     * 제주국제공항[북]	월성 마을 방향	405000663
     * 500	제주대 방면	405500007
     * 95	국제부두 방면	405095005
     * 70	도립미술관 방면	405070002
     * 100	삼양1동 방면	405100003
     * 90	국제부두 방면	405090001
     * 38	함덕 방면	405038004
     * 제주국제공항	다호 마을 방향	405001478
     * 500	한라대 방면	405500001
     * 37	번대 방면	405037002
     * 200	관광대 방면	405200008
     * 70	도립미술관 방면	405070002
     * 38	하귀(수산) 방면	405038001
     * 95	한라수목원 방면	405095002
     * 200	수산리 방면	405200009
     * 37	수산 방면	405037001
     * 36	외도부영 방면	405036001
     * 200	백록초 방면	405200013
     * 제주국제공항	제주 썬호텔[북] 방향	405001759
     * 제주국제공항	제주시외버스터미널(종점) 방향	405001897
     */

    public String routeidTo(String routeid, String updndir, String what) {
        String busnum = "";
        String busgoto = "";
        switch (routeid) {

            //제주 국제공항입구[동]
            case "405700010":
                if (updndir.equals("1")) {
                    //true
                    busnum = "702";
                    busgoto = "사계경유 방면";
                } else {
                    //false
                    busnum = "702";
                    busgoto = "덕수경유 방면";
                }
                break;

            case "405200017":
                busnum = "200";
                busgoto = "국제공항입구 방면";
                break;
            case "405090002":
                busnum = "90";
                busgoto = "한라수목원 방면";
                break;
            case "405700013":
                busnum = "702";
                busgoto = "사계경유 방면";
                break;
            case "405900007":
                busnum = "906";
                busgoto = "상두거리-제주 방면";
                break;
            case "405900009":
                busnum = "906";
                busgoto = "상두거리-제주 방면";
                break;
            case "405087007":
                busnum = "87";
                busgoto = "제주대 방면";
                break;
            case "405037009":
                busnum = "37";
                busgoto = "제주대 방면";
                break;
            case "405037010":
                busnum = "37";
                busgoto = "제주대 방면";
                break;
            case "405200006":
                busnum = "200";
                busgoto = "국제공항입구 방면";
                break;
            case "405200027":
                busnum = "200";
                busgoto = "국제공항입구 방면";
                break;
            case "405087005":
                busnum = "87";
                busgoto = "제주대 방면";
                break;

            //제주국제공항(종점)
            case "405600002":
                busnum = "600";
                busgoto = "제주 방면";
                break;

            //제주국제공항[북]
            case "405036003":
                busnum = "39";
                busgoto = "월평 방면";
                break;
            case "405036004":
                busnum = "36";
                busgoto = "월평 방면";
                break;
            case "405500007":
                busnum = "500";
                busgoto = "제주대 방면";
                break;
            case "405100004":
                busnum = "100";
                busgoto = "삼양3동 방면";
                break;
            case "405070001":
                busnum = "70";
                busgoto = "국제부두 방면";
                break;
            case "405095001":
                busnum = "95";
                busgoto = "국제부두 방면";
                break;
            case "405095005":
                busnum = "95";
                busgoto = "국제부두 방면";
                break;
            case "405070002":
                busnum = "70";
                busgoto = "도립미술관 방면";
                break;
            case "405100003":
                busnum = "100";
                busgoto = "삼양1동 방면";
                break;
            case "405090001":
                busnum = "90";
                busgoto = "국제부두 방면";
                break;
            case "405038004":
                busnum = "38";
                busgoto = "함덕 방면";
                break;

            //제주국제공항(다호마을)
            case "405200003":
                busnum = "200";
                busgoto = "수산리 방면";
                break;
            case "405755001":
                busnum = "755";
                busgoto = "모슬포항 방면";
                break;
            case "405500001":
                busnum = "500";
                busgoto = "한라대 방면";
                break;
            case "405200021":
                busnum = "200";
                busgoto = "백록초 방면";
                break;
            case "405200002":
                busnum = "200";
                busgoto = "백록초 방면";
                break;

            case "405037002":
                busnum = "37";
                busgoto = "번대 방면";
                break;

            case "405200008":
                busnum = "200";
                busgoto = "관광대 방면";
                break;

            case "405038001":
                busnum = "38";
                busgoto = "하귀(수산) 방면";
                break;

            case "405095002":
                busnum = "95";
                busgoto = "한라수목원 방면";
                break;

            case "405200009":
                busnum = "200";
                busgoto = "수산리 방면";
                break;

            case "405037001":
                busnum = "37";
                busgoto = "수산 방면";
                break;

            case "405036001":
                busnum = "38";
                busgoto = "외도부영 방면";
                break;

            case "405200013":
                busnum = "200";
                busgoto = "백록초 방면";
                break;

            //제주국제공항(시외버스터미널)
            case "405755002":
                busnum = "755";
                busgoto = "시외버스터미널 방면";
                break;
            case "405755005":
                busnum = "755";
                busgoto = "시외버스터미널 방면";
                break;

        }
        if (what.equals("busnum"))
            return busnum;
        else if (what.equals("busgoto"))
            return busgoto;
        else
            return "";
    }
}
