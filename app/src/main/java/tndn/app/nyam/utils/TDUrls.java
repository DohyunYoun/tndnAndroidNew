package tndn.app.nyam.utils;

public class TDUrls {

    /**
     * TNDN URL
     */
//    실제 서버
//    private String TNDNURL = "http://52.69.30.53:505/";
//    테스트 서버
    private String TNDNURL = "http://52.69.34.17:505";


    /**
     * BASE
     */
    public String logHomeURL = TNDNURL + "/logHome";
    public String currencyURL = "http://tndn.net/api/currency";
    public String errorCheckURL = TNDNURL + "/errorCheck";
    public String getImageURL = TNDNURL + "/getImage";

    /**
     * USER
     */
    public String getUserURL = TNDNURL + "/getUser";
    public String setUserURL = TNDNURL + "/setUser";
    public String userLoginURL = TNDNURL + "/userLogin";
    public String userJoinURL = TNDNURL + "/userJoin";

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

}
