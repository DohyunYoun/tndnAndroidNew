package tndn.app.nyam.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * This class is for use sharedPreferences to easy on evenywhere.
 * Implement singleton pattern. If you want add value.
 * You can modify sample code.
 * <p/>
 * Sample code. (You will change all key(KEY,Key,key) to value name use before)
 * private static final String KEY = "key";
 * private String key = "";
 * <p/>
 * public void setKey(String key){
 * this.key = key;
 * mEditor.putString(KEY,key);
 * mEditor.commit();
 * }
 * <p/>
 * public String getKey(){
 * if(key.equals("")){
 * key = mPrefs.getString("KEY", "");
 * }
 * return key;
 * }
 * //background select
 * private static final String BGSELECT = "bgSelect";
 * private int bgSelect = 1;
 * <p/>
 * public void setBgSelect(int bgSelect){
 * this.bgSelect = bgSelect;
 * mEditor.putInt(BGSELECT, bgSelect);
 * mEditor.commit();
 * }
 * <p/>
 * public int getBgSelect(){
 * if(bgSelect == 1){
 * bgSelect = mPrefs.getInt(BGSELECT, 1);
 * }
 * return bgSelect;
 * }
 */

/**
 * SAve ARRAY LIst
 * //Retrieve the values
 * Gson gson = new Gson();
 * String jsonText = Prefs.getString("key", null);
 * String[] text = gson.fromJson(jsonText, new TypeToken<List<JsonLog>>(){}.getType());  //EDIT: gso to gson
 * <p>
 * <p>
 * //Set the values
 * Gson gson = new Gson();
 * List<String> textList = new ArrayList<String>();
 * textList.addAll(data);
 * String jsonText = gson.toJson(textList);
 * prefsEditor.putString("key", jsonText);
 * prefsEditor.commit();
 */
/*
*getpref
* PreferenceManager.getInstance().getPhonenum()
*
* setpref
* PreferenceManager.getInstance().setPphoneNum(phone);
*
 */
public class PreferenceManager {

    private static final String PREF_NAME = "tndn_prefs.xml";

    private static PreferenceManager instance;

    public static PreferenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferenceManager(context);
        }
        return instance;
    }

    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    private PreferenceManager(Context context) {
        mPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();
    }

    /**
     * BASE
     */
    //"OK", "" location check
    private static final String LOCATIONCHECK = "LOCATIONCHECK";
    private String locationcheck = "";

    public void setLocationcheck(String key) {
        this.locationcheck = key;
        clear(LOCATIONCHECK);
        mEditor.putString(LOCATIONCHECK, key);
        mEditor.commit();
    }

    public String getLocationcheck() {
        if (locationcheck.equals("")) {
            locationcheck = mPrefs.getString(LOCATIONCHECK, "");
        }
        return locationcheck;
    }

    /**
     * USER LOG
     */
    //log__useris
    private static final String USERIS = "USERIS";
    private String useris = "";

    public void setUseris(String key) {
        this.useris = key;
        clear(USERIS);
        mEditor.putString(USERIS, key);
        mEditor.commit();
    }

    public String getUseris() {
        if (useris.equals("")) {
            useris = mPrefs.getString(USERIS, "");
        }
        return useris;
    }

    private static final String USERCODE = "USERCODE";
    private String usercode = "";

    public void setUsercode(String key) {
        this.usercode = key;
        clear(USERCODE);
        mEditor.putString(USERCODE, key);
        mEditor.commit();
    }

    public String getUsercode() {
        if (usercode.equals("")) {
            usercode = mPrefs.getString(USERCODE, "");
        }
        return usercode;
    }

    //log_____user from
    private static final String USERFROM = "USERFROM";
    private String userfrom = "";

    public void setUserfrom(String key) {
        this.userfrom = key;
        clear(USERFROM);
        mEditor.putString(USERFROM, key);
        mEditor.commit();
    }

    public String getUserfrom() {
        if (userfrom.equals("")) {
            userfrom = mPrefs.getString(USERFROM, "");
        }
        return userfrom;
    }

    /**
     * GET STORE LIST
     */
    private static final String LOCALIZATION = "LOCALIZATION";
    private String localization = "";

    public void setLocalization(String key) {
        this.localization = key;
        clear(LOCALIZATION);
        mEditor.putString(LOCALIZATION, key);
        mEditor.commit();
    }

    public String getLocalization() {
        if (localization.equals("")) {
            localization = mPrefs.getString(LOCALIZATION, "");
        }
        return localization;
    }


    //foodId
    private static final String FOODID = "FOODID";
    private String foodId = "";

    public void setFoodId(String key) {
        this.foodId = key;
        clear(FOODID);
        mEditor.putString(FOODID, key);
        mEditor.commit();
    }

    public String getFoodId() {
        if (foodId.equals("")) {
            foodId = mPrefs.getString(FOODID, "");
        }
        return foodId;
    }

    //locationId
    private static final String LOCATIONID = "LOCATIONID";
    private String locationId = "";

    public void setLocationId(String key) {
        this.locationId = key;
        clear(LOCATIONID);
        mEditor.putString(LOCATIONID, key);
        mEditor.commit();
    }

    public String getLocationId() {
        if (locationId.equals("")) {
            locationId = mPrefs.getString(LOCATIONID, "");
        }
        return locationId;
    }


    //orderby
    private static final String ORDERBY = "ORDERBY";
    private String orderby = "";

    public void setOrderby(String key) {
        this.orderby = key;
        clear(ORDERBY);
        mEditor.putString(ORDERBY, key);
        mEditor.commit();
    }

    public String getOrderby() {
        if (orderby.equals("")) {
            orderby = mPrefs.getString(ORDERBY, "");
        }
        return orderby;
    }

    //order data
    private static final String ORDERDATA = "ORDERDATA";
    private String orderdata = "";

    public void setOrderdata(String key) {
        this.orderdata = key;
        clear(ORDERDATA);
        mEditor.putString(ORDERDATA, key);
        mEditor.commit();
    }

    public String getOrderdata() {
        if (orderdata.equals("")) {
            orderdata = mPrefs.getString(ORDERDATA, "");
        }
        return orderdata;
    }
/**
 * END STORE LIST
 */

    /**
     * Currency
     */
    //currency save
    private static final String CURRENCY = "CURRENCY";
    private String currency = "";

    public void setCurrency(String key) {
        this.currency = key;
        clear(CURRENCY);
        mEditor.putString(CURRENCY, key);
        mEditor.commit();
    }

    public String getCurrency() {
        if (currency.equals("")) {
            currency = mPrefs.getString(CURRENCY, "");
        }
        return currency;
    }

    //currency date save
    private static final String CURRDATE = "CURRDATE";
    private String currdate = "";

    public void setCurrDate(String key) {
        this.currdate = key;
        clear(CURRDATE);
        mEditor.putString(CURRDATE, key);
        mEditor.commit();
    }

    public String getCurrDate() {
        if (currdate.equals("")) {
            currdate = mPrefs.getString(CURRDATE, "");
        }
        return currdate;
    }

    /**
     * TDMypageActivity
     */

    //set name
    private static final String USERNAME = "USERNAME";
    private String username = "";

    public void setUsername(String key) {
        this.username = key;
        clear(USERNAME);
        mEditor.putString(USERNAME, key);
        mEditor.commit();
    }

    public String getUsername() {
        if (username.equals("")) {
            username = mPrefs.getString(USERNAME, "");
        }
        return username;
    }

    //set email
    private static final String USEREMAIL = "USEREMAIL";
    private String useremail = "";

    public void setUseremail(String key) {
        this.useremail = key;
        clear(USEREMAIL);
        mEditor.putString(USEREMAIL, key);
        mEditor.commit();
    }

    public String getUseremail() {
        if (useremail.equals("")) {
            useremail = mPrefs.getString(USEREMAIL, "");
        }
        return useremail;
    }

    //set tndnid(usercode)
    private static final String TNDNID = "TNDNID";
    private String tndnid = "";

    public void setTndnid(String key) {
        this.tndnid = key;
        clear(TNDNID);
        mEditor.putString(TNDNID, key);
        mEditor.commit();
    }

    public String getTndnid() {
        if (tndnid.equals("")) {
            tndnid = mPrefs.getString(TNDNID, "");
        }
        return tndnid;
    }


    //when user logged in. set idx_app_user and send to request
    private static final String IDXAPPUSER = "IDXAPPUSER";
    private String idxAppUser = "";

    public void setIdxAppUser(String key) {
        this.idxAppUser = key;
        clear(IDXAPPUSER);
        mEditor.putString(IDXAPPUSER, key);
        mEditor.commit();
    }

    public String getIdxAppUser() {
        if (idxAppUser.equals("")) {
            idxAppUser = mPrefs.getString(IDXAPPUSER, "");
        }
        return idxAppUser;
    }


    /**
     * FOR MAP
     * 맵부부 수정하면 싹 다 수정해야지
     */
    //route end search intent
    private static final String MAPROUTEINTENT = "MAPROUTEINTENT";
    private String maprouteintent = "";

    public void setMaprouteintent(String key) {
        this.maprouteintent = key;
        clear(MAPROUTEINTENT);
        mEditor.putString(MAPROUTEINTENT, key);
        mEditor.commit();
    }

    public String getMaprouteintent() {
        if (maprouteintent.equals("")) {
            maprouteintent = mPrefs.getString(MAPROUTEINTENT, "");
        }
        return maprouteintent;
    }

    //start temp data
    private static final String MAPSTARTDATA = "MAPSTARTDATA";
    private String mapstartdata = "";

    public void setMapstartdata(String key) {
        this.mapstartdata = key;
        clear(MAPSTARTDATA);
        mEditor.putString(MAPSTARTDATA, key);
        mEditor.commit();
    }

    public String getMapstartdata() {
        if (mapstartdata.equals("")) {
            mapstartdata = mPrefs.getString(MAPSTARTDATA, "");
        }
        return mapstartdata;
    }

    //end temp data
    private static final String MAPENDDATA = "MAPENDDATA";
    private String mapenddata = "";

    public void setMapenddata(String key) {
        this.mapenddata = key;
        clear(MAPENDDATA);
        mEditor.putString(MAPENDDATA, key);
        mEditor.commit();
    }

    public String getMapenddata() {
        if (mapenddata.equals("")) {
            mapenddata = mPrefs.getString(MAPENDDATA, "");
        }
        return mapenddata;
    }


    //map check
    private static final String MAPINTENT = "MAPINTENT";
    private String mapintent = "";

    public void setMapintent(String key) {
        this.mapintent = key;
        clear(MAPINTENT);
        mEditor.putString(MAPINTENT, key);
        mEditor.commit();
    }

    public String getMapintent() {
        if (mapintent.equals("")) {
            mapintent = mPrefs.getString(MAPINTENT, "");
        }
        return mapintent;
    }

    //"OK", ""
    private static final String FROMINFO = "FROMINFO";
    private String frominfo = "";

    public void setFrominfo(String key) {
        this.frominfo = key;
        clear(FROMINFO);
        mEditor.putString(FROMINFO, key);
        mEditor.commit();
    }

    public String getFrominfo() {
        if (frominfo.equals("")) {
            frominfo = mPrefs.getString(FROMINFO, "");
        }
        return frominfo;
    }

    /**
     * END MAP
     */

    //clear
    public void clear(String key) {
        mEditor.remove(key);
        mEditor.commit();
    }

}
