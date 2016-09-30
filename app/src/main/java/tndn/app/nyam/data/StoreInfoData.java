package tndn.app.nyam.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 2016 08 16
 */
public class StoreInfoData implements Parcelable {


    int id;
    String classify_kor;
    String classify_chn;
    String category_name_kor;
    String category_name_chn;
    String name_kor;
    String name_chn;
    String address_kor;
    String address_chn;
    String main_menu_kor;
    String main_menu_chn;
    String tel_1;
    String tel_2;
    String tel_3;
    String business_hour_open;
    String business_hour_closed;
    String detail_kor;
    String detail_chn;
    String budget;
    String latitude;
    String longitude;
    int is_pay;
    String menu_input_type;
    String distance = "";
    ArrayList<Integer> images;
    ArrayList<StoreMenuData> menus;

    protected StoreInfoData(Parcel in) {

        id = in.readInt();
        classify_kor = in.readString();
        classify_chn = in.readString();
        category_name_kor = in.readString();
        category_name_chn = in.readString();
        name_kor = in.readString();
        name_chn = in.readString();
        address_kor = in.readString();
        address_chn = in.readString();
        main_menu_kor = in.readString();
        main_menu_chn = in.readString();
        tel_1 = in.readString();
        tel_2 = in.readString();
        tel_3 = in.readString();
        business_hour_open = in.readString();
        business_hour_closed = in.readString();
        detail_kor = in.readString();
        detail_chn = in.readString();
        budget = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        is_pay = in.readInt();
        menu_input_type = in.readString();
        distance = in.readString();
        images = (ArrayList<Integer>) in.readSerializable();
        menus = (ArrayList<StoreMenuData>) in.readSerializable();


    }

    public StoreInfoData() {

    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {

        parcel.writeInt(id);
        parcel.writeString(classify_kor);
        parcel.writeString(classify_chn);
        parcel.writeString(category_name_kor);
        parcel.writeString(category_name_chn);
        parcel.writeString(name_kor);
        parcel.writeString(name_chn);
        parcel.writeString(address_kor);
        parcel.writeString(address_chn);
        parcel.writeString(main_menu_kor);
        parcel.writeString(main_menu_chn);
        parcel.writeString(tel_1);
        parcel.writeString(tel_2);
        parcel.writeString(tel_3);
        parcel.writeString(business_hour_open);
        parcel.writeString(business_hour_closed);
        parcel.writeString(detail_kor);
        parcel.writeString(detail_chn);
        parcel.writeString(budget);
        parcel.writeString(latitude);
        parcel.writeString(longitude);
        parcel.writeInt(is_pay);
        parcel.writeString(menu_input_type);
        parcel.writeString(distance);
        parcel.writeSerializable(images);
        parcel.writeSerializable(menus);
    }

    @Override
    public String toString() {
        return "StoreInfoData{" +
                "id=" + id +
                ", classify_kor='" + classify_kor + '\'' +
                ", classify_chn='" + classify_chn + '\'' +
                ", category_name_kor='" + category_name_kor + '\'' +
                ", category_name_chn='" + category_name_chn + '\'' +
                ", name_kor='" + name_kor + '\'' +
                ", name_chn='" + name_chn + '\'' +
                ", address_kor='" + address_kor + '\'' +
                ", address_chn='" + address_chn + '\'' +
                ", main_menu_kor='" + main_menu_kor + '\'' +
                ", main_menu_chn='" + main_menu_chn + '\'' +
                ", tel_1='" + tel_1 + '\'' +
                ", tel_2='" + tel_2 + '\'' +
                ", tel_3='" + tel_3 + '\'' +
                ", business_hour_open='" + business_hour_open + '\'' +
                ", business_hour_closed='" + business_hour_closed + '\'' +
                ", detail_kor='" + detail_kor + '\'' +
                ", detail_chn='" + detail_chn + '\'' +
                ", budget='" + budget + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", is_pay=" + is_pay +
                ", menu_input_type='" + menu_input_type + '\'' +
                ", distance='" + distance + '\'' +
                ", images=" + images +
                ", menus=" + menus +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassify_kor() {
        return classify_kor;
    }

    public void setClassify_kor(String classify_kor) {
        this.classify_kor = classify_kor;
    }

    public String getClassify_chn() {
        return classify_chn;
    }

    public void setClassify_chn(String classify_chn) {
        this.classify_chn = classify_chn;
    }

    public String getCategory_name_kor() {
        return category_name_kor;
    }

    public void setCategory_name_kor(String category_name_kor) {
        this.category_name_kor = category_name_kor;
    }

    public String getCategory_name_chn() {
        return category_name_chn;
    }

    public void setCategory_name_chn(String category_name_chn) {
        this.category_name_chn = category_name_chn;
    }

    public String getName_kor() {
        return name_kor;
    }

    public void setName_kor(String name_kor) {
        this.name_kor = name_kor;
    }

    public String getName_chn() {
        return name_chn;
    }

    public void setName_chn(String name_chn) {
        this.name_chn = name_chn;
    }

    public String getAddress_kor() {
        return address_kor;
    }

    public void setAddress_kor(String address_kor) {
        this.address_kor = address_kor;
    }

    public String getAddress_chn() {
        return address_chn;
    }

    public void setAddress_chn(String address_chn) {
        this.address_chn = address_chn;
    }

    public String getMain_menu_kor() {
        return main_menu_kor;
    }

    public void setMain_menu_kor(String main_menu_kor) {
        this.main_menu_kor = main_menu_kor;
    }

    public String getMain_menu_chn() {
        return main_menu_chn;
    }

    public void setMain_menu_chn(String main_menu_chn) {
        this.main_menu_chn = main_menu_chn;
    }

    public String getTel_1() {
        return tel_1;
    }

    public void setTel_1(String tel_1) {
        this.tel_1 = tel_1;
    }

    public String getTel_2() {
        return tel_2;
    }

    public void setTel_2(String tel_2) {
        this.tel_2 = tel_2;
    }

    public String getTel_3() {
        return tel_3;
    }

    public void setTel_3(String tel_3) {
        this.tel_3 = tel_3;
    }

    public String getBusiness_hour_open() {
        return business_hour_open;
    }

    public void setBusiness_hour_open(String business_hour_open) {
        this.business_hour_open = business_hour_open;
    }

    public String getBusiness_hour_closed() {
        return business_hour_closed;
    }

    public void setBusiness_hour_closed(String business_hour_closed) {
        this.business_hour_closed = business_hour_closed;
    }

    public String getDetail_kor() {
        return detail_kor;
    }

    public void setDetail_kor(String detail_kor) {
        this.detail_kor = detail_kor;
    }

    public String getDetail_chn() {
        return detail_chn;
    }

    public void setDetail_chn(String detail_chn) {
        this.detail_chn = detail_chn;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getIs_pay() {
        return is_pay;
    }

    public void setIs_pay(int is_pay) {
        this.is_pay = is_pay;
    }

    public String getMenu_input_type() {
        return menu_input_type;
    }

    public void setMenu_input_type(String menu_input_type) {
        this.menu_input_type = menu_input_type;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public ArrayList<Integer> getImages() {
        return images;
    }

    public void setImages(ArrayList<Integer> images) {
        this.images = images;
    }

    public ArrayList<StoreMenuData> getMenus() {
        return menus;
    }

    public void setMenus(ArrayList<StoreMenuData> menus) {
        this.menus = menus;
    }

    public static final Creator<StoreInfoData> CREATOR = new Creator<StoreInfoData>() {
        @Override
        public StoreInfoData createFromParcel(Parcel in) {
            return new StoreInfoData(in);
        }

        @Override
        public StoreInfoData[] newArray(int size) {
            return new StoreInfoData[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }
}
