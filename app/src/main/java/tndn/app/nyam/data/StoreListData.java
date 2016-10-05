package tndn.app.nyam.data;

import java.io.Serializable;

/**
 * 2016 08 16
 * new api
 * <p/>
 * ResCategory -> StoreLIst
 */
public class StoreListData implements Serializable {

    /*
    {
  "result": "success",
  "data": [
    {
      "idx_store": 6758,
      "idx_store_classify": 13,
      "idx_region_category": 32,
      "classify_kor": "한식",
      "classify_chn": "韩国料理",
      "category_name_kor": "수원",
      "category_name_chn": "京畿道水原",
      "name_kor": "명동보리밥",
      "name_chn": "明洞大麦饭",
      "address_kor": "경기도 수원시 팔달구 효원로 249번길 18-5",
      "address_chn": null,
      "main_menu_kor": "大麦饭",
      "main_menu_chn": "大麦饭",
      "tel_1": "031",
      "tel_2": "224",
      "tel_3": "8959",
      "business_hour_open": "1000",
      "business_hour_closed": "2200",
      "budget": "",
      "latitude": "0",
      "longitude": "0",
      "is_pay": 0,
      "menu_input_type": 0,
      "idx_image_file_path": 654,
      "distance": 0
    },
     */
    int idx_store;
    int idx_store_classify;
    int idx_region_category;
    int idx_store_major_classify;
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
    String budget;
    String latitude;
    String longitude;
    String is_pay;
    String menu_input_type;
    int idx_image_file_path;
    String distance;

    public int getIdx_store() {
        return idx_store;
    }

    public void setIdx_store(int idx_store) {
        this.idx_store = idx_store;
    }

    public int getIdx_store_classify() {
        return idx_store_classify;
    }

    public void setIdx_store_classify(int idx_store_classify) {
        this.idx_store_classify = idx_store_classify;
    }

    public int getIdx_region_category() {
        return idx_region_category;
    }

    public void setIdx_region_category(int idx_region_category) {
        this.idx_region_category = idx_region_category;
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

    public String getIs_pay() {
        return is_pay;
    }

    public void setIs_pay(String is_pay) {
        this.is_pay = is_pay;
    }

    public String getMenu_input_type() {
        return menu_input_type;
    }

    public void setMenu_input_type(String menu_input_type) {
        this.menu_input_type = menu_input_type;
    }

    public int getIdx_image_file_path() {
        return idx_image_file_path;
    }

    public void setIdx_image_file_path(int idx_image_file_path) {
        this.idx_image_file_path = idx_image_file_path;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getIdx_store_major_classify() {
        return idx_store_major_classify;
    }

    public void setIdx_store_major_classify(int idx_store_major_classify) {
        this.idx_store_major_classify = idx_store_major_classify;
    }

    @Override
    public String toString() {
        return "StoreListData{" +
                "idx_store=" + idx_store +
                ", idx_store_classify=" + idx_store_classify +
                ", idx_region_category=" + idx_region_category +
                ", idx_store_major_classify=" + idx_store_major_classify +
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
                ", budget='" + budget + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", is_pay='" + is_pay + '\'' +
                ", menu_input_type='" + menu_input_type + '\'' +
                ", idx_image_file_path=" + idx_image_file_path +
                ", distance='" + distance + '\'' +
                '}';
    }
}
