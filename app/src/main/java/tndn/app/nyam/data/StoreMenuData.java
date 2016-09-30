package tndn.app.nyam.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class StoreMenuData implements Serializable {

    /*
"{
  ""result"": ""success"",
  ""info"": [
    {
      ""id"": 6,
      ""classify_kor"": ""한식"",
      ""classify_chn"": ""韩国料理"",
      ""category_name_kor"": ""제주시청"",
      ""category_name_chn"": ""济州市政府"",
      ""name_kor"": ""티엔디엔"",
      ""name_chn"": ""甜点"",
      ""address_kor"": ""제주 제주시 중앙로 217(이도이동)"",
      ""address_chn"": null,
      ""main_menu_kor"": ""티엔디엔"",
      ""main_menu_chn"": ""티엔디엔"",
      ""tel_1"": ""070"",
      ""tel_2"": ""8670"",
      ""tel_3"": ""9409"",
      ""business_hour_open"": ""0900"",
      ""business_hour_closed"": ""2400"",
      ""detail_kor"": ""이곳에서도, 어제쓴것처럼"",
      ""detail_chn"": ""이곳에서도, 어제쓴것처럼"",
      ""budget"": ""10000"",
      ""latitude"": ""33.29226303100586"",
      ""longitude"": ""126.5948715209961"",
      ""is_pay"": 1,
      ""menu_input_type"": 0,
      ""distance"": 13330.99
    }
  ],
  ""images"": [
 {
      ""idx_image_file_path"": 329,
      ""info_flag"": 0
    }
],
  ""menus"": [
    {
      ""id"": 9500,
      ""idx_store"": 6,
      ""menu_price"": ""10000"",
      ""menu_price_for_ice"": null,
      ""menu_size"": null,
      ""menu_category"": 1,
      ""is_best_menu"": 0,
      ""is_image_menu"": 0,
      ""menu_name_kor"": ""돈가스우동정식"",
      ""menu_name_chn"": ""炸猪排乌冬面套餐"",
      ""menu_name_eng"": ""Set Menu with Pork Cutlet and Udon"",
      ""menu_name_jpn"": ""トンカツ定食（うどん付）"",
      ""idx_image_file_path"": 1031
    },
]"
     */

    int id;
    int idx_store;
    String menu_price;
    String menu_price_for_ice;
    String menu_size;
    String menu_category;
    int is_best_menu;
    int is_image_menu;
    String menu_name_kor;
    String menu_name_chn;
    String menu_name_eng;
    String menu_name_jpn;
    int idx_image_file_path;
    int count = 0;
    int countForIce = 0;

    public StoreMenuData() {

    }

//
//    protected StoreMenuData(Parcel in) {
//
//        id = in.readInt();
//        idx_store = in.readInt();
//        menu_price = in.readString();
//        menu_price_for_ice = in.readString();
//        menu_size = in.readString();
//        menu_category = in.readString();
//        is_best_menu = in.readInt();
//        is_image_menu = in.readInt();
//        menu_name_kor = in.readString();
//        menu_name_chn = in.readString();
//        menu_name_eng = in.readString();
//        menu_name_jpn = in.readString();
//        idx_image_file_path = in.readInt();
//        count = in.readInt();
//        countForIce = in.readInt();
//
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int flags) {
//        parcel.writeInt(id);
//        parcel.writeInt(idx_store);
//        parcel.writeString(menu_price);
//        parcel.writeString(menu_price_for_ice);
//        parcel.writeString(menu_size);
//        parcel.writeString(menu_category);
//        parcel.writeInt(is_best_menu);
//        parcel.writeInt(is_image_menu);
//        parcel.writeString(menu_name_kor);
//        parcel.writeString(menu_name_chn);
//        parcel.writeString(menu_name_eng);
//        parcel.writeString(menu_name_jpn);
//        parcel.writeInt(idx_image_file_path);
//        parcel.writeInt(count);
//        parcel.writeInt(countForIce);
//
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdx_store() {
        return idx_store;
    }

    public void setIdx_store(int idx_store) {
        this.idx_store = idx_store;
    }

    public String getMenu_price() {
        return menu_price;
    }

    public void setMenu_price(String menu_price) {
        this.menu_price = menu_price;
    }

    public String getMenu_price_for_ice() {
        return menu_price_for_ice;
    }

    public void setMenu_price_for_ice(String menu_price_for_ice) {
        this.menu_price_for_ice = menu_price_for_ice;
    }

    public String getMenu_size() {
        return menu_size;
    }

    public void setMenu_size(String menu_size) {
        this.menu_size = menu_size;
    }

    public String getMenu_category() {
        return menu_category;
    }

    public void setMenu_category(String menu_category) {
        this.menu_category = menu_category;
    }

    public int getIs_best_menu() {
        return is_best_menu;
    }

    public void setIs_best_menu(int is_best_menu) {
        this.is_best_menu = is_best_menu;
    }

    public int getIs_image_menu() {
        return is_image_menu;
    }

    public void setIs_image_menu(int is_image_menu) {
        this.is_image_menu = is_image_menu;
    }

    public String getMenu_name_kor() {
        return menu_name_kor;
    }

    public void setMenu_name_kor(String menu_name_kor) {
        this.menu_name_kor = menu_name_kor;
    }

    public String getMenu_name_chn() {
        return menu_name_chn;
    }

    public void setMenu_name_chn(String menu_name_chn) {
        this.menu_name_chn = menu_name_chn;
    }

    public String getMenu_name_eng() {
        return menu_name_eng;
    }

    public void setMenu_name_eng(String menu_name_eng) {
        this.menu_name_eng = menu_name_eng;
    }

    public String getMenu_name_jpn() {
        return menu_name_jpn;
    }

    public void setMenu_name_jpn(String menu_name_jpn) {
        this.menu_name_jpn = menu_name_jpn;
    }

    public int getIdx_image_file_path() {
        return idx_image_file_path;
    }

    public void setIdx_image_file_path(int idx_image_file_path) {
        this.idx_image_file_path = idx_image_file_path;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCountForIce() {
        return countForIce;
    }

    public void setCountForIce(int countForIce) {
        this.countForIce = countForIce;
    }

    @Override
    public String toString() {
        return "StoreMenuData{" +
                "id=" + id +
                ", idx_store=" + idx_store +
                ", menu_price='" + menu_price + '\'' +
                ", menu_price_for_ice='" + menu_price_for_ice + '\'' +
                ", menu_size='" + menu_size + '\'' +
                ", menu_category='" + menu_category + '\'' +
                ", is_best_menu=" + is_best_menu +
                ", is_image_menu=" + is_image_menu +
                ", menu_name_kor='" + menu_name_kor + '\'' +
                ", menu_name_chn='" + menu_name_chn + '\'' +
                ", menu_name_eng='" + menu_name_eng + '\'' +
                ", menu_name_jpn='" + menu_name_jpn + '\'' +
                ", idx_image_file_path=" + idx_image_file_path +
                ", count=" + count +
                ", countForIce=" + countForIce +
                '}';
    }

//    public static final Creator<StoreMenuData> CREATOR = new Creator<StoreMenuData>() {
//        @Override
//        public StoreMenuData createFromParcel(Parcel in) {
//            return new StoreMenuData(in);
//        }
//
//        @Override
//        public StoreMenuData[] newArray(int size) {
//            return new StoreMenuData[size];
//        }
//    };
//
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }


}
