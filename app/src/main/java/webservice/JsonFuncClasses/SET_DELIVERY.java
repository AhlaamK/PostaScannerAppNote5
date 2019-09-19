package webservice.JsonFuncClasses;

/**
 * Created by ahlaam.kazi on 1/20/2018.
 */

public class SET_DELIVERY {
    private String CASH_COLLECTED;
    private String IDENTIFIER;
    private String[] WAYBILL;
    private String EVENTTYPE;
    private String CUSTNAME;
    private String EVENT_NOTE;
    private String DRIVERCODE;
    private String BYRST;
    private String LONGITUDE;
    private String DATETIME;
    private String LATITUDE;


    public SET_DELIVERY(){
        CASH_COLLECTED="";
       IDENTIFIER="";
                   // [] WAYBILL="";
                     EVENTTYPE="";
                     CUSTNAME="";
                     EVENT_NOTE="";
                     DRIVERCODE="";
                     BYRST="";
                     LONGITUDE="";
                     DATETIME="";
        LATITUDE="";
    }

    public SET_DELIVERY( String CASH_COLLECTED,String IDENTIFIER,String[] WAYBILL,String EVENTTYPE,String CUSTNAME,String EVENT_NOTE,String DRIVERCODE,String BYRST, String LONGITUDE,String DATETIME ,String LATITUDE ) {
        this.CASH_COLLECTED=CASH_COLLECTED;
        this.IDENTIFIER=IDENTIFIER;
        this.WAYBILL=WAYBILL;
        this.EVENTTYPE=EVENTTYPE;
        this.CUSTNAME=CUSTNAME;
        this.EVENT_NOTE=EVENT_NOTE;
        this.DRIVERCODE=DRIVERCODE;
        this.BYRST=BYRST;
        this.LONGITUDE= LONGITUDE;
        this.DATETIME= DATETIME;
        this.LATITUDE= LATITUDE;

    }
}
