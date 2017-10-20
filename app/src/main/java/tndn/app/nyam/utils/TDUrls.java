package tndn.app.nyam.utils;

public class TDUrls {

    /**
     * TNDN URL
     */
//    실제 서버
//    private String TNDNURL = "http://52.69.30.53:505";
//    테스트 서버
    private String TNDNURL = "http://52.69.58.115:505";


    /**
     * BASE
     */
    public String logHomeURL = TNDNURL + "/logHome";
    public String currencyURL = "http://tndn.net/api/currency";
    public String errorCheckURL = TNDNURL + "/errorCheck2";
    public String getImageURL = TNDNURL + "/getImage";
    public String getBannerURL = TNDNURL + "/getBanner";
    public String getBannerInfoURL = TNDNURL + "/getBannerInfo";

    public String chkVersion = TNDNURL + "/chkVersion";

    /**
     * USER
     */
    public String getUserInfoURL = TNDNURL + "/getUserInfo";
    public String setUserEditURL = TNDNURL + "/setUserEdit";
    public String getTndnLoginURL = TNDNURL + "/getTndnLogin";
    public String getSocialLoginURL = TNDNURL + "/getSocialLogin";
    public String setTndnJoinURL = TNDNURL + "/setTndnJoin";

    /**
     * STORE
     */
    public String getStoreListURL = TNDNURL + "/getStoreList";
    public String getStoreInfoURL = TNDNURL + "/getStoreInfo";
    public String getStorePreOrderURL = TNDNURL + "/getStorePreOrder";

    /**
     * MAP
     */
    public String getMapBusAirportURL = TNDNURL + "/getMapBusAirport";
    public String getMapFromURL = TNDNURL + "/getMapFrom";
    public String getMapNameURL = TNDNURL + "/getMapName";
    public String getMapRouteURL = TNDNURL + "/getMapRoute";

    /**
     * PAY
     */
    public String setStoreInstantOrder = TNDNURL + "/setStoreInstantOrder";
    public String setStorePay = TNDNURL + "/setStorePay";
    public String cscanbHwaxherURL = "http://www.tndntravel.com/api/pay/hwaxher/cscanbwechatSign";


    /**
     * ADD
     */
    public String qualityRequest = "http://www.tndn.co.kr/quality/request";
}
